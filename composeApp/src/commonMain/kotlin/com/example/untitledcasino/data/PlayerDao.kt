package com.example.untitledcasino.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT credits FROM PlayerEntity")
    fun getPlayerCredits(): Flow<Int>

    @Query("UPDATE PlayerEntity SET credits = :credits")
    suspend fun setPlayerCredits(credits: Int)
}