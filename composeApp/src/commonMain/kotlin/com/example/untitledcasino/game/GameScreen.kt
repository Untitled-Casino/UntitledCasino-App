package com.example.untitledcasino.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.untitledcasino.game.vm.GameVM
import kotlinx.serialization.Serializable

@Serializable
data object GameScreenRoute {

}

@Composable
fun GameScreen(gameContent: GameContent, vm: GameVM) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = gameContent.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            gameContent.visuals()
        }
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            gameContent.controls()
            BetInput(
                onBetEntered = { vm.betAmount = it }
            )
        }
    }
}

@Composable
fun BetInput(onBetEntered: (Int) -> Unit) {
    var betAmount by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Bet:",
            style = MaterialTheme.typography.bodyLarge
        )

        OutlinedTextField(
            value = betAmount,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    betAmount = newValue
                }
            },
            modifier = Modifier.weight(1f),
            placeholder = { Text("0") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Button(
            onClick = {
                val amount = betAmount.toIntOrNull() ?: 0
                onBetEntered(amount)
            },
            enabled = betAmount.isNotEmpty()
        ) {
            Text("Enter")
        }
    }
}