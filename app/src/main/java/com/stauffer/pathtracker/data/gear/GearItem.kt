package com.stauffer.pathtracker.data.gear

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gear")
data class GearItem(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "gearName", defaultValue = "") var gearName: String?,
    @ColumnInfo(name = "gearBonus", defaultValue = "") var gearBonus: String?,
    @ColumnInfo(name = "gearType", defaultValue = "") var gearType: String?,
    @ColumnInfo(name = "gearCheckPenalty", defaultValue = "") var gearCheckPenalty: String?,
    @ColumnInfo(name = "gearSpellFailure", defaultValue = "") var gearSpellFailure: String?,
    @ColumnInfo(name = "gearWeight", defaultValue = "") var gearWeight: String?,
)