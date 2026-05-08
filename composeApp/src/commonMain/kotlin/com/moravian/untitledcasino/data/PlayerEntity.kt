package com.moravian.untitledcasino.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlayerEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val credits: Int = 0,
)
