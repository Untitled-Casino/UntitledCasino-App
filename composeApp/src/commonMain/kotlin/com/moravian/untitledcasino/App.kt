package com.moravian.untitledcasino

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.moravian.untitledcasino.data.CasinoDatabase
import com.moravian.untitledcasino.game.CoinFlipControls
import com.moravian.untitledcasino.game.CoinFlipVisuals
import com.moravian.untitledcasino.game.DailyNumberControls
import com.moravian.untitledcasino.game.DailyNumberVisuals
import com.moravian.untitledcasino.game.GAMES
import com.moravian.untitledcasino.game.GameContent
import com.moravian.untitledcasino.game.GameScreen
import com.moravian.untitledcasino.game.GameScreenRoute
import com.moravian.untitledcasino.game.GameSelectionRoute
import com.moravian.untitledcasino.game.GameSelectionScreen
import com.moravian.untitledcasino.game.HiLoControls
import com.moravian.untitledcasino.game.HiLoVisuals
import com.moravian.untitledcasino.game.vm.CoinFlipVM
import com.moravian.untitledcasino.game.vm.DailyNumberVM
import com.moravian.untitledcasino.game.vm.HiLoVM
import com.moravian.untitledcasino.theme.UntitledCasinoTheme
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import untitledcasino.composeapp.generated.resources.*

@Composable
fun App(
    database: CasinoDatabase,
) {
    val snackbarHostState = remember { SnackbarHostState() }
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

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination

    val topBarTitle = when {
        destination?.hasRoute<CreditsRoute>() == true -> stringResource(Res.string.credits)
        destination?.hasRoute<ConfirmRoute>() == true -> stringResource(Res.string.confirm_purchase)
        destination?.hasRoute<PurchaseHistoryScreenRoute>() == true -> stringResource(Res.string.history)
        destination?.hasRoute<GameplayHistoryScreenRoute>() == true -> stringResource(Res.string.history)
        destination?.hasRoute<GameSelectionRoute>() == true -> stringResource(Res.string.game_select)
        destination?.hasRoute<GameScreenRoute>() == true -> stringResource(Res.string.game)
        else -> stringResource(Res.string.app_name)
    }

    UntitledCasinoTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopBar(
                    back = if (navBackStackEntry?.destination?.hasRoute<HomeRoute>() == true) null else ({ navController.navigateUp() }),
                    text = topBarTitle,
                )
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = HomeRoute,
                modifier = Modifier.padding(innerPadding),
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
                        games = GAMES,
                        onStartGame = { selectedType -> navController.navigate(GameScreenRoute(gameType = selectedType)) },
                        onHistory = {
                            navController.navigate(GameplayHistoryScreenRoute)
                        },
                    )
                }
                composable<GameScreenRoute> { navBackStackEntry ->
                    val game = navBackStackEntry.toRoute<GameScreenRoute>()

                    val (vm, gameContent) = when (game.gameType) {
                        "coinflip" -> {
                            val specificVm: CoinFlipVM = viewModel()
                            specificVm.setup(playerRepo, stringResource(Res.string.coin_flip_title))
                            specificVm to GameContent(
                                title = specificVm.activeGame.value,
                                viewModel = specificVm,
                                visuals = { CoinFlipVisuals(specificVm) },
                                controls = { CoinFlipControls(specificVm) },
                            )
                        }

                        "hilo" -> {
                            val specificVm: HiLoVM = viewModel()
                            specificVm.setup(playerRepo, stringResource(Res.string.hi_lo_title))
                            specificVm to GameContent(
                                title = specificVm.activeGame.value,
                                viewModel = specificVm,
                                visuals = { HiLoVisuals(specificVm) },
                                controls = { HiLoControls(specificVm) },
                            )
                        }

                        "dailynumber" -> {
                            val specificVm: DailyNumberVM = viewModel()
                            specificVm.setup(playerRepo, stringResource(Res.string.daily_number_title))
                            specificVm.initClient(httpClient)
                            specificVm to GameContent(
                                title = specificVm.activeGame.value,
                                viewModel = specificVm,
                                visuals = { DailyNumberVisuals(specificVm) },
                                controls = { DailyNumberControls(specificVm) },
                            )
                        }

                        else -> {
                            error(stringResource(Res.string.unreachable_state))
                        }
                    }

                    GameScreen(playerRepo, gameContent, vm)
                }
                composable<CreditsRoute> {
                    CreditsScreen(
                        onPurchase = { selectedOption ->
                            navController.navigate(ConfirmRoute(selectedOption.creditsReceive))
                        },
                        onHistory = {
                            navController.navigate(PurchaseHistoryScreenRoute)
                        },
                        playerRepo = playerRepo,
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
                composable<PurchaseHistoryScreenRoute> {
                    val history by playerRepo.purchaseHistory.collectAsState(initial = emptyList())

                    HistoryScreen(
                        historyItems = history,
                        itemContent = { purchase ->
                            HistoryRow(
                                stringResource(
                                    Res.string.history_purchase,
                                    formatWithCommas(purchase.credits.toString()),
                                    formatPrice(purchase.priceInCents),
                                    formatEpochMillis(purchase.timestamp),
                                ),
                            )
                        },
                    )
                }
                composable<GameplayHistoryScreenRoute> {
                    val history by playerRepo.gameplayHistory.collectAsState(initial = emptyList())

                    HistoryScreen(
                        historyItems = history,
                        itemContent = { gameplay ->
                            HistoryRow(
                                stringResource(
                                    Res.string.history_gameplay,
                                    gameplay.gameName,
                                    formatWithCommas(gameplay.bet.toString()),
                                    formatWithCommas(gameplay.reward.toString()),
                                    formatEpochMillis(gameplay.timestamp),
                                ),
                            )
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    back: (() -> Unit)? = null,
    text: String,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
            if (back != null) {
                IconButton(onClick = back) {
                    Icon(
                        painterResource(Res.drawable.arrow_back),
                        contentDescription = stringResource(Res.string.back),
                    )
                }
            }
        },
    )
}
