package com.stauffer.pathtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.stauffer.pathtracker.data.BaseStat
import com.stauffer.pathtracker.data.skills.SKILL_TO_BASE_STAT_MAP
import com.stauffer.pathtracker.data.skills.SkillDao
import com.stauffer.pathtracker.data.skills.SkillDatabase
import com.stauffer.pathtracker.data.skills.SkillItem
import com.stauffer.pathtracker.data.stats.StatDao
import com.stauffer.pathtracker.data.stats.StatDatabase
import com.stauffer.pathtracker.data.stats.StatItem
import com.stauffer.pathtracker.ui.theme.PathtrackerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.floor

class SkillScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PathtrackerTheme {
                val db = Room.databaseBuilder(
                    applicationContext,
                    SkillDatabase::class.java, "skills"
                ).build()
                val skillDao = remember { db.skillDao() }
                val statDb = Room.databaseBuilder(
                    applicationContext,
                    StatDatabase::class.java, "stats"
                ).build()
                val statDao = remember { statDb.statDao() }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SkillScreenContent(
                        modifier = Modifier.padding(innerPadding),
                        skillDao,
                        statDao
                    )
                }
            }
        }
    }
}

@Composable
private fun SkillScreenContent(
    modifier: Modifier = Modifier,
    skillDao: SkillDao,
    statDao: StatDao
) {
    val scope = rememberCoroutineScope()
    val currentSkills by produceState<SkillItem?>(initialValue = null) {
        withContext(Dispatchers.IO) {
            if (skillDao.getCount() == 0) skillDao.insert(skillItem = SkillItem())
            value = skillDao.get()
        }
    }
    val currentStats by produceState< StatItem?>(initialValue = null) {
        withContext(Dispatchers.IO) {
            if (statDao.getCount() == 0) statDao.insert(StatItem())
            value = statDao.get()
        }
    }
    val strength = currentStats?.str ?: 0
    val dexterity = currentStats?.dex ?: 0
    val constitution = currentStats?.con ?: 0
    val intelligence = currentStats?.int ?: 0
    val wisdom = currentStats?.wis ?: 0
    val charisma = currentStats?.cha ?: 0

    // Title Row
    Row {
        Text(text = "Skill")
        Text(text = "Total")
        Text(text = "Modifier")
        Text(text = "Ranks")
        Text(text = "Misc")
    }
    currentSkills?.let { skills ->
        var updatedSkills by remember { mutableStateOf(skills.copy()) }
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SkillRow(
                name = "Acrobatics",
                ranks = updatedSkills.acrobatics,
                mod = returnStatModifier(SKILL_TO_BASE_STAT_MAP[skills.acrobatics], dexterity)
            )
            SkillRow(
                name = "Appraise",
                ranks = updatedSkills.appraise,
                mod = returnStatModifier(SKILL_TO_BASE_STAT_MAP[skills.appraise], intelligence)
            )
            SkillRow(
                name = "Bluff",
                ranks = updatedSkills.bluff,
                mod = returnStatModifier(SKILL_TO_BASE_STAT_MAP[skills.bluff], charisma)
            )
        }
    }
}

@Composable
private fun SkillRow(
    name: String,
    ranks: String,
    mod: String,
) {
    // If player has at least 1 rank in a skill, award +3 to the misc modifier
    var misc = 0
    if (ranks.toInt() > 0) misc += 3

    val total = ranks.toInt() + mod.toInt() + misc

    Row {
        Text(text = name)
        Text(text = total.toString())
        Text(text = "=")
        Text(text = mod)
        Text(text = ranks)
        Text(text = misc.toString())
    }
}

private fun returnStatModifier(baseStat: BaseStat?, currentStat: Int): String = when (baseStat) {
    BaseStat.STR -> calculateStatModifier(currentStat)
    BaseStat.DEX -> calculateStatModifier(currentStat)
    BaseStat.CON -> calculateStatModifier(currentStat)
    BaseStat.INT -> calculateStatModifier(currentStat)
    BaseStat.WIS -> calculateStatModifier(currentStat)
    BaseStat.CHA -> calculateStatModifier(currentStat)
    null -> calculateStatModifier(0)
}

private fun calculateStatModifier(score: Int) = "${floor((score - 10) / 2.0).toInt()}"