package com.example.untitledcasino.game.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitledcasino.PlayerRepo
import kotlinx.coroutines.launch

abstract class GameVM : ViewModel() {
    protected var playerRepo: PlayerRepo? = null

    // State for the UI to observe
    var betAmount by mutableStateOf(0)
    var isGameInProgress by mutableStateOf(false)
        protected set

    var uiMessage by mutableStateOf("")

    open fun setup(repo: PlayerRepo) {
        this.playerRepo = repo
    }

    protected fun attemptStartGame(onSuccess: () -> Unit) {
        val amount = betAmount
        isGameInProgress = true
        onSuccess()

        /*
        if (amount <= 0) {
            uiMessage = "Please enter a valid bet."
            return
        }

        viewModelScope.launch {
            val success = playerRepo?.tryCharge(amount) ?: false

            if (success) {
                uiMessage = ""
                isGameInProgress = true
                onSuccess()
            } else {
                uiMessage = "Insufficient credits!"
            }
        }

         */
    }

    protected fun grantWinnings(amount: Int) {
        viewModelScope.launch {
            playerRepo?.addCredits(amount)
            isGameInProgress = false
        }
    }

    protected fun endRound() {
        isGameInProgress = false
    }
}