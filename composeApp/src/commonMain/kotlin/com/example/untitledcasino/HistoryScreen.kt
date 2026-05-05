package com.example.untitledcasino

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.untitledcasino.data.PurchaseEntity
import kotlinx.serialization.Serializable

@Serializable
data object HistoryScreenRoute {

}

@Composable
fun <T> HistoryScreen(
    title: String,
    historyItems: List<T>,
    itemContent: @Composable (T) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        if (historyItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("No history available")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(historyItems) { item ->
                    itemContent(item)
                }
            }
        }
    }
}

@Composable
fun PurchaseHistoryRow(purchase: PurchaseEntity) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
    ) {
        Text("Purchase: ${formatWithCommas(purchase.credits.toString())} credits for ${formatPrice(purchase.priceInCents)} at ${formatEpochMillis(purchase.timestamp)}")
    }
}