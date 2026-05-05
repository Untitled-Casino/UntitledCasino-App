package com.example.untitledcasino.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameplayEntity (
    @PrimaryKey(autoGenerate = true)
    val gameplayId: Int = 0,
    val playerId: Int = 1,
    val gameName: String,
    val bet: Int = 0,
    val reward: Int = 0,
    val timestamp: Long = 0,
)