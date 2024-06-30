package com.stauffer.pathtracker.data.skills

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stauffer.pathtracker.data.BaseStat

@Entity(tableName = "skills")
data class SkillItem(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "acrobatics", defaultValue = "") var acrobatics: String = "",
    @ColumnInfo(name = "appraise", defaultValue = "") var appraise: String = "",
    @ColumnInfo(name = "bluff", defaultValue = "") var bluff: String = "",
    @ColumnInfo(name = "climb", defaultValue = "") var climb: String = "",
    @ColumnInfo(name = "craft1", defaultValue = "") var craft1: String = "",
    @ColumnInfo(name = "craft2", defaultValue = "") var craft2: String = "",
    @ColumnInfo(name = "craft3", defaultValue = "") var craft3: String = "",
    @ColumnInfo(name = "diplomacy", defaultValue = "") var diplomacy: String = "",
    @ColumnInfo(name = "disableDevice", defaultValue = "") var disableDevice: String = "",
    @ColumnInfo(name = "disguise", defaultValue = "") var disguise: String = "",
    @ColumnInfo(name = "escapeArtist", defaultValue = "") var escapeArtist: String = "",
    @ColumnInfo(name = "fly", defaultValue = "") var fly: String = "",
    @ColumnInfo(name = "handleAnimal", defaultValue = "") var handleAnimal: String = "",
    @ColumnInfo(name = "heal", defaultValue = "") var heal: String = "",
    @ColumnInfo(name = "intimidate", defaultValue = "") var intimidate: String = "",
    @ColumnInfo(name = "knowledgeArcana", defaultValue = "") var knowledgeArcana: String = "",
    @ColumnInfo(name = "knowledgeDungeoneering", defaultValue = "") var knowledgeDungeoneering: String = "",
    @ColumnInfo(name = "knowledgeEngineering", defaultValue = "") var knowledgeEngineering: String = "",
    @ColumnInfo(name = "knowledgeGeography", defaultValue = "") var knowledgeGeography: String = "",
    @ColumnInfo(name = "knowledgeHistory", defaultValue = "") var knowledgeHistory: String = "",
    @ColumnInfo(name = "knowledgeLocal",defaultValue = "") var knowledgeLocal: String = "",
    @ColumnInfo(name = "knowledgeNature", defaultValue = "") var knowledgeNature: String = "",
    @ColumnInfo(name = "knowledgeNobility", defaultValue = "") var knowledgeNobility: String = "",
    @ColumnInfo(name = "knowledgePlanes", defaultValue = "") var knowledgePlanes: String = "",
    @ColumnInfo(name = "knowledgeReligion", defaultValue = "") var knowledgeReligion: String = "",
    @ColumnInfo(name = "linguistics", defaultValue = "") var linguistics: String = "",
    @ColumnInfo(name = "perception", defaultValue = "") var perception: String = "",
    @ColumnInfo(name = "perform1", defaultValue = "") var perform1: String = "",
    @ColumnInfo(name = "perform2", defaultValue = "") var perform2: String = "",
    @ColumnInfo(name = "profession1", defaultValue = "") var profession1: String = "",
    @ColumnInfo(name = "profession2", defaultValue = "") var profession2: String = "",
    @ColumnInfo(name = "ride", defaultValue = "") var ride: String = "",
    @ColumnInfo(name = "senseMotive", defaultValue = "") var senseMotive: String = "",
    @ColumnInfo(name = "sleightOfHand", defaultValue = "") var sleightOfHand: String = "",
    @ColumnInfo(name = "spellcraft", defaultValue = "") var spellcraft: String = "",
    @ColumnInfo(name = "stealth", defaultValue = "") var stealth: String = "",
    @ColumnInfo(name = "survival", defaultValue = "") var survival: String = "",
    @ColumnInfo(name = "swim", defaultValue = "") var swim: String = "",
    @ColumnInfo(name = "useMagicDevice", defaultValue = "") var useMagicDevice: String = "",
)

val SKILL_TO_BASE_STAT_MAP: Map<String, BaseStat> = mapOf(
    "acrobatics" to BaseStat.DEX,
    "appraise" to BaseStat.INT,
    "bluff" to BaseStat.CHA,
    "climb" to BaseStat.STR,
    "craft1" to BaseStat.INT,
    "craft2" to BaseStat.INT,
    "craft3" to BaseStat.INT,
    "diplomacy" to BaseStat.CHA,
    "disableDevice" to BaseStat.DEX,
    "disguise" to BaseStat.CHA,
    "escapeArtist" to BaseStat.DEX,
    "fly" to BaseStat.DEX,
    "handleAnimal" to BaseStat.CHA,
    "heal" to BaseStat.WIS,
    "intimidate" to BaseStat.CHA,
    "knowledgeArcana" to BaseStat.INT,
    "knowledgeDungeoneering" to BaseStat.INT,
    "knowledgeEngineering" to BaseStat.INT,
    "knowledgeGeography" to BaseStat.INT,
    "knowledgeHistory" to BaseStat.INT,
    "knowledgeLocal" to BaseStat.INT,
    "knowledgeNature" to BaseStat.INT,
    "knowledgeNobility" to BaseStat.INT,
    "knowledgePlanes" to BaseStat.INT,
    "knowledgeReligion" to BaseStat.INT,
    "linguistics" to BaseStat.INT,
    "perception" to BaseStat.WIS,
    "perform1" to BaseStat.CHA,
    "perform2" to BaseStat.CHA,
    "profession1" to BaseStat.WIS,
    "profession2" to BaseStat.WIS,
    "ride" to BaseStat.DEX,
    "senseMotive" to BaseStat.WIS,
    "sleightOfHand" to BaseStat.DEX,
    "spellcraft" to BaseStat.INT,
    "stealth" to BaseStat.DEX,
    "survival" to BaseStat.WIS,
    "swim" to BaseStat.STR,
    "useMagicDevice" to BaseStat.CHA
)

fun getSkillString(skill: String): String {
    return when (skill.lowercase()) {
        "acrobatics" -> "acrobatics"
        "appraise" -> "appraise"
        "bluff" -> "bluff"
        "climb" -> "climb"
        "craft1" -> "craft1"
        "craft2" -> "craft2"
        "craft3" -> "craft3"
        "diplomacy" -> "diplomacy"
        "disableDevice" -> "disableDevice"
        "disguise" -> "disguise"
        "escapeArtist" -> "escapeArtist"
        "fly" -> "fly"
        "handleAnimal" -> "handleAnimal"
        "heal" -> "heal"
        "intimidate" -> "intimidate"
        "knowledgeArcana" -> "knowledgeArcana"
        "knowledgeDungeoneering" -> "knowledgeDungeoneering"
        "knowledgeEngineering" -> "knowledgeEngineering"
        "knowledgeGeography" -> "knowledgeGeography"
        "knowledgeHistory" -> "knowledgeHistory"
        "knowledgeLocal" -> "knowledgeLocal"
        "knowledgeNature" -> "knowledgeNature"
        "knowledgeNobility" -> "knowledgeNobility"
        "knowledgePlanes" -> "knowledgePlanes"
        "knowledgeReligion" -> "knowledgeReligion"
        "linguistics" -> "linguistics"
        "perception" -> "perception"
        "perform1" -> "perform1"
        "perform2" -> "perform2"
        "profession1" -> "profession1"
        "profession2" -> "profession2"
        "ride" -> "ride"
        "senseMotive" -> "senseMotive"
        "sleightOfHand" -> "sleightOfHand"
        "spellcraft" -> "spellcraft"
        "stealth" -> "stealth"
        "survival" -> "survival"
        "swim" -> "swim"
        "useMagicDevice" -> "useMagicDevice"
        else -> ""
    }
}