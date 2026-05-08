package com.moravian.untitledcasino.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.moravian.untitledcasino.game.vm.DailyNumberVM
import org.jetbrains.compose.resources.stringResource
import untitledcasino.composeapp.generated.resources.*

@Composable
fun DailyNumberControls(vm: DailyNumberVM) {
    Column(
        modifier = Modifier.width(360.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ChoiceBox(
            text = stringResource(Res.string.play),
            isDimmed = false,
            isEnabled = !vm.isBusy,
            modifier = Modifier.fillMaxWidth(),
            baseContainerColor = Color(0xFF0c9631),
            onClick = { vm.roll() },
        )
    }
}

@Composable
fun DailyNumberVisuals(vm: DailyNumberVM) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = vm.uiMessage.ifEmpty {
                stringResource(Res.string.roll_closest)
            },
            color = if (vm.uiMessage.contains("won")) Color.Green else Color.White,
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(Res.string.roll),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = vm.rolledNumber?.toString()?.padStart(4, '0') ?: "0000",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(Res.string.daily_goal),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = vm.goalNumber?.toString()?.padStart(4, '0') ?: "0000",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}
