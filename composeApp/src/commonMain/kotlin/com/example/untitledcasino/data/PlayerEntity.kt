package com.example.untitledcasino.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlayerEntity (
    @PrimaryKey val id: Int = 1,
    val credits: Int = 0
)