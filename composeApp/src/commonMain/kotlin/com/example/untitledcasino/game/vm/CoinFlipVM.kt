package com.example.untitledcasino.game.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CoinFlipVM : GameVM() {
    var sideSelection by mutableStateOf("")
    var lastResult by mutableStateOf("None")
    var won by mutableStateOf<Boolean?>(null)

    fun select(userChoice: String) {
        sideSelection = userChoice
    }

    fun flip() {
        if (sideSelection.isEmpty()) return

        attemptStartGame {
            val result = if ((0..1).random() == 0) "H" else "T"
            lastResult = result
            won = (sideSelection == result)
        }
    }

    fun onAnimationFinished() {
        //had to do this because of null check :(
        if (won == true) {
            val bet = betAmount
            grantWinnings(bet * 2)
        } else {
            endRound()
        }
        sideSelection = ""
    }


}