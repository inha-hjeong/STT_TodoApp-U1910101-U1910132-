package com.example.mp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun appDao(): AppDao
}