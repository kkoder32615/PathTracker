package com.stauffer.pathtracker.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StatItem::class], version = 1)
abstract class StatDatabase: RoomDatabase() {
    abstract fun statDao(): StatDao
}