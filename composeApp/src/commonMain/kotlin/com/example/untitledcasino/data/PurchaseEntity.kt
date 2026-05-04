package com.example.untitledcasino.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PurchaseEntity (
    @PrimaryKey(autoGenerate = true)
    val purchaseId: Int = 0,
    val playerId: Int = 1,
    val credits: Int,
    val priceInCents: Int,
    val timestamp: Long,
)