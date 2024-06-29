package com.stauffer.pathtracker.data.skills

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SkillDao {
    @Query("SELECT * FROM skills")
    fun get(): Flow<List<SkillItem>>

    @Insert
    fun insert(skillItem: SkillItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(skillItem: SkillItem)
}