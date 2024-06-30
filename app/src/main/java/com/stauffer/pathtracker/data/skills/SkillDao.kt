package com.stauffer.pathtracker.data.skills

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SkillDao {
    @Query("SELECT * FROM skills")
    suspend fun get(): SkillItem

    @Query("SELECT COUNT(*) FROM skills")
    suspend fun getCount(): Int

    @Insert
    suspend fun insert(skillItem: SkillItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(skillItem: SkillItem)
}