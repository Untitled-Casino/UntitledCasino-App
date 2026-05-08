package com.moravian.untitledcasino

import com.moravian.untitledcasino.data.GameplayEntity
import com.moravian.untitledcasino.data.PlayerDao
import com.moravian.untitledcasino.data.PlayerEntity
import com.moravian.untitledcasino.data.PurchaseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

const val INIT_CREDITS = 1000

class PlayerRepo(
    private val playerDao: PlayerDao,
) {
    val credits: Flow<Int?> = playerDao.getPlayerCredits()

    val purchaseHistory: Flow<List<PurchaseEntity>> = playerDao.getPurchaseHistory()
    val gameplayHistory: Flow<List<GameplayEntity>> = playerDao.getGameplayHistory()

    suspend fun initializePlayerIfNeeded() {
        withContext(Dispatchers.IO) {
            val currentCredits = playerDao.getPlayerCredits().firstOrNull()

            if (currentCredits == null) {
                val rowsInserted = playerDao.insertInitialPlayer(
                    PlayerEntity(id = 1, credits = INIT_CREDITS),
                )
                println("First run: Created player with ID: $rowsInserted")
            } else {
                println("Player already exists with $currentCredits credits")
            }
        }
    }

    suspend fun setCredits(newCredits: Int) {
        playerDao.setPlayerCredits(newCredits)
    }

    suspend fun addCredits(newCredits: Int) {
        val currentBalance = credits.first() ?: 0
        val newBalance = currentBalance + newCredits
        playerDao.setPlayerCredits(newBalance)
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
        withContext(Dispatchers.IO) {
            addCredits(option.creditsReceive)

            playerDao.insertPurchase(
                PurchaseEntity(
                    credits = option.creditsReceive,
                    priceInCents = option.priceInCents,
                    timestamp = currentTimeMillis(),
                ),
            )
        }
    }

    suspend fun recordGameplay(gameName: String, bet: Int, reward: Int) {
        withContext(Dispatchers.IO) {
            addCredits(reward)

            playerDao.insertGameplay(
                GameplayEntity(
                    gameName = gameName,
                    bet = bet,
                    reward = reward,
                    timestamp = currentTimeMillis(),
                ),
            )
        }
    }
}
