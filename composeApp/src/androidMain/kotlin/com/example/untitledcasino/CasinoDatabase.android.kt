package com.example.untitledcasino

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.untitledcasino.data.CasinoDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<CasinoDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("survey.db")
    return Room.databaseBuilder<CasinoDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}
