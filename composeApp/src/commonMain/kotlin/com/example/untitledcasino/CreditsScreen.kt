package com.example.untitledcasino

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import untitledcasino.composeapp.generated.resources.Res
import untitledcasino.composeapp.generated.resources.*

@Serializable
data object CreditsRoute {

}

data class CreditPurchaseOption(
    val creditsReceive: Int,
    val priceInCents: Int,
    val img: DrawableResource,
)

val creditPurchaseOptions = listOf(
    CreditPurchaseOption(100, 149, Res.drawable.credit_1),
    CreditPurchaseOption(500, 499, Res.drawable.credit_2),
    CreditPurchaseOption(1000, 899, Res.drawable.credit_3),
    CreditPurchaseOption(5000, 3999, Res.drawable.credit_4),
    CreditPurchaseOption(10000, 7499, Res.drawable.credit_5),
    CreditPurchaseOption(100000, 49999, Res.drawable.credit_6),
)
val creditPurchaseOptionsMap = creditPurchaseOptions.associateBy { it.creditsReceive }

@Composable
fun CreditsScreen(
    onPurchase: (option: CreditPurchaseOption) -> Unit,
    onHistory: () -> Unit,
    playerRepo: PlayerRepo,
) {
    var currentOption by remember { mutableStateOf<CreditPurchaseOption?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp),
    ) {
        CreditBalance(playerRepo)
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onHistory
        ) {
            Text(
                text = "History",
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f)
        ) {
            items(creditPurchaseOptions) { option ->
                CreditPurchaseOptionItem(
                    option = option,
                    onClick = {
                        scope.launch {
                            currentOption = option
                        }
                    }
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        currentOption?.let {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "${formatWithCommas(currentOption!!.creditsReceive.toString())} Credits",
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Button(
                    onClick = {
                        onPurchase(currentOption!!)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    )
                ) {
                    Text(
                        text = "Purchase",
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
fun CreditPurchaseOptionItem(
    option: CreditPurchaseOption,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(4.dp).aspectRatio(1f)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = formatWithCommas(option.creditsReceive.toString()),
                fontWeight = FontWeight.Bold,
            )
            Icon(
                painterResource(option.img),
                contentDescription = stringResource(Res.string.credits),
                tint = androidx.compose.ui.graphics.Color.Unspecified,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = formatPrice(option.priceInCents),
            )
        }
    }
}