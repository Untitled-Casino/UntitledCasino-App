package com.example.untitledcasino

import com.example.untitledcasino.data.PlayerDao
import com.example.untitledcasino.data.PlayerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

const val INIT_CREDITS = 100

class PlayerRepo(private val playerDao: PlayerDao) {
    val credits: Flow<Int?> = playerDao.getPlayerCredits()

    suspend fun initializePlayerIfNeeded() {
        if (credits.first() == null) {
            playerDao.insertInitialPlayer(PlayerEntity(id = 1, credits = INIT_CREDITS))
        }
    }

    suspend fun setCredits(newCredits: Int) {
        playerDao.setPlayerCredits(newCredits)
    }

    suspend fun addCredits(newCredits: Int) {
        playerDao.setPlayerCredits((credits.first() ?: 0) + newCredits)
    }

    suspend fun tryCharge(cost: Int): Boolean {
        val current = credits.first() ?: 0

        if (current >= cost) {
            setCredits(current - cost)
            return true
        }
        return false
    }
}