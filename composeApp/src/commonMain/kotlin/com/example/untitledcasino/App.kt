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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.untitledcasino.data.CasinoDatabase
import com.example.untitledcasino.game.CoinFlipControls
import com.example.untitledcasino.game.CoinFlipVisuals
import com.example.untitledcasino.game.DailyNumberControls
import com.example.untitledcasino.game.DailyNumberVisuals
import com.example.untitledcasino.game.GameContent
import com.example.untitledcasino.game.GameScreen
import com.example.untitledcasino.game.GameScreenRoute
import com.example.untitledcasino.game.GameSelectionRoute
import com.example.untitledcasino.game.GameSelectionScreen
import com.example.untitledcasino.game.HiLoControls
import com.example.untitledcasino.game.HiLoVisuals
import com.example.untitledcasino.game.vm.CoinFlipVM
import com.example.untitledcasino.game.vm.DailyNumberVM
import com.example.untitledcasino.game.vm.HiLoVM
import com.example.untitledcasino.theme.UntitledCasinoTheme
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import untitledcasino.composeapp.generated.resources.Res
import untitledcasino.composeapp.generated.resources.app_name
import untitledcasino.composeapp.generated.resources.arrow_back
import untitledcasino.composeapp.generated.resources.back
import untitledcasino.composeapp.generated.resources.coin_flip_title
import untitledcasino.composeapp.generated.resources.daily_number_title
import untitledcasino.composeapp.generated.resources.hi_lo_title
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

    val httpClient = remember {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
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
                composable<GetHelpRoute> {
                    CasinoWebView()
                }
                composable<HomeRoute> {
                    HomeScreen(
                        onGameSelection = { navController.navigate(GameSelectionRoute) },
                        onOpenCredits = { navController.navigate(CreditsRoute) },
                        onGetHelp = { navController.navigate(GetHelpRoute) },
                        playerRepo = playerRepo,
                    )
                }
                composable<GameSelectionRoute> {
                    GameSelectionScreen(
                        onStartGame = { selectedType -> navController.navigate(GameScreenRoute(gameType = selectedType)) },
                        onHistory = {
                            navController.navigate(HistoryScreenRoute(type = HistoryType.GAMEPLAY))
                        },
                    )
                }
                composable<GameScreenRoute> { navBackStackEntry ->
                    val game = navBackStackEntry.toRoute<GameScreenRoute>()

                    val (vm, gameContent) = when (game.gameType) {
                        "coinflip" -> {
                            val specificVm = CoinFlipVM(stringResource(Res.string.coin_flip_title))
                            specificVm.setup(playerRepo)
                            specificVm to GameContent(
                                title = specificVm.activeGame.value,
                                viewModel = specificVm,
                                visuals = { CoinFlipVisuals(specificVm) },
                                controls = { CoinFlipControls(specificVm) }
                            )
                        }
                        "hilo" -> {
                            val specificVm = HiLoVM(stringResource(Res.string.hi_lo_title))
                            specificVm.setup(playerRepo)
                            specificVm to GameContent(
                                title = specificVm.activeGame.value,
                                viewModel = specificVm,
                                visuals = { HiLoVisuals(specificVm) },
                                controls = { HiLoControls(specificVm) }
                            )
                        }
                        "dailynumber" -> {
                            val specificVm = DailyNumberVM(stringResource(Res.string.daily_number_title), httpClient)
                            specificVm.setup(playerRepo)
                            specificVm to GameContent(
                                title = specificVm.activeGame.value,
                                viewModel = specificVm,
                                visuals = { DailyNumberVisuals(specificVm) },
                                controls = { DailyNumberControls(specificVm) }
                            )
                        }
                        else -> TODO("Unsupported game type")
                    }

                    GameScreen(playerRepo,gameContent, vm)
                }
                composable<CreditsRoute> {
                    LaunchedEffect(Unit) {
                        playerRepo.addCredits(100)
                    }
                    CreditsScreen(
                        onPurchase = { selectedOption ->
                            navController.navigate(ConfirmRoute(selectedOption.creditsReceive))
                        },
                        onHistory = {
                            navController.navigate(HistoryScreenRoute(type = HistoryType.PURCHASE))
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
                                playerRepo.recordPurchase(option)

                                val message = getString(
                                    Res.string.purchase_success,
                                    formatWithCommas(option.creditsReceive.toString()),
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
                composable<HistoryScreenRoute> { navBackStackEntry ->
                    val route = navBackStackEntry.toRoute<HistoryScreenRoute>()

                    when (route.type) {
                        HistoryType.PURCHASE -> {
                            val history by playerRepo.purchaseHistory.collectAsState(initial = emptyList())

                            HistoryScreen(
                                title = "Purchase History",
                                historyItems = history,
                                itemContent = { purchase ->
                                    PurchaseHistoryRow(purchase)
                                }
                            )
                        }
                        HistoryType.GAMEPLAY -> {
                            val history by playerRepo.gameplayHistory.collectAsState(initial = emptyList())

                            HistoryScreen(
                                title = "Game History",
                                historyItems = history,
                                itemContent = { gameplay ->
                                    GameplayHistoryRow(gameplay)
                                }
                            )
                        }
                    }
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