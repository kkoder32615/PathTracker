package com.stauffer.pathtracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface StatDao {
    @Query("SELECT * FROM stats")
    fun get(): StatItem

    @Query("SELECT COUNT(*) FROM stats")
    fun getCount(): Int

    @Insert
    suspend fun insert(statItem: StatItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(statItem: StatItem)
}