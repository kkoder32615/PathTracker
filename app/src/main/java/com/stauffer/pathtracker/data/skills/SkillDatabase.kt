package com.stauffer.pathtracker.data.skills

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SkillItem::class], version = 1)
abstract class SkillDatabase : RoomDatabase() {
    abstract fun skillDao(): SkillDao
}