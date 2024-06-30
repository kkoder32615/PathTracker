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
import com.stauffer.pathtracker.data.skills.SkillDao
import com.stauffer.pathtracker.data.skills.SkillDatabase
import com.stauffer.pathtracker.data.skills.SkillItem
import com.stauffer.pathtracker.ui.theme.PathtrackerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SkillScreenContent(
                        modifier = Modifier.padding(innerPadding),
                        skillDao
                    )
                }
            }
        }
    }
}

@Composable
private fun SkillScreenContent(
    modifier: Modifier = Modifier,
    skillDao: SkillDao
) {
    val scope = rememberCoroutineScope()
    val currentSkills by produceState<SkillItem?>(initialValue = null) {
        withContext(Dispatchers.IO) {
            if (skillDao.getCount() == 0) skillDao.insert(skillItem = SkillItem())
            value = skillDao.get()
        }
    }

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

        }
    }
}

@Composable
private fun SkillRow() {

}