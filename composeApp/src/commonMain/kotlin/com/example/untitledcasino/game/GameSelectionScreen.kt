package com.example.untitledcasino.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.untitledcasino.PlayerRepo
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import untitledcasino.composeapp.generated.resources.Res
import untitledcasino.composeapp.generated.resources.coin_flip_title
import untitledcasino.composeapp.generated.resources.hi_lo_title
import untitledcasino.composeapp.generated.resources.piggy_bank

@Serializable
data object GameSelectionRoute {

}

@Composable
fun GameSelectionScreen(
    playerRepo: PlayerRepo,
    onStartGame: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
    ) {
        Text(text = "Game Selection Screen",)
        GameCard(
            title = stringResource(Res.string.coin_flip_title),
            gameID = "coinflip",
            ) { id -> onStartGame(id) }
        GameCard(
            title = stringResource(Res.string.hi_lo_title),
            gameID = "hilo",
        ) { id -> onStartGame(id) }
    }
}

@Composable
fun GameCard(
    title: String,
    gameID: String,
    onStartGame: (String) -> Unit,
) {
    Column() {
        GamePicture(
            resource = Res.drawable.piggy_bank,
        )
        PlayButton(
            text = title,
            gameID = gameID,
            modifier = Modifier.fillMaxWidth(),
            onStartGame = onStartGame
        )
    }
}

@Composable
fun GamePicture(
    resource: DrawableResource,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(resource),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
        )
    }
}

@Composable
fun PlayButton(
    text: String = "Button",
    gameID: String,
    modifier: Modifier = Modifier,
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    onStartGame: (String) -> Unit) {
    Box(
        modifier = modifier
            .height(60.dp)
            .background(
                color = buttonColor,
                shape = RectangleShape
            )
            .clickable(onClick = { onStartGame(gameID) }),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}