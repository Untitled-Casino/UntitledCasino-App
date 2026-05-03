package com.example.untitledcasino.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.untitledcasino.PlayerRepo
import com.example.untitledcasino.game.vm.GameVM
import kotlinx.serialization.Serializable

@Serializable
data object GameScreenRoute {

}

@Composable
fun GameScreen(gameContent: GameContent, playerRepo: PlayerRepo) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(gameContent.title)
        Box() {
            gameContent.visuals()
        }
        Column() {
            gameContent.controls()
            BetInput(vm = gameContent.viewModel)
        }
    }
}

@Composable
fun BetInput(vm: GameVM) {
    val bet by remember { mutableStateOf(0) }
    Row() {
        Text("Bet Amount: ${vm.betAmount}")

    }
}