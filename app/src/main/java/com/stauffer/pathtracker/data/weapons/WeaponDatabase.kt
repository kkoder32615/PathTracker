package com.stauffer.pathtracker.data.weapons

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeaponItem::class], version = 1)
abstract class WeaponDatabase : RoomDatabase() {
    abstract fun weaponDao(): WeaponDao
}