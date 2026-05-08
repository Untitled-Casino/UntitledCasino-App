package com.moravian.untitledcasino

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.moravian.untitledcasino.game.ChoiceBox
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import untitledcasino.composeapp.generated.resources.Res
import untitledcasino.composeapp.generated.resources.app_name
import untitledcasino.composeapp.generated.resources.casino_logo
import untitledcasino.composeapp.generated.resources.credits
import untitledcasino.composeapp.generated.resources.games
import untitledcasino.composeapp.generated.resources.get_help

@Serializable
data object HomeRoute

@Composable
fun HomeScreen(
    onGameSelection: () -> Unit,
    onOpenCredits: () -> Unit,
    onGetHelp: () -> Unit,
    playerRepo: PlayerRepo,
) {
    Column(
        modifier = Modifier.safeContentPadding().fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LogoAndTitle()
        CreditBalance(playerRepo)
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ChoiceBox(
                text = stringResource(Res.string.games),
                isDimmed = false,
                isEnabled = true,
                modifier = Modifier.weight(1f),
                onClick = onGameSelection,
                style = MaterialTheme.typography.titleLarge,
            )
            ChoiceBox(
                text = stringResource(Res.string.credits),
                isDimmed = false,
                isEnabled = true,
                modifier = Modifier.weight(1f),
                onClick = onOpenCredits,
                style = MaterialTheme.typography.titleLarge,
            )
        }
        ChoiceBox(
            text = stringResource(Res.string.get_help),
            isDimmed = false,
            isEnabled = true,
            modifier = Modifier.fillMaxWidth(),
            onClick = onGetHelp,
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}

@Composable
fun LogoAndTitle(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(Res.drawable.casino_logo),
            contentDescription = stringResource(Res.string.app_name),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(300.dp),
        )
    }
}
