package com.example.untitledcasino.game.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.untitledcasino.formatWithCommas
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.getString
import untitledcasino.composeapp.generated.resources.*
import kotlin.math.abs

@Serializable
data class DailyNumberResponse(
    val number: Int,
)

class DailyNumberVM : GameVM() {
    private var httpClient: HttpClient? = null

    fun initClient(client: HttpClient) {
        this.httpClient = client
        fetchDailyGoal()
    }

    var goalNumber by mutableStateOf<Int?>(null)
    var rolledNumber by mutableStateOf<Int?>(null)

    fun fetchDailyGoal() {
        viewModelScope.launch {
            try {
                val response: DailyNumberResponse = httpClient!!.get("https://itchybarn.com/api/dailynumber").body()
                goalNumber = response.number
            } catch (e: Exception) {
                uiMessage = getString(Res.string.failed_daily_goal)
            }
        }
    }

    fun roll() {
        val target = goalNumber ?: return

        attemptStartGame {
            val roll = (0..9999).random()
            rolledNumber = roll

            val winnings = if (roll == target) {
                betAmount * 1000
            } else {
                val difference = abs(roll - target).toDouble()
                val closeness = 1.0 - (difference / 10000.0)

                (betAmount * closeness).toInt()
            }

            grantWinnings(winnings)
            viewModelScope.launch {
                uiMessage = getString(
                    Res.string.you_won_credits,
                    formatWithCommas(number = winnings.toString()),
                )
            }
        }
    }
}
