package com.example.untitledcasino.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInitialPlayer(player: PlayerEntity): Long

    @Query("SELECT credits FROM PlayerEntity WHERE id = 1")
    fun getPlayerCredits(): Flow<Int>

    @Query("UPDATE PlayerEntity SET credits = :credits WHERE id = 1")
    suspend fun setPlayerCredits(credits: Int)

    @Insert
    suspend fun insertPurchase(purchase: PurchaseEntity): Long

    @Query("SELECT * FROM PurchaseEntity WHERE playerId = 1 ORDER BY timestamp DESC")
    fun getPurchaseHistory(): Flow<List<PurchaseEntity>>

    @Insert
    suspend fun insertGameplay(gameplay: GameplayEntity): Long

    @Query("SELECT * FROM GameplayEntity WHERE playerId = 1 ORDER BY timestamp DESC")
    fun getGameplayHistory(): Flow<List<GameplayEntity>>
}