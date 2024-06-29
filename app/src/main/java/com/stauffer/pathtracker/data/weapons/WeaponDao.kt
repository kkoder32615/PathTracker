package com.stauffer.pathtracker.data.weapons

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WeaponDao {
    @Query("SELECT * FROM weapons")
    fun get(): Flow<List<WeaponItem>>

    @Insert
    fun insert(vararg weaponItem: WeaponItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(weaponItem: WeaponItem)

    @Delete
    fun delete(weaponItem: WeaponItem)
}