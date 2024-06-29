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
import com.stauffer.pathtracker.data.weapons.WeaponDao
import com.stauffer.pathtracker.data.weapons.WeaponDatabase
import com.stauffer.pathtracker.data.weapons.WeaponItem
import com.stauffer.pathtracker.ui.BtmAppBar
import com.stauffer.pathtracker.ui.theme.PathtrackerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeaponScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PathtrackerTheme {
                var refreshKey by remember { mutableIntStateOf(0) }
                val db =
                    Room.databaseBuilder(
                        applicationContext,
                        WeaponDatabase::class.java,
                        "weapons"
                    )
                        .build()
                val weaponDao = remember { db.weaponDao() }
                val scope = rememberCoroutineScope()

                var showDeleteSingleDialog by remember { mutableStateOf(false) }
                var showDeleteAllDialog by remember { mutableStateOf(false) }
                var showEditDialog by remember { mutableStateOf(false) }
                var weaponItemToEdit by remember { mutableStateOf<WeaponItem?>(null) }

                var weaponId by remember { mutableIntStateOf(0) }
                var weaponName by remember { mutableStateOf("") }
                var weaponAttackBonus by remember { mutableStateOf("") }
                var weaponCritical by remember { mutableStateOf("") }
                var weaponType by remember { mutableStateOf("") }
                var weaponRange by remember { mutableStateOf("") }
                var weaponAmmunition by remember { mutableStateOf("") }
                var weaponDamage by remember { mutableStateOf("") }

                WeaponDialogs(
                    showDeleteSingleDialog,
                    showDeleteAllDialog,
                    showEditDialog,
                    weaponId,
                    weaponName,
                    weaponAttackBonus,
                    weaponCritical,
                    weaponType,
                    weaponRange,
                    weaponAmmunition,
                    weaponDamage,
                    onUpdateShowDeleteSingle = { showDeleteSingleDialog = it },
                    onUpdateShowDeleteAll = { showDeleteAllDialog = it },
                    onUpdateShowEdit = { showEditDialog = it },
                    weaponDao,
                    onUpdateWeaponItemToEdit = { weaponItemToEdit = it },
                    onConfirm = {
                        when (it) {
                            DialogType.DELETE_SINGLE -> {
                                weaponItemToEdit?.let {
                                    scope.launch {
                                        weaponDao.delete(it)
                                    }
                                }
                                weaponItemToEdit = null
                            }

                            DialogType.DELETE_ALL -> {
                                scope.launch(Dispatchers.IO) { db.clearAllTables() }
                            }

                            DialogType.UPDATE -> {
                                weaponItemToEdit?.let {
                                    scope.launch {
                                        weaponDao.update(it)
                                    }
                                }
                                weaponItemToEdit = null
                            }
                        }
                        refreshKey++
                    }
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomAppBar { BtmAppBar(this@WeaponScreen) } },
                    floatingActionButton = {
                        WFab(
                            weaponDao,
                            weaponName,
                            weaponAttackBonus,
                            weaponCritical,
                            weaponType,
                            weaponRange,
                            weaponAmmunition,
                            weaponDamage
                        ) { refreshKey++ }
                    }
                ) {
                    WeaponScreenContent(
                        modifier = Modifier.padding(it),
                        weaponDao,
                        refreshKey,
                        onDeleteSingle = { weaponItem ->
                            weaponItemToEdit = weaponItem
                            showDeleteSingleDialog = true
                        },
                        onDeleteAll = { showDeleteAllDialog = true },
                        onEdit = { weaponItem ->
                            weaponItemToEdit = weaponItem
                            weaponId = weaponItem.uid
                            weaponName = weaponItem.name.toString()
                            weaponAttackBonus = weaponItem.attackBonus.toString()
                            weaponCritical = weaponItem.critical.toString()
                            weaponType = weaponItem.type.toString()
                            weaponRange = weaponItem.range.toString()
                            weaponAmmunition = weaponItem.ammunition.toString()
                            weaponDamage = weaponItem.damage.toString()
                            showEditDialog = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun WeaponScreenContent(
    modifier: Modifier,
    weaponDao: WeaponDao,
    refreshKey: Int,
    onDeleteSingle: (WeaponItem) -> Unit,
    onDeleteAll: () -> Unit,
    onEdit: (WeaponItem) -> Unit
) {
    val weaponListState = remember { mutableStateOf<List<WeaponItem>>(emptyList()) }
    val weaponList by weaponListState

    LaunchedEffect(key1 = refreshKey) {
        weaponDao.get().collect { weaponListState.value = it }
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(weaponList) {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${it.name}",
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
                            Text("Attack Bonus: ${it.attackBonus}")
                            Text("Critical: ${it.critical}")
                            Text("Type: ${it.type}")
                        }
                        Column {
                            Text("Range: ${it.range}")
                            Text("Ammunition: ${it.ammunition}")
                            Text("Damage: ${it.damage}")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
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
private fun WeaponDialogs(
    showDeleteSingleDialog: Boolean,
    showDeleteAllDialog: Boolean,
    showEditDialog: Boolean,
    weaponId: Int,
    weaponName: String,
    weaponAttackBonus: String,
    weaponCritical: String,
    weaponType: String,
    weaponRange: String,
    weaponAmmunition: String,
    weaponDamage: String,
    onUpdateShowDeleteSingle: (Boolean) -> Unit,
    onUpdateShowDeleteAll: (Boolean) -> Unit,
    onUpdateShowEdit: (Boolean) -> Unit,
    weaponDao: WeaponDao,
    onUpdateWeaponItemToEdit: (WeaponItem?) -> Unit,
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
        var updatedWeaponItem by remember {
            mutableStateOf(
                WeaponItem(
                    weaponId,
                    weaponName,
                    weaponAttackBonus,
                    weaponCritical,
                    weaponType,
                    weaponRange,
                    weaponAmmunition,
                    weaponDamage
                )
            )
        }
        AlertDialog(
            title = { Text("Edit") },
            onDismissRequest = { onUpdateShowEdit(false) },
            text = {
                Column {
                    OutlinedTextField(
                        value = updatedWeaponItem.name ?: "",
                        onValueChange = {
                            updatedWeaponItem = updatedWeaponItem.copy(name = it)
                        },
                        label = { Text("Item Name") }
                    )
                    OutlinedTextField(
                        value = updatedWeaponItem.critical ?: "",
                        onValueChange = {
                            updatedWeaponItem = updatedWeaponItem.copy(critical = it)
                        },
                        label = { Text("Critical") }
                    )
                    OutlinedTextField(
                        value = updatedWeaponItem.type ?: "",
                        onValueChange = {
                            updatedWeaponItem = updatedWeaponItem.copy(type = it)
                        },
                        label = { Text("Type") }
                    )
                    OutlinedTextField(
                        value = updatedWeaponItem.range ?: "",
                        onValueChange = {
                            updatedWeaponItem = updatedWeaponItem.copy(range = it)
                        },
                        label = { Text("Range") }
                    )
                    OutlinedTextField(
                        value = updatedWeaponItem.ammunition ?: "",
                        onValueChange = {
                            updatedWeaponItem = updatedWeaponItem.copy(ammunition = it)
                        },
                        label = { Text("Ammunition") }
                    )
                    OutlinedTextField(
                        value = updatedWeaponItem.damage ?: "",
                        onValueChange = {
                            updatedWeaponItem = updatedWeaponItem.copy(damage = it)
                        },
                        label = { Text("Damage") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    updatedWeaponItem.let {
                        scope.launch(Dispatchers.IO) {
                            weaponDao.update(it)
                            onUpdateWeaponItemToEdit(null)
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
private fun WFab(
    weaponDao: WeaponDao,
    weaponName: String,
    weaponAttackBonus: String,
    weaponCritical: String,
    weaponType: String,
    weaponRange: String,
    weaponAmmunition: String,
    weaponDamage: String,
    triggerUpdate: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var localWeaponName by remember { mutableStateOf(weaponName) }
    val localWeaponBonus by remember { mutableStateOf(weaponAttackBonus) }
    var localWeaponCritical by remember { mutableStateOf(weaponCritical) }
    var localWeaponType by remember { mutableStateOf(weaponType) }
    var localWeaponRange by remember { mutableStateOf(weaponRange) }
    var localWeaponAmmunition by remember { mutableStateOf(weaponAmmunition) }
    var localWeaponDamage by remember { mutableStateOf(weaponDamage) }

    val newItem = WeaponItem(
        0,
        localWeaponName,
        localWeaponBonus,
        localWeaponCritical,
        localWeaponType,
        localWeaponRange,
        localWeaponAmmunition,
        localWeaponDamage
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
                        value = localWeaponName,
                        onValueChange = { localWeaponName = it },
                        label = { Text("Item Name") }
                    )
                    OutlinedTextField(
                        value = localWeaponCritical,
                        onValueChange = { localWeaponCritical = it },
                        label = { Text("Critical") }
                    )
                    OutlinedTextField(
                        value = localWeaponType,
                        onValueChange = { localWeaponType = it },
                        label = { Text("Type") }
                    )
                    OutlinedTextField(
                        value = localWeaponRange,
                        onValueChange = { localWeaponRange = it },
                        label = { Text("Range") }
                    )
                    OutlinedTextField(
                        value = localWeaponAmmunition,
                        onValueChange = { localWeaponAmmunition = it },
                        label = { Text("Ammunition") }
                    )
                    OutlinedTextField(
                        value = localWeaponDamage,
                        onValueChange = { localWeaponDamage = it },
                        label = { Text("Damage") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    scope.launch(Dispatchers.IO) { weaponDao.insert(newItem) }
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