package com.example.untitledcasino.game.vm

import kotlin.math.ceil
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class HiLoVM : GameVM() {
    var currentCard by mutableStateOf(7)
    var potentialWinnings by mutableStateOf(0)
    var isStreakActive by mutableStateOf(false)
    var round by mutableStateOf(0)
    val maxRound = 4

    fun startHiLo() {
        attemptStartGame {
            isStreakActive = true
            potentialWinnings = betAmount.toInt()
        }
    }

    fun makeGuess(higher: Boolean) {
        val nextCard = (1..13).random()
        val won = if (higher) nextCard >= currentCard else nextCard <= currentCard

        currentCard = nextCard

        if (won) {
            potentialWinnings = ceil(potentialWinnings * 1.2).toInt()

            if (round >= maxRound) {
                cashOut()
            } else {
                round++
            }
        } else {
            isStreakActive = false
            round = 0
            endRound()
        }
    }

    fun cashOut() {
        grantWinnings(potentialWinnings)
        isStreakActive = false
    }
}