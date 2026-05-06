package com.example.untitledcasino.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.untitledcasino.CreditBalance
import com.example.untitledcasino.PlayerRepo
import com.example.untitledcasino.game.vm.GameVM
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import untitledcasino.composeapp.generated.resources.*

@Serializable
data class GameScreenRoute(
    val gameType: String,
)

@Composable
fun GameScreen(
    playerRepo: PlayerRepo,
    gameContent: GameContent,
    vm: GameVM,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CreditBalance(playerRepo)
        Text(
            text = gameContent.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            gameContent.visuals()
        }
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            gameContent.controls()
            BetInput(
                vm = vm,
                onBetEntered = { vm.betAmount = it },
            )
        }
    }
}

@Composable
fun BetInput(
    vm: GameVM,
    onBetEntered: (Int) -> Unit,
) {
    var betAmount by rememberSaveable { mutableStateOf("") }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(Res.string.bet),
                style = MaterialTheme.typography.bodyLarge,
            )

            OutlinedTextField(
                value = betAmount,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        betAmount = newValue
                    }
                },
                modifier = Modifier
                    .weight(2f)
                    .height(60.dp),
                placeholder = { Text("0") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
            )

            ChoiceBox(
                text = stringResource(Res.string.enter),
                modifier = Modifier.weight(1f),
                onClick = {
                    val amount = betAmount.toIntOrNull() ?: 0
                    betAmount = ""
                    onBetEntered(amount)
                },
                isEnabled = betAmount.isNotEmpty(),
            )
        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(
                Res.string.current_bet,
                vm.betAmount,
            ),
        )
    }
}
