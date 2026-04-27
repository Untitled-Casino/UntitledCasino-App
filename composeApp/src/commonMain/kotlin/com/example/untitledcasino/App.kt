package com.example.untitledcasino

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.untitledcasino.data.CasinoDatabase
import com.example.untitledcasino.theme.UntitledCasinoTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import untitledcasino.composeapp.generated.resources.Res
import untitledcasino.composeapp.generated.resources.app_name
import untitledcasino.composeapp.generated.resources.arrow_back
import untitledcasino.composeapp.generated.resources.back

@Composable
fun App(
    database: CasinoDatabase,
) {
    val navController = rememberNavController()

    val playerDao = database.getDao()
    val casinoVM = remember { CasinoVM(playerDao) }

    UntitledCasinoTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                val curBackStackEntry by navController.currentBackStackEntryAsState()
                val curDestination = curBackStackEntry?.destination
                val onHomeScreen = curDestination?.hasRoute<HomeScreen>() == true
                TopBar(back = if (onHomeScreen) null else ({ navController.navigateUp() }))
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = HomeScreen,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<HomeScreen> {
                    HomeScreen(
                        onGameSelection = { navController.navigate(GameSelectionScreen) },
                        onOpenCredits = { navController.navigate(CreditsScreen) },
                        viewModel = casinoVM,
                    )
                }
                composable<GameSelectionScreen> {
                    GameSelectionScreen(viewModel = casinoVM)
                }
                composable<CreditsScreen> {
                    CreditsScreen(viewModel = casinoVM)
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