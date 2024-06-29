package com.stauffer.pathtracker.data.gear

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GearDao {
    @Query("SELECT * FROM gear")
    fun get(): Flow<List<GearItem>>

    @Insert
    fun insert(gearItem: GearItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(gearItem: GearItem)

    @Delete
    fun delete(gearItem: GearItem)
}