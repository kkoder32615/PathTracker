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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.stauffer.pathtracker.data.StatDao
import com.stauffer.pathtracker.data.StatDatabase
import com.stauffer.pathtracker.data.StatItem
import com.stauffer.pathtracker.ui.BtmAppBar
import com.stauffer.pathtracker.ui.theme.PathtrackerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.floor

class StatScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PathtrackerTheme {

                val statDao = Room.databaseBuilder(
                    applicationContext,
                    StatDatabase::class.java, "stats"
                ).build().statDao()
                val stats = statDao.get()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomAppBar { BtmAppBar(context = this@StatScreen) } }
                ) { innerPadding ->
                    StatScreenContent(
                        modifier = Modifier.padding(innerPadding),
                        stats = stats,
                        statDao = statDao
                    )
                }
            }
        }
    }
}

@Composable
fun StatScreenContent(modifier: Modifier, stats: StatItem, statDao: StatDao) {
    val scope = rememberCoroutineScope()
    val currentStats = remember {
        mutableStateOf(
            StatItem(
                name = stats.name,
                charClass = stats.charClass,
                level = stats.level,
                race = stats.race,
                deity = stats.deity,
                alignment = stats.alignment,
                str = stats.str,
                dex = stats.dex,
                con = stats.con,
                int = stats.int,
                wis = stats.wis,
                cha = stats.cha
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Info",
                fontSize = 36.sp
            )
        }
        InfoRow(
            label = "Name",
            value = currentStats.value.name,
            onValueChange = {
                currentStats.value = currentStats.value.copy(name = it)
                scope.launchWithDelay { statDao.update(currentStats.value) }
            }
        )
        InfoRow(
            label = "Class",
            value = currentStats.value.charClass,
            onValueChange = {
                currentStats.value = currentStats.value.copy(charClass = it)
                scope.launchWithDelay { statDao.update(currentStats.value) }
            }
        )
        InfoRow(
            label = "Level",
            value = currentStats.value.level.toString(),
            onValueChange = {
                currentStats.value = currentStats.value.copy(level = it.toIntOrNull() ?: 0)
                scope.launchWithDelay { statDao.update(currentStats.value) }
            }
        )
        InfoRow(
            label = "Race",
            value = currentStats.value.race,
            onValueChange = {
                currentStats.value = currentStats.value.copy(race = it)
                scope.launchWithDelay { statDao.update(currentStats.value) }
            }
        )
        InfoRow(
            label = "Deity",
            value = currentStats.value.deity,
            onValueChange = {
                currentStats.value = currentStats.value.copy(deity = it)
                scope.launchWithDelay { statDao.update(currentStats.value) }
            }
        )
        InfoRow(
            label = "Alignment",
            value = currentStats.value.alignment,
            onValueChange = {
                currentStats.value = currentStats.value.copy(alignment = it)
                scope.launchWithDelay { statDao.update(currentStats.value) }
            }
        )
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Stats",
                fontSize = 36.sp
            )
        }
        StatRow(
            label = "Strength",
            value = currentStats.value.str.toString(),
            onValueChange = {
                currentStats.value = currentStats.value.copy(str = it.toIntOrNull() ?: 0)
                scope.launchWithDelay { statDao.update(currentStats.value) }
            }
        )
        StatRow(
            label = "Dexterity",
            value = currentStats.value.dex.toString(),
            onValueChange = {
                currentStats.value = currentStats.value.copy(dex = it.toIntOrNull() ?: 0)
                scope.launchWithDelay { statDao.update(currentStats.value) }
            }
        )
        StatRow(
            label = "Constitution",
            value = currentStats.value.con.toString(),
            onValueChange = {
                currentStats.value = currentStats.value.copy(con = it.toIntOrNull() ?: 0)
                scope.launchWithDelay { statDao.update(currentStats.value) }
            }
        )
        StatRow(
            label = "Intelligence",
            value = currentStats.value.int.toString(),
            onValueChange = {
                currentStats.value = currentStats.value.copy(int = it.toIntOrNull() ?: 0)
                scope.launchWithDelay { statDao.update(currentStats.value) }
            }
        )
        StatRow(
            label = "Wisdom",
            value = currentStats.value.wis.toString(),
            onValueChange = {
                currentStats.value = currentStats.value.copy(wis = it.toIntOrNull() ?: 0)
                scope.launchWithDelay { statDao.update(currentStats.value) }
            }
        )
        StatRow(
            label = "Charisma",
            value = currentStats.value.cha.toString(),
            onValueChange = {
                currentStats.value = currentStats.value.copy(cha = it.toIntOrNull() ?: 0)
                scope.launchWithDelay { statDao.update(currentStats.value) }
            }
        )
    }
}

@Composable
fun InfoRow(
    label: String, value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(start = 16.dp)
    ) {
        Text(
            text = label,
            fontSize = 24.sp,
            maxLines = 1,
            modifier = Modifier.weight(0.5f)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            singleLine = true,
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f)
        )
    }
}

@Composable
private fun StatRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 16.dp)
    ) {
        Text(
            text = label,
            fontSize = 24.sp,
            maxLines = 1,
            modifier = Modifier.weight(0.6f)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier
                .weight(0.75f)
                .padding(start = 58.dp, end = 16.dp)
        )
        Text(
            text = calculateStatModifier(value.toIntOrNull() ?: 0),
            fontSize = 20.sp,
            maxLines = 1,
            modifier = Modifier.weight(0.5f)
        )
    }
}

private fun calculateStatModifier(score: Int) = "Mod: ${floor((score - 10) / 2.0).toInt()}"

/**
 * Launches a new coroutine and repeats `action` every `delayMillis` milliseconds
 */
private fun CoroutineScope.launchWithDelay(
    delayTime: Long = 2000,
    action: suspend () -> Unit
): Job {
    var delayJob: Job? = null
    return launch {
        delayJob?.cancel()
        delayJob = launch {
            delay(delayTime)
            action()
        }
    }
}