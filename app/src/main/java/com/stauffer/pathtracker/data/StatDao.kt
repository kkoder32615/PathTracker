package com.stauffer.pathtracker.data

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface StatDao {
    @Query("SELECT * FROM stats")
    fun get(): StatItem

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(statItem: StatItem)
}