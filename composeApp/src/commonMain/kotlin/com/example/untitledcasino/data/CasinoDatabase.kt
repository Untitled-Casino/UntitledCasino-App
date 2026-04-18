package com.example.untitledcasino.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [
        PlayerEntity::class,
    ],
    version = 1,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class CasinoDatabase : RoomDatabase() {
    abstract fun getDao(): PlayerDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<CasinoDatabase> {
    override fun initialize(): CasinoDatabase
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<CasinoDatabase>,
): CasinoDatabase = builder
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
    .fallbackToDestructiveMigration(true) // destroys old data upon a version migration
    .build()