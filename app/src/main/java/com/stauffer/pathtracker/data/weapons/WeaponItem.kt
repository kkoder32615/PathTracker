package com.stauffer.pathtracker.data.weapons

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weapons")
data class WeaponItem(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "name", defaultValue = "") var name: String?,
    @ColumnInfo(name = "attackBonus", defaultValue = "") var attackBonus: String?,
    @ColumnInfo(name = "critical", defaultValue = "") var critical: String?,
    @ColumnInfo(name = "type", defaultValue = "") var type: String?,
    @ColumnInfo(name = "range", defaultValue = "") var range: String?,
    @ColumnInfo(name = "ammunition", defaultValue = "") var ammunition: String?,
    @ColumnInfo(name = "damage", defaultValue = "") var damage: String?,
)