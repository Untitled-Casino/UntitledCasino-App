package com.example.untitledcasino

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
data object GameSelectionRoute {

}

@Composable
fun GameSelectionScreen(
    playerRepo: PlayerRepo,
) {
    Text("Game Selection Screen")
}