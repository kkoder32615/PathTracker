@file:OptIn(ExperimentalMaterial3Api::class)

package com.stauffer.pathtracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.stauffer.pathtracker.data.alignments
import com.stauffer.pathtracker.data.classes
import com.stauffer.pathtracker.data.stats.StatDao
import com.stauffer.pathtracker.data.stats.StatDatabase
import com.stauffer.pathtracker.data.stats.StatItem
import com.stauffer.pathtracker.ui.BtmAppBar
import com.stauffer.pathtracker.ui.theme.PathtrackerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.floor

class StatScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PathtrackerTheme {
                val db = Room.databaseBuilder(
                    applicationContext,
                    StatDatabase::class.java, "stats"
                ).build()
                val statDao = remember { db.statDao() }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomAppBar { BtmAppBar(context = this@StatScreen) } }
                ) {
                    StatScreenContent(
                        modifier = Modifier.padding(it),
                        statDao
                    )
                }
            }
        }
    }
}

@Composable
fun StatScreenContent(
    modifier: Modifier,
    statDao: StatDao
) {
    val scope = rememberCoroutineScope()
    val currentStats by produceState<StatItem?>(initialValue = null) {
        withContext(Dispatchers.IO) {
            if (statDao.getCount() == 0) statDao.insert(StatItem())
            value = statDao.get()
            Log.d("StatScreenContent", "Fetched stats: $value")
        }
    }

    currentStats?.let { stats ->
        var updatedStats by remember { mutableStateOf(stats.copy()) }
        val charClassState = remember { mutableStateOf(stats.charClass) }
        val alignmentState = remember { mutableStateOf(stats.alignment) }

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
                value = updatedStats.name,
                onValueChange = {
                    updatedStats = updatedStats.copy(name = it)
                    scope.launch {
                        statDao.update(updatedStats)
                    }
                },
            )

            var expandedClassPicker by remember { mutableStateOf(false) }
            var selectedClassItem by remember { mutableStateOf(classes[0]) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = "Class",
                    fontSize = 24.sp,
                    maxLines = 1,
                    modifier = Modifier.weight(0.5f)
                )
                ExposedDropdownMenuBox(
                    expanded = expandedClassPicker,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                    onExpandedChange = { expandedClassPicker = !expandedClassPicker }
                ) {
                    OutlinedTextField(
                        value = charClassState.value,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedClassPicker,
                        onDismissRequest = { expandedClassPicker = false }
                    ) {
                        classes.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    updatedStats = updatedStats.copy(charClass = it)
                                    selectedClassItem = it
                                    charClassState.value = it
                                    expandedClassPicker = false
                                    scope.launch {
                                        statDao.update(updatedStats)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            InfoRow(
                label = "Level",
                value = updatedStats.level.toString(),
                onValueChange = {
                    updatedStats = updatedStats.copy(level = it.toIntOrNull() ?: 0)
                    scope.launch {
                        statDao.update(updatedStats)
                    }
                },
            )
            InfoRow(
                label = "Race",
                value = updatedStats.race,
                onValueChange = {
                    updatedStats = updatedStats.copy(race = it)
                    scope.launch {
                        statDao.update(updatedStats)
                    }
                },
            )
            InfoRow(
                label = "Deity",
                value = updatedStats.deity,
                onValueChange = {
                    updatedStats = updatedStats.copy(deity = it)
                    scope.launch {
                        statDao.update(updatedStats)
                    }
                },
            )

            var expandedAlignmentPicker by remember { mutableStateOf(false) }
            var selectedAlignmentItem by remember { mutableStateOf(alignments[0]) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = "Alignment",
                    fontSize = 24.sp,
                    maxLines = 1,
                    modifier = Modifier.weight(0.5f)
                )
                ExposedDropdownMenuBox(
                    expanded = expandedAlignmentPicker,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                    onExpandedChange = { expandedAlignmentPicker = !expandedAlignmentPicker }
                ) {
                    OutlinedTextField(
                        value = alignmentState.value,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedAlignmentPicker,
                        onDismissRequest = { expandedAlignmentPicker = false }
                    ) {
                        alignments.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    updatedStats = updatedStats.copy(alignment = it)
                                    selectedAlignmentItem = it
                                    alignmentState.value = it
                                    expandedAlignmentPicker = false
                                    scope.launch {
                                        statDao.update(updatedStats)
                                    }
                                }
                            )
                        }
                    }
                }
            }

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
                value = updatedStats.str.toString(),
                onValueChange = {
                    updatedStats = updatedStats.copy(str = it.toIntOrNull() ?: 0)
                    scope.launch {
                        statDao.update(updatedStats)
                    }
                },
                modifier = Modifier
            )
            StatRow(
                label = "Dexterity",
                value = updatedStats.dex.toString(),
                onValueChange = {
                    updatedStats = updatedStats.copy(dex = it.toIntOrNull() ?: 0)
                    scope.launch {
                        statDao.update(updatedStats)
                    }
                },
                modifier = Modifier
            )
            StatRow(
                label = "Constitution",
                value = updatedStats.con.toString(),
                onValueChange = {
                    updatedStats = updatedStats.copy(con = it.toIntOrNull() ?: 0)
                    scope.launch {
                        statDao.update(updatedStats)
                    }
                },
                modifier = Modifier
            )
            StatRow(
                label = "Intelligence",
                value = updatedStats.int.toString(),
                onValueChange = {
                    updatedStats = updatedStats.copy(int = it.toIntOrNull() ?: 0)
                    scope.launch {
                        statDao.update(updatedStats)
                    }
                },
                modifier = Modifier
            )
            StatRow(
                label = "Wisdom",
                value = updatedStats.wis.toString(),
                onValueChange = {
                    updatedStats = updatedStats.copy(wis = it.toIntOrNull() ?: 0)
                    scope.launch {
                        statDao.update(updatedStats)
                    }
                },
                modifier = Modifier
            )
            StatRow(
                label = "Charisma",
                value = updatedStats.cha.toString(),
                onValueChange = {
                    updatedStats = updatedStats.copy(cha = it.toIntOrNull() ?: 0)
                    scope.launch {
                        statDao.update(updatedStats)
                    }
                },
                modifier = Modifier
            )
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(value) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(start = 16.dp)
    ) {
        Text(
            text = label,
            fontSize = 24.sp,
            maxLines = 1,
            modifier = modifier.weight(0.5f)
        )
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                onValueChange(it)
            },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            singleLine = true,
            modifier = modifier
                .padding(end = 16.dp)
                .weight(1f)
        )
    }
}

@Composable
private fun StatRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {
    var text by remember { mutableStateOf(value) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(start = 16.dp)
    ) {
        Text(
            text = label,
            fontSize = 24.sp,
            maxLines = 1,
            modifier = modifier.weight(0.6f)
        )
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                onValueChange(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = modifier
                .weight(0.75f)
                .padding(start = 58.dp, end = 16.dp)
        )
        Text(
            text = calculateStatModifier(text.toIntOrNull() ?: 0),
            fontSize = 20.sp,
            maxLines = 1,
            modifier = modifier.weight(0.5f)
        )
    }
}

private fun calculateStatModifier(score: Int) = "Mod: ${floor((score - 10) / 2.0).toInt()}"