package com.example.untitledcasino

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
data object CreditsScreen {

}

@Composable
fun CreditsScreen(
    playerRepo: PlayerRepo,
) {
    Text("Credits Screen")
}