package com.example.untitledcasino.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlayerEntity (
    @PrimaryKey(autoGenerate = true)
    val credits: Int = 0
)