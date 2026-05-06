package com.example.untitledcasino.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import untitledcasino.composeapp.generated.resources.Res
import untitledcasino.composeapp.generated.resources.coin_flip_image
import untitledcasino.composeapp.generated.resources.coin_flip_title
import untitledcasino.composeapp.generated.resources.daily_number_title
import untitledcasino.composeapp.generated.resources.hi_lo_image
import untitledcasino.composeapp.generated.resources.hi_lo_title
import untitledcasino.composeapp.generated.resources.piggy_bank

@Serializable
data object GameSelectionRoute {

}


@Composable
fun GameSelectionScreen(
    games: List<GameInfo>,
    onStartGame: (String) -> Unit,
    onHistory: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {


            items(items = games) {
                    game ->
                GameCard(
                    title = stringResource(game.title),
                    resource = game.resource,
                    gameID = game.gameID,
                ) { id -> onStartGame(id) }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))

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
    }
}

@Composable
fun GameCard(
    title: String,
    resource: DrawableResource,
    gameID: String,
    onStartGame: (String) -> Unit,
) {
    Column() {
        GamePicture(
            resource = resource,
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
            .height(150.dp)
            .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(resource),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .aspectRatio(16f / 9f),
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