package com.example.untitledcasino.game.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class HiLoVM : GameVM() {
    var currentCard by mutableStateOf<Int?>(null)
    var nextCard by mutableStateOf<Int?>(null)

    var roundWon by mutableStateOf(false)
    var potentialWinnings by mutableStateOf(0)
    var isStreakActive by mutableStateOf(false)
    var gameWon by mutableStateOf(false)

    var round by mutableStateOf(0)
    val maxRound = 4

    var isAnimationPlaying by mutableStateOf(false)

    fun startHiLo() {
        attemptStartGame {
            isStreakActive = true
            gameWon = false
            potentialWinnings = betAmount
            round = 0
            currentCard = (1..13).random()
            nextCard = null
            finishProcessing()
        }
    }

    fun makeGuess(higher: Boolean) {
        val current = currentCard ?: return
        if (isBusy || !isStreakActive) return

        isBusy = true

        val drawn = (1..13).random()
        roundWon = if (higher) drawn > current else drawn < current

        nextCard = drawn
    }

    fun completeTurn() {
        if (roundWon) {
            potentialWinnings = (potentialWinnings * 1.2).toInt()
            if (round >= maxRound) {
                cashOut()
            } else {
                round++
                currentCard = nextCard
                nextCard = null
                finishProcessing()
            }
        } else {
            gameWon = false
            isStreakActive = false
            currentCard = null
            nextCard = null
            grantWinnings(0)
            finishProcessing()
        }
    }

    fun cashOut() {
        gameWon = true
        isStreakActive = false
        grantWinnings(potentialWinnings)
    }
}
