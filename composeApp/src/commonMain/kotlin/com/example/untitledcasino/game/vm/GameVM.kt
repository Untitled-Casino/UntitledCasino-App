package com.example.untitledcasino.game.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitledcasino.PlayerRepo
import com.example.untitledcasino.currentTimeMillis
import com.example.untitledcasino.data.PurchaseEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class GameVM(
    gameName: String
) : ViewModel() {
    protected var playerRepo: PlayerRepo? = null

    // State for the UI to observe
    var betAmount by mutableStateOf(0)
    var isBusy by mutableStateOf(false)
        protected set

    private var _activeGame = MutableStateFlow("")
    val activeGame: StateFlow<String> = _activeGame

    var uiMessage by mutableStateOf("")

    init {
        _activeGame.value = gameName
    }

    open fun setup(repo: PlayerRepo) {
        this.playerRepo = repo
    }

    protected fun attemptStartGame(onSuccess: () -> Unit) {
        if (betAmount <= 0) {
            uiMessage = "Please enter a valid bet."
            return
        }

        viewModelScope.launch {
            val success = playerRepo?.tryCharge(betAmount) ?: false

            if (success) {
                uiMessage = ""
                isBusy = true
                onSuccess()
            } else {
                uiMessage = "Insufficient credits!"
                isBusy = false
            }
        }
    }

    protected fun grantWinnings(amount: Int) {
        viewModelScope.launch {
            playerRepo?.recordGameplay(
                gameName = activeGame.value,
                bet = betAmount,
                reward = amount,
            )
            isBusy = false
        }
    }

    protected fun finishProcessing() {
        isBusy = false
    }
}