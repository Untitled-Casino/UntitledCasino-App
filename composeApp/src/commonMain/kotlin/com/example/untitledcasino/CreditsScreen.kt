package com.example.untitledcasino

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import untitledcasino.composeapp.generated.resources.Res
import untitledcasino.composeapp.generated.resources.credit_pile
import untitledcasino.composeapp.generated.resources.credits

@Serializable
data object CreditsRoute {

}

data class CreditPurchaseOption(
    val creditsReceive: Int,
    val priceInCents: Int,
)

val creditPurchaseOptions = listOf(
    CreditPurchaseOption(100, 149),
    CreditPurchaseOption(500, 499),
    CreditPurchaseOption(1000, 899),
    CreditPurchaseOption(5000, 3999),
    CreditPurchaseOption(10000, 7499),
    CreditPurchaseOption(100000, 49999),
)
val creditPurchaseOptionsMap = creditPurchaseOptions.associateBy { it.creditsReceive }

@Composable
fun CreditsScreen(
    onPurchase: (option: CreditPurchaseOption) -> Unit,
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
                    text = "${formatWithCommas(currentOption!!.creditsReceive)} Credits",
                    color = MaterialTheme.colorScheme.onBackground
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
                    Text("Purchase")
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
        modifier = Modifier.padding(4.dp).fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = formatWithCommas(option.creditsReceive),
            )
            Icon(
                painterResource(Res.drawable.credit_pile),
                contentDescription = stringResource(Res.string.credits),
                tint = androidx.compose.ui.graphics.Color.Unspecified,
            )
            Text(
                text = formatPrice(option.priceInCents),
            )
        }
    }
}