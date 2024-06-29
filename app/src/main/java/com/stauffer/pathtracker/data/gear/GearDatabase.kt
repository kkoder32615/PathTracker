package com.stauffer.pathtracker.data.gear

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GearItem::class], version = 1)
abstract class GearDatabase : RoomDatabase() {
    abstract fun gearDao(): GearDao
}