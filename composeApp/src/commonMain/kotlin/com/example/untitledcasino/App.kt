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
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavBackStackEntry
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
import com.example.untitledcasino.game.GAMES
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
        destination?.hasRoute<HomeRoute>() == true -> "Home"
        destination?.hasRoute<CreditsRoute>() == true -> "Credits"
        destination?.hasRoute<ConfirmRoute>() == true -> "Confirm Purchase"
        destination?.hasRoute<HistoryScreenRoute>() == true -> "History"
        destination?.hasRoute<GameSelectionRoute>() == true -> "Game Select"
        destination?.hasRoute<GameScreenRoute>() == true -> "Game"
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
                        games = GAMES,
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
                                historyItems = history,
                                itemContent = { purchase ->
                                    HistoryRow("Purchase: ${formatWithCommas(purchase.credits.toString())} credits for ${formatPrice(purchase.priceInCents)}\n${formatEpochMillis(purchase.timestamp)}")
                                }
                            )
                        }
                        HistoryType.GAMEPLAY -> {
                            val history by playerRepo.gameplayHistory.collectAsState(initial = emptyList())

                            HistoryScreen(
                                historyItems = history,
                                itemContent = { gameplay ->
                                    HistoryRow("${gameplay.gameName} - Bet: ${formatWithCommas(gameplay.bet.toString())} - Reward: ${formatWithCommas(gameplay.reward.toString())}\n${formatEpochMillis(gameplay.timestamp)}")
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
private fun TopBar(
    back: (() -> Unit)? = null,
    text: String,
) {
    TopAppBar(
        title = {
            Text(
                text =  text,
                fontWeight = FontWeight.Bold,
            )
        },
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