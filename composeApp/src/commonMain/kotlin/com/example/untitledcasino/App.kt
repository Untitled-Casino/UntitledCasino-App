package com.example.untitledcasino

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.untitledcasino.data.CasinoDatabase
import com.example.untitledcasino.theme.UntitledCasinoTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import untitledcasino.composeapp.generated.resources.Res
import untitledcasino.composeapp.generated.resources.app_name
import untitledcasino.composeapp.generated.resources.arrow_back
import untitledcasino.composeapp.generated.resources.back
import untitledcasino.composeapp.generated.resources.purchase_failed
import untitledcasino.composeapp.generated.resources.purchase_success

@Composable
fun App(
    database: CasinoDatabase,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    val playerDao = database.getDao()
    val playerRepo = remember { PlayerRepo(playerDao) }

    LaunchedEffect(Unit) {
        playerRepo.initializePlayerIfNeeded()
    }

    UntitledCasinoTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                val curBackStackEntry by navController.currentBackStackEntryAsState()
                val curDestination = curBackStackEntry?.destination
                val onHomeScreen = curDestination?.hasRoute<HomeRoute>() == true
                TopBar(back = if (onHomeScreen) null else ({ navController.navigateUp() }))
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = HomeRoute,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<HomeRoute> {
                    HomeScreen(
                        onGameSelection = { navController.navigate(GameSelectionRoute) },
                        onOpenCredits = { navController.navigate(CreditsRoute) },
                        playerRepo = playerRepo,
                    )
                }
                composable<GameSelectionRoute> {
                    GameSelectionScreen(playerRepo = playerRepo)
                }
                composable<CreditsRoute> {
                    CreditsScreen(
                        onPurchase = { selectedOption ->
                            navController.navigate(ConfirmRoute(selectedOption.creditsReceive))
                        },
                        playerRepo = playerRepo
                    )
                }
                composable<ConfirmRoute> { navBackStackEntry ->
                    val route = navBackStackEntry.toRoute<ConfirmRoute>()
                    val option = creditPurchaseOptionsMap[route.creditsReceive]

                    ConfirmScreen(
                        option = option!!,
                        onSuccess = { option ->
                            scope.launch {
                                playerRepo.addCredits(option.creditsReceive)

                                val message = getString(
                                    Res.string.purchase_success,
                                    formatWithCommas(option.creditsReceive),
                                    formatPrice(option.priceInCents),
                                )

                                navController.navigate(HomeRoute) {
                                    popUpTo(HomeRoute) { inclusive = true }
                                }

                                snackbarHostState.showSnackbar(message)
                            }
                        },
                        onFailure = {
                            scope.launch {
                                val message = getString(Res.string.purchase_failed)

                                navController.navigate(HomeRoute) {
                                    popUpTo(HomeRoute) { inclusive = true }
                                }

                                snackbarHostState.showSnackbar(message)
                            }
                        },
                        playerRepo = playerRepo,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(back: (() -> Unit)? = null) {
    TopAppBar(
        title = { Text(stringResource(Res.string.app_name)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
            if (back != null) {
                Button(onClick = back) {
                    Icon(
                        painterResource(Res.drawable.arrow_back),
                        contentDescription = stringResource(Res.string.back),
                    )
                }
            }
        },
    )
}