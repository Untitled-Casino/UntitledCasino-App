package com.example.untitledcasino

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untitledcasino.data.PlayerDao
import com.example.untitledcasino.data.PlayerEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CasinoVM(private val playerDao: PlayerDao): ViewModel() {
    init {
        viewModelScope.launch {
            playerDao.insertInitialPlayer(
                PlayerEntity(
                    id = 1,
                    credits = 100
                )
            )
        }
    }

    val playerCredits: StateFlow<Int> = playerDao.getPlayerCredits()
        .filterNotNull()
        .stateIn( // only one active connection to database
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // stop listening if not used for 5 seconds
            initialValue = 0
        )

    fun chargePlayer(cost: Int, onSuccess: () -> Unit, onFail: () -> Unit) {
        viewModelScope.launch {
            val current = playerCredits.value

            if (current >= cost) {
                playerDao.setPlayerCredits(playerCredits.value + cost)
                onSuccess()
            } else {
                onFail()
            }
        }
    }
}