package com.stauffer.pathtracker.data.gear

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gear")
data class GearItem(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "name", defaultValue = "") var name: String?,
    @ColumnInfo(name = "bonus", defaultValue = "") var bonus: String?,
    @ColumnInfo(name = "type", defaultValue = "") var type: String?,
    @ColumnInfo(name = "checkPenalty", defaultValue = "") var checkPenalty: String?,
    @ColumnInfo(name = "spellFailure", defaultValue = "") var spellFailure: String?,
    @ColumnInfo(name = "weight", defaultValue = "") var weight: String?,
)