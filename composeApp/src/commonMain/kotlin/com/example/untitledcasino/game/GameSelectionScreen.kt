package com.example.untitledcasino.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.untitledcasino.PlayerRepo
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import untitledcasino.composeapp.generated.resources.Res
import untitledcasino.composeapp.generated.resources.coin_flip_title
import untitledcasino.composeapp.generated.resources.hi_lo_title

@Serializable
data object GameSelectionRoute {

}

@Composable
fun GameSelectionScreen(
    onStartGame: (String) -> Unit,
    onHistory: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
    ) {
        Text(text = "Game Selection Screen",)
        Button(onClick = { onStartGame("coinflip") }) { Text(stringResource(Res.string.coin_flip_title)) }
        Button(onClick = { onStartGame("hilo") }) { Text(stringResource(Res.string.hi_lo_title))}
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onHistory
        ) {
            Text(
                text = "History",
                fontWeight = FontWeight.Bold,
            )
        }
    }
}