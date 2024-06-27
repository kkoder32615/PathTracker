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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
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
        }
    }

    currentStats?.let { stats ->
        val nameState = remember { mutableStateOf(stats.name) }
        val charClassState = remember { mutableStateOf(stats.charClass) }
        val levelState = remember { mutableStateOf(stats.level.toString()) }
        val raceState = remember { mutableStateOf(stats.race) }
        val deityState = remember { mutableStateOf(stats.deity) }
        val alignmentState = remember { mutableStateOf(stats.alignment) }
        val strState = remember { mutableStateOf(stats.str.toString()) }
        val dexState = remember { mutableStateOf(stats.dex.toString()) }
        val conState = remember { mutableStateOf(stats.con.toString()) }
        val intState = remember { mutableStateOf(stats.int.toString()) }
        val wisState = remember { mutableStateOf(stats.wis.toString()) }
        val chaState = remember { mutableStateOf(stats.cha.toString()) }

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
                value = nameState.value,
                onValueChange = { nameState.value = it },
                modifier = Modifier.onFocusChanged {
                    if (!it.isFocused) {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                statDao.update(stats.copy(name = nameState.value))
                            }
                        }
                    }
                }
            )
            InfoRow(
                label = "Class",
                value = charClassState.value,
                onValueChange = { charClassState.value = it },
                modifier = Modifier.onFocusChanged {
                    if (!it.isFocused) {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                statDao.update(stats.copy(charClass = charClassState.value))
                            }
                        }
                    }
                }
            )
            InfoRow(
                label = "Level",
                value = levelState.value,
                onValueChange = { levelState.value = it },
                modifier = Modifier.onFocusChanged {
                    if (!it.isFocused) {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                statDao.update(
                                    stats.copy(
                                        level = levelState.value.toIntOrNull() ?: 0
                                    )
                                )
                            }
                        }
                    }
                }
            )
            InfoRow(
                label = "Race",
                value = raceState.value,
                onValueChange = { raceState.value = it },
                modifier = Modifier.onFocusChanged {
                    if (!it.isFocused) {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                statDao.update(stats.copy(race = raceState.value))
                            }
                        }
                    }
                }
            )
            InfoRow(
                label = "Deity",
                value = deityState.value,
                onValueChange = { deityState.value = it },
                modifier = Modifier.onFocusChanged {
                    if (!it.isFocused) {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                statDao.update(stats.copy(deity = deityState.value))
                            }
                        }
                    }
                }
            )
            InfoRow(
                label = "Alignment",
                value = alignmentState.value,
                onValueChange = { alignmentState.value = it },
                modifier = Modifier.onFocusChanged {
                    if (!it.isFocused) {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                statDao.update(stats.copy(alignment = alignmentState.value))
                            }
                        }
                    }
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
                value = strState.value,
                onValueChange = { strState.value = it },
                modifier = Modifier.onFocusChanged {
                    if (!it.isFocused) {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                statDao.update(stats.copy(str = strState.value.toIntOrNull() ?: 0))
                            }
                        }
                    }
                }
            )
            StatRow(
                label = "Dexterity",
                value = dexState.value,
                onValueChange = { dexState.value = it },
                modifier = Modifier.onFocusChanged {
                    if (!it.isFocused) {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                statDao.update(stats.copy(dex = dexState.value.toIntOrNull() ?: 0))
                            }
                        }
                    }
                }
            )
            StatRow(
                label = "Constitution",
                value = conState.value,
                onValueChange = { conState.value = it },
                modifier = Modifier.onFocusChanged {
                    if (!it.isFocused) {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                statDao.update(stats.copy(con = conState.value.toIntOrNull() ?: 0))
                            }
                        }
                    }
                }
            )
            StatRow(
                label = "Intelligence",
                value = intState.value,
                onValueChange = { intState.value = it },
                modifier = Modifier.onFocusChanged {
                    if (!it.isFocused) {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                statDao.update(stats.copy(int = intState.value.toIntOrNull() ?: 0))
                            }
                        }
                    }
                }
            )
            StatRow(
                label = "Wisdom",
                value = wisState.value,
                onValueChange = { wisState.value = it },
                modifier = Modifier.onFocusChanged {
                    if (!it.isFocused) {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                statDao.update(stats.copy(wis = wisState.value.toIntOrNull() ?: 0))
                            }
                        }
                    }
                }
            )
            StatRow(
                label = "Charisma",
                value = chaState.value,
                onValueChange = { chaState.value = it },
                modifier = Modifier.onFocusChanged {
                    if (!it.isFocused) {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                statDao.update(stats.copy(cha = chaState.value.toIntOrNull() ?: 0))
                            }
                        }
                    }
                }
            )
        }
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
            modifier = modifier.weight(0.5f)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
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
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = modifier
                .weight(0.75f)
                .padding(start = 58.dp, end = 16.dp)
        )
        Text(
            text = calculateStatModifier(value.toIntOrNull() ?: 0),
            fontSize = 20.sp,
            maxLines = 1,
            modifier = modifier.weight(0.5f)
        )
    }
}

private fun calculateStatModifier(score: Int) = "Mod: ${floor((score - 10) / 2.0).toInt()}"