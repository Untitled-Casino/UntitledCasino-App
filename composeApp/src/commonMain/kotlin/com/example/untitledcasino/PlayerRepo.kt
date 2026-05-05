package com.example.untitledcasino

import com.example.untitledcasino.data.GameplayEntity
import com.example.untitledcasino.data.PlayerDao
import com.example.untitledcasino.data.PlayerEntity
import com.example.untitledcasino.data.PurchaseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlin.time.Clock

const val INIT_CREDITS = 100

class PlayerRepo(private val playerDao: PlayerDao) {
    val credits: Flow<Int?> = playerDao.getPlayerCredits()

    val purchaseHistory: Flow<List<PurchaseEntity>> = playerDao.getPurchaseHistory()
    val gameplayHistory: Flow<List<GameplayEntity>> = playerDao.getGameplayHistory()

    suspend fun initializePlayerIfNeeded() {
        withContext(Dispatchers.IO) {
            val current = playerDao.getPlayerCredits().firstOrNull()
            if (current == null) {
                playerDao.insertInitialPlayer(PlayerEntity(id = 1, credits = 1000))
            }
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

    suspend fun recordPurchase(option: CreditPurchaseOption) {
        addCredits(option.creditsReceive)

        playerDao.insertPurchase(
            PurchaseEntity(
                credits = option.creditsReceive,
                priceInCents = option.priceInCents,
                timestamp = currentTimeMillis(),
            )
        )
    }

    suspend fun recordGameplay(gameName: String, bet: Int, reward: Int) {
        addCredits(reward)

        playerDao.insertGameplay(
            GameplayEntity(
                gameName = gameName,
                bet = bet,
                reward = reward,
                timestamp = currentTimeMillis(),
            )
        )
    }
}