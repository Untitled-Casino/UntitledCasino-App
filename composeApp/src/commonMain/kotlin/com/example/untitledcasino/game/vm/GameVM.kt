package com.example.untitledcasino.game.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitledcasino.PlayerRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import untitledcasino.composeapp.generated.resources.*

abstract class GameVM() : ViewModel() {
    protected var playerRepo: PlayerRepo? = null
    var betAmount by mutableStateOf(0)
    var isBusy by mutableStateOf(false)
        protected set

    private var _activeGame = MutableStateFlow("")
    val activeGame: StateFlow<String> = _activeGame

    var uiMessage by mutableStateOf("")

    open fun setup(repo: PlayerRepo, stringResource: String) {
        this.playerRepo = repo
        this._activeGame.value = stringResource
    }

    protected fun attemptStartGame(onSuccess: () -> Unit) {
        if (betAmount <= 0) {
            viewModelScope.launch {
                uiMessage = getString(Res.string.valid_bet)
            }
            return
        }

        viewModelScope.launch {
            val success = playerRepo?.tryCharge(betAmount) ?: false

            if (success) {
                uiMessage = ""
                isBusy = true
                onSuccess()
            } else {
                viewModelScope.launch {
                    uiMessage = getString(Res.string.insufficient_credits)
                }
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