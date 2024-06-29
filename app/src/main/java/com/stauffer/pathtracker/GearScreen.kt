package com.stauffer.pathtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.stauffer.pathtracker.data.DialogType
import com.stauffer.pathtracker.data.gear.GearDao
import com.stauffer.pathtracker.data.gear.GearDatabase
import com.stauffer.pathtracker.data.gear.GearItem
import com.stauffer.pathtracker.ui.BtmAppBar
import com.stauffer.pathtracker.ui.theme.PathtrackerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GearScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PathtrackerTheme {
                val db = Room.databaseBuilder(
                    applicationContext,
                    GearDatabase::class.java, "gear"
                ).build()
                val gearDao = remember { db.gearDao() }
                val scope = rememberCoroutineScope()

                var refreshKey by remember { mutableIntStateOf(0) }
                var showDeleteSingleDialog by remember { mutableStateOf(false) }
                var showDeleteAllDialog by remember { mutableStateOf(false) }
                var showEditDialog by remember { mutableStateOf(false) }
                var gearItemToEdit by remember { mutableStateOf<GearItem?>(null) }

                var gearId by remember { mutableIntStateOf(0) }
                var gearName by remember { mutableStateOf("") }
                var gearBonus by remember { mutableStateOf("") }
                var gearType by remember { mutableStateOf("") }
                var gearCheckPenalty by remember { mutableStateOf("") }
                var gearSpellFailure by remember { mutableStateOf("") }
                var gearWeight by remember { mutableStateOf("") }

                GearDialogs(
                    showDeleteSingleDialog,
                    showDeleteAllDialog,
                    showEditDialog,
                    gearId,
                    gearName,
                    gearBonus,
                    gearType,
                    gearCheckPenalty,
                    gearSpellFailure,
                    gearWeight,
                    gearDao,
                    onUpdateShowDeleteSingle = { showDeleteSingleDialog = it },
                    onUpdateShowDeleteAll = { showDeleteAllDialog = it },
                    onUpdateShowEdit = { showEditDialog = it },
                    onUpdateGearItemToEdit = { gearItemToEdit = it },
                    onConfirm = {
                        when (it) {
                            DialogType.DELETE_SINGLE -> {
                                gearItemToEdit?.let {
                                    scope.launch {
                                        gearDao.delete(
                                            it
                                        )
                                    }
                                }
                                gearItemToEdit = null
                            }

                            DialogType.DELETE_ALL -> {
                                scope.launch(Dispatchers.IO) { db.clearAllTables() }
                            }

                            DialogType.UPDATE -> {
                                gearItemToEdit?.let {
                                    scope.launch {
                                        gearDao.update(
                                            it
                                        )
                                    }
                                }
                                gearItemToEdit = null
                            }
                        }
                        refreshKey++
                    }
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomAppBar { BtmAppBar(this@GearScreen) } },
                    floatingActionButton = {
                        GFab(
                            gearName,
                            gearBonus,
                            gearType,
                            gearCheckPenalty,
                            gearSpellFailure,
                            gearWeight,
                            gearDao,
                        ) { refreshKey++ }
                    }
                ) {
                    GScreen(
                        modifier = Modifier.padding(it),
                        gearDao,
                        refreshKey,
                        onDeleteSingle = { gearItem ->
                            gearItemToEdit = gearItem
                            showDeleteSingleDialog = true
                        },
                        onDeleteAll = { showDeleteAllDialog = true },
                        onEdit = { gearItem ->
                            gearItemToEdit = gearItem
                            gearId = gearItem.uid
                            gearName = gearItem.gearName.toString()
                            gearBonus = gearItem.gearBonus.toString()
                            gearType = gearItem.gearType.toString()
                            gearCheckPenalty = gearItem.gearCheckPenalty.toString()
                            gearSpellFailure = gearItem.gearSpellFailure.toString()
                            gearWeight = gearItem.gearWeight.toString()
                            showEditDialog = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun GScreen(
    modifier: Modifier,
    gearDao: GearDao,
    refreshKey: Int,
    onDeleteSingle: (GearItem) -> Unit,
    onDeleteAll: () -> Unit,
    onEdit: (GearItem) -> Unit,
) {
    val gearListState = remember { mutableStateOf<List<GearItem>>(emptyList()) }
    val gearList by gearListState

    LaunchedEffect(key1 = refreshKey) {
        gearDao.get().collect { gearListState.value = it }
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(gearList) {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${it.gearName}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Bonus: ${it.gearBonus}")
                            Text("Type: ${it.gearType}")
                            Text("Check Penalty: ${it.gearCheckPenalty}")
                        }
                        Column {
                            Text("Spell Failure: ${it.gearSpellFailure}%")
                            Text("Weight: ${it.gearWeight} lbs")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { onEdit(it) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Edit")
                        }
                        Spacer(modifier = Modifier.size(64.dp))
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { onDeleteSingle(it) },
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
        item {
            Button(onClick = onDeleteAll) {
                Text("Delete All")
            }
        }
    }
}

@Composable
private fun GearDialogs(
    showDeleteSingleDialog: Boolean,
    showDeleteAllDialog: Boolean,
    showEditDialog: Boolean,
    gearId: Int,
    gearName: String,
    gearBonus: String,
    gearType: String,
    gearCheckPenalty: String,
    gearSpellFailure: String,
    gearWeight: String,
    gearDao: GearDao,
    onUpdateShowDeleteSingle: (Boolean) -> Unit,
    onUpdateShowDeleteAll: (Boolean) -> Unit,
    onUpdateShowEdit: (Boolean) -> Unit,
    onUpdateGearItemToEdit: (GearItem?) -> Unit,
    onConfirm: (DialogType) -> Unit
) {
    val scope = rememberCoroutineScope()
    if (showDeleteSingleDialog) {
        AlertDialog(
            title = { Text("Are you sure?") },
            onDismissRequest = { onUpdateShowDeleteSingle(false) },
            confirmButton = {
                Button(onClick = {
                    onConfirm(DialogType.DELETE_SINGLE)
                    onUpdateShowDeleteSingle(false)
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { onUpdateShowDeleteSingle(false) }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDeleteAllDialog) {
        AlertDialog(
            title = { Text("Are you sure you want to delete all?") },
            onDismissRequest = { onUpdateShowDeleteAll(false) },
            confirmButton = {
                Button(onClick = {
                    onConfirm(DialogType.DELETE_ALL)
                    onUpdateShowDeleteAll(false)
                }) {
                    Text("Delete All")
                }
            },
            dismissButton = {
                Button(onClick = { onUpdateShowDeleteAll(false) }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showEditDialog) {
        var updatedGearItem by remember {
            mutableStateOf(
                GearItem(
                    gearId,
                    gearName,
                    gearBonus,
                    gearType,
                    gearCheckPenalty,
                    gearSpellFailure,
                    gearWeight
                )
            )
        }
        AlertDialog(
            title = { Text("Edit") },
            onDismissRequest = { onUpdateShowEdit(false) },
            text = {
                Column {
                    OutlinedTextField(
                        value = updatedGearItem.gearName ?: "",
                        onValueChange = { updatedGearItem = updatedGearItem.copy(gearName = it) },
                        label = { Text("Item Name") }
                    )
                    OutlinedTextField(
                        value = updatedGearItem.gearBonus ?: "",
                        onValueChange = { updatedGearItem = updatedGearItem.copy(gearBonus = it) },
                        label = { Text("AC Bonus") }
                    )
                    OutlinedTextField(
                        value = updatedGearItem.gearType ?: "",
                        onValueChange = { updatedGearItem = updatedGearItem.copy(gearType = it) },
                        label = { Text("Type") }
                    )
                    OutlinedTextField(
                        value = updatedGearItem.gearCheckPenalty ?: "",
                        onValueChange = {
                            updatedGearItem = updatedGearItem.copy(gearCheckPenalty = it)
                        },
                        label = { Text("Check Penalty") }
                    )
                    OutlinedTextField(
                        value = updatedGearItem.gearSpellFailure ?: "",
                        onValueChange = {
                            updatedGearItem = updatedGearItem.copy(gearSpellFailure = it)
                        },
                        label = { Text("Spell Failure %") }
                    )
                    OutlinedTextField(
                        value = updatedGearItem.gearWeight ?: "",
                        onValueChange = { updatedGearItem = updatedGearItem.copy(gearWeight = it) },
                        label = { Text("Weight") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    updatedGearItem.let {
                        scope.launch(Dispatchers.IO) {
                            gearDao.update(it)
                            onUpdateGearItemToEdit(null)
                        }
                    }
                    onUpdateShowEdit(false)
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { onUpdateShowEdit(false) }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun GFab(
    gearName: String,
    gearBonus: String,
    gearType: String,
    gearCheckPenalty: String,
    gearSpellFailure: String,
    gearWeight: String,
    gearDao: GearDao,
    triggerUpdate: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var localGearName by remember { mutableStateOf(gearName) }
    var localGearBonus by remember { mutableStateOf(gearBonus) }
    var localGearType by remember { mutableStateOf(gearType) }
    var localGearCheckPenalty by remember { mutableStateOf(gearCheckPenalty) }
    var localGearSpellFailure by remember { mutableStateOf(gearSpellFailure) }
    var localGearWeight by remember { mutableStateOf(gearWeight) }
    val scope = rememberCoroutineScope()

    val newItem = GearItem(
        0,
        localGearName,
        localGearBonus,
        localGearType,
        localGearCheckPenalty,
        localGearSpellFailure,
        localGearWeight
    )


    FloatingActionButton({ showDialog = true }) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "Add"
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "New Item") },
            text = {
                Column {
                    OutlinedTextField(
                        value = localGearName,
                        onValueChange = { localGearName = it },
                        label = { Text("Item Name") }
                    )
                    OutlinedTextField(
                        value = localGearBonus,
                        onValueChange = { localGearBonus = it },
                        label = { Text("AC Bonus") }
                    )
                    OutlinedTextField(
                        value = localGearType,
                        onValueChange = { localGearType = it },
                        label = { Text("Type") }
                    )
                    OutlinedTextField(
                        value = localGearCheckPenalty,
                        onValueChange = { localGearCheckPenalty = it },
                        label = { Text("Check Penalty") }
                    )
                    OutlinedTextField(
                        value = localGearSpellFailure,
                        onValueChange = { localGearSpellFailure = it },
                        label = { Text("Spell Failure %") }
                    )
                    OutlinedTextField(
                        value = localGearWeight,
                        onValueChange = { localGearWeight = it },
                        label = { Text("Weight") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    scope.launch(Dispatchers.IO) { gearDao.insert(newItem) }
                    triggerUpdate()
                    showDialog = false
                })
                { Text(text = "Save") }
            },
            dismissButton = {
                Button(onClick = { showDialog = false })
                { Text(text = "Cancel") }
            }
        )
    }
}