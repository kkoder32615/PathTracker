package com.stauffer.pathtracker.data.stats

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stats")
data class StatItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "name", defaultValue = "") var name: String = "",
    @ColumnInfo(name = "charClass", defaultValue = "") var charClass: String = "",
    @ColumnInfo(name = "level", defaultValue = "0") var level: Int = 0,
    @ColumnInfo(name = "race", defaultValue = "") var race: String = "",
    @ColumnInfo(name = "deity", defaultValue = "") var deity: String = "",
    @ColumnInfo(name = "alignment", defaultValue = "") var alignment: String = "",

    @ColumnInfo(name = "str", defaultValue = "0") var str: Int = 0,
    @ColumnInfo(name = "dex", defaultValue = "0") var dex: Int = 0,
    @ColumnInfo(name = "con", defaultValue = "0") var con: Int = 0,
    @ColumnInfo(name = "int", defaultValue = "0") var int: Int = 0,
    @ColumnInfo(name = "wis", defaultValue = "0") var wis: Int = 0,
    @ColumnInfo(name = "cha", defaultValue = "0") var cha: Int = 0,
)