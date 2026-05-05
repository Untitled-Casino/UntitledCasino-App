package com.example.untitledcasino.game.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.untitledcasino.formatWithCommas
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import untitledcasino.composeapp.generated.resources.Res
import untitledcasino.composeapp.generated.resources.coin_flip_title
import kotlin.math.abs

class DailyNumberVM(
    gameName: String,
    private val httpClient: HttpClient,
) : GameVM(gameName) {
    init {

    }

    var goalNumber by mutableStateOf<Int?>(null)
    var rolledNumber by mutableStateOf<Int?>(null)

    suspend fun fetchDailyGoal() {
        try {
            val response: String = httpClient.get("https://itchybarn.com/api/dailynumber").bodyAsText()
            goalNumber = response.trim().toInt()
        } catch (e: Exception) {
            uiMessage = "Failed to fetch daily goal"
        }
    }

    fun roll() {
        val roll = (0..9999).random()
        rolledNumber = roll

        val target = goalNumber!!

        val winnings = if (roll == target) {
            betAmount * 1000
        } else {
            val difference = abs(roll - target).toDouble()
            val closeness = 1.0 - (difference / 10000.0)

            (betAmount * closeness * 1.5).toInt()
        }

        grantWinnings(winnings)
        uiMessage = "You won ${formatWithCommas(winnings.toString())} credits!"
    }
}