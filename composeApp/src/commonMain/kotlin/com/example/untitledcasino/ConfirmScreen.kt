package com.example.untitledcasino

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import untitledcasino.composeapp.generated.resources.Res
import untitledcasino.composeapp.generated.resources.cancel

@Serializable
data class ConfirmRoute(
    val creditsReceive: Int,
) {
}

@Composable
fun ConfirmScreen(
    option: CreditPurchaseOption,
    onSuccess: (creditsReceive: CreditPurchaseOption) -> Unit,
    onFailure: () -> Unit,
    playerRepo: PlayerRepo,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CreditBalance(playerRepo)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Confirm purchase of ${formatWithCommas(option.creditsReceive)} credits?",
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    // HERE IS WHERE IN-APP PURCHASE WOULD HAPPEN
                    onSuccess(option)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            ) {
                Text(
                    text = formatPrice(option.priceInCents),
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(Modifier.width(16.dp))
            Button(
                onClick = {
                    onFailure()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            ) {
                Text(
                    text = stringResource(Res.string.cancel),
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}