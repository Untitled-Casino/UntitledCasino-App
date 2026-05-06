package com.example.untitledcasino

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute {

}

@Composable
fun HomeScreen(
    onGameSelection: () -> Unit,
    onOpenCredits: () -> Unit,
    onGetHelp: () -> Unit,
    playerRepo: PlayerRepo,
) {
    Column(
        modifier = Modifier.safeContentPadding().fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CreditBalance(playerRepo)
        TextButton(onClick = onGameSelection) {
            Text("Games")
        }
        TextButton(onClick = onOpenCredits) {
            Text("Credits")
        }

        TextButton(onClick = onGetHelp) {
            Text("Get Help")
        }
    }
}
