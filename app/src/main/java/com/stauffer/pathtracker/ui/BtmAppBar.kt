package com.stauffer.pathtracker.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.stauffer.pathtracker.GearScreen
import com.stauffer.pathtracker.SkillScreen
import com.stauffer.pathtracker.StatScreen
import com.stauffer.pathtracker.WeaponScreen

@Composable
fun BtmAppBar(context: Context) {
    BottomAppBar(
        actions = {
            val buttonLabels = listOf(
                "Info and Stats",
                "Gear",
                "Weapons",
                "Skills",
                "Spells",
                "Feats",
                "Abilities",
                "Inventory",
                "Dice Roller"
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(buttonLabels) {
                    ElevatedButton(onClick = {
                        when (it) {
                            "Info and Stats" -> {
                                if (context is StatScreen) {
                                    // DO NOTHING, YOU ARE ALREADY ON THIS SCREEN
                                } else {
                                    context.startActivity(Intent(context, StatScreen::class.java))
                                }
                            }

                            "Gear" -> {
                                if (context is GearScreen) {
                                    // DO NOTHING, YOU ARE ALREADY ON THIS SCREEN
                                } else {
                                    context.startActivity(Intent(context, GearScreen::class.java))
                                }
                            }

                            "Weapons" -> {
                                if (context is WeaponScreen) {
                                    // DO NOTHING, YOU ARE ALREADY ON THIS SCREEN
                                } else {
                                    context.startActivity(Intent(context, WeaponScreen::class.java))
                                }
                            }

                            "Skills" -> {
                                if (context is SkillScreen) {
                                    // DO NOTHING, YOU ARE ALREADY ON THIS SCREEN
                                } else {
                                    context.startActivity(Intent(context, SkillScreen::class.java))
                                }
                            }
                        }
                    }) {
                        Text(text = it)
                    }
                }
            }
        }
    )
}