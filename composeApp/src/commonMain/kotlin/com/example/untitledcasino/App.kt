package com.example.untitledcasino

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.untitledcasino.data.CasinoDatabase
import org.jetbrains.compose.resources.painterResource

import untitledcasino.composeapp.generated.resources.Res
import untitledcasino.composeapp.generated.resources.compose_multiplatform

@Composable
fun App(
    database: CasinoDatabase,
) {
    val navController = rememberNavController()

    MaterialTheme {
        Scaffold(
            topBar = {
                val curBackStackEntry by navController.currentBackStackEntryAsState()
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
                        database = database,
                    )
                }
                composable<GameSelectionScreen> {
                    GameSelectionScreen()
                }
                composable<CreditsScreen> {
                    CreditsScreen()
                }
            }
        }
    }
}