package com.example.meucarro.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.meucarro.services.database.user_preferences.UserPreferences
import com.example.meucarro.services.http.RetrofitClient
import com.example.meucarro.services.http.maintenance.MaintenanceService
import com.example.meucarro.services.http.maintenance.dto.request.CreateMaintenanceRequest
import com.example.meucarro.services.worker.testReminderNow
import com.example.meucarro.ui.theme.AzulPrincipal
import com.example.meucarro.ui.theme.FundoClaro
import com.example.meucarro.ui.theme.TextoPrincipal
import com.example.meucarro.ui.theme.TextoSecundario
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val notes = remember { mutableStateListOf<Note>() }
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val coroutineScope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var noteToEdit by remember { mutableStateOf<Note?>(null) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }
    val isRefreshing = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        testReminderNow(context)
    }

    LaunchedEffect(Unit) {
        try {
            val service = RetrofitClient.createService(
                MaintenanceService::class.java,
                context
            )
            val response = service.getMaintenances()
            notes.clear()
            notes.addAll(response.map {
                Note(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    odometer = it.odometer,
                    performedAt = it.performedAt,
                    nextDueAt = it.nextDueAt
                )
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun refreshMaintenances() {
        coroutineScope.launch {
            isRefreshing.value = true
            try {
                val service = RetrofitClient.createService(MaintenanceService::class.java, context)
                val response = service.getMaintenances()
                notes.clear()
                notes.addAll(response.map {
                    Note(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        odometer = it.odometer,
                        performedAt = it.performedAt,
                        nextDueAt = it.nextDueAt
                    )
                })
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isRefreshing.value = false
            }
        }
    }

    Scaffold(
        containerColor = FundoClaro,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Meu Carro",
                        color = TextoPrincipal
                    )
                },
                actions = {
                    IconButton(onClick = {
                        showLogoutDialog = true
                    }) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Sair",
                            tint = TextoPrincipal
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                noteToEdit = null
                showDialog = true
            }, containerColor = AzulPrincipal) {
                Icon(Icons.Default.Add, contentDescription = "Criar Nota", tint = Color.White)
            }
        }
    ) { padding ->
        Surface(
            modifier = Modifier.padding(padding),
            color = FundoClaro
        ) {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing.value),
                onRefresh = { refreshMaintenances() }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(notes) { note ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    text = note.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextoPrincipal
                                )
                                Text(text = note.description, color = TextoSecundario)
                                Text(
                                    text = "Odômetro: ${note.odometer} km",
                                    color = TextoSecundario
                                )
                                Text(
                                    text = "Realizado em: ${formatFromIsoDate(note.performedAt)}",
                                    color = TextoSecundario
                                )
                                Text(
                                    text = "Próxima em: ${formatFromIsoDate(note.nextDueAt)}",
                                    color = TextoSecundario
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton(onClick = {
                                        noteToEdit = note
                                        showDialog = true
                                    }) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Editar",
                                            tint = AzulPrincipal
                                        )
                                    }
                                    IconButton(onClick = {
                                        noteToDelete = note
                                        showDeleteDialog = true
                                    }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Excluir",
                                            tint = TextoSecundario
                                        )
                                    }
                                }
                            }

                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(66.dp))
                    }
                }
            }
        }
    }

    if (showDialog) {
        CreateNoteDialog(
            onDismiss = {
                showDialog = false
                noteToEdit = null
            },
            onSave = { newNote ->
                coroutineScope.launch {
                    try {
                        val service = RetrofitClient.createService(
                            MaintenanceService::class.java,
                            context
                        )

                        if (noteToEdit != null) {
                            val response = service.updateMaintenance(
                                noteToEdit!!.id!!,
                                CreateMaintenanceRequest(
                                    clientId = noteToEdit!!.id!!,
                                    name = newNote.name,
                                    description = newNote.description,
                                    odometer = newNote.odometer,
                                    performedAt = newNote.performedAt,
                                    nextDueAt = newNote.nextDueAt
                                )
                            )

                            val index = notes.indexOfFirst { it.id == noteToEdit!!.id }
                            if (index != -1) {
                                notes[index] = Note(
                                    id = response.id,
                                    name = response.name,
                                    description = response.description,
                                    odometer = response.odometer,
                                    performedAt = response.performedAt,
                                    nextDueAt = response.nextDueAt
                                )
                            }
                        } else {
                            val maintenanceId = UUID.randomUUID().toString()
                            val response = service.createMaintenance(
                                CreateMaintenanceRequest(
                                    clientId = maintenanceId,
                                    name = newNote.name,
                                    description = newNote.description,
                                    odometer = newNote.odometer,
                                    performedAt = newNote.performedAt,
                                    nextDueAt = newNote.nextDueAt
                                )
                            )

                            notes.add(
                                Note(
                                    id = response.id,
                                    name = response.name,
                                    description = response.description,
                                    odometer = response.odometer,
                                    performedAt = response.performedAt,
                                    nextDueAt = response.nextDueAt
                                )
                            )
                        }

                        showDialog = false
                        noteToEdit = null
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            },
            noteToEdit = noteToEdit
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Deseja sair?", color = TextoPrincipal) },
            text = { Text("Você será desconectado da sua conta.", color = TextoPrincipal) },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        userPrefs.clear()
                        showLogoutDialog = false
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                }) {
                    Text("Sair", color = AzulPrincipal)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar", color = TextoSecundario)
                }
            }
        )
    }

    if (showDeleteDialog && noteToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar exclusão", color = TextoPrincipal) },
            text = {
                Text(
                    "Tem certeza que deseja excluir esta nota?",
                    color = TextoPrincipal,
                    fontSize = 18.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        try {
                            val service = RetrofitClient.createService(
                                MaintenanceService::class.java,
                                context
                            )

                            noteToDelete?.id?.let { id ->
                                service.deleteMaintenance(id)
                                notes.remove(noteToDelete)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            noteToDelete = null
                            showDeleteDialog = false
                        }
                    }
                }) {
                    Text("Excluir", color = Color.Blue)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    noteToDelete = null
                }) {
                    Text("Cancelar", color = TextoSecundario)
                }
            }
        )
    }
}

@Composable
fun CreateNoteDialog(onDismiss: () -> Unit, onSave: (Note) -> Unit, noteToEdit: Note? = null) {
    var name by remember { mutableStateOf(TextFieldValue(noteToEdit?.name ?: "")) }
    var description by remember { mutableStateOf(TextFieldValue(noteToEdit?.description ?: "")) }
    var odometer by remember {
        mutableStateOf(
            TextFieldValue(
                noteToEdit?.odometer?.toString() ?: ""
            )
        )
    }
    var performedAt by remember {
        mutableStateOf(
            noteToEdit?.performedAt?.let { formatFromIsoDate(it) } ?: ""
        )
    }

    var nextDueAt by remember {
        mutableStateOf(
            noteToEdit?.nextDueAt?.let { formatFromIsoDate(it) } ?: ""
        )
    }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (noteToEdit != null) "Editar Nota" else "Nova Nota",
                color = TextoPrincipal
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    textStyle = TextStyle(color = TextoPrincipal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulPrincipal,
                        unfocusedBorderColor = TextoSecundario,
                        cursorColor = TextoPrincipal
                    )
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição") },
                    textStyle = TextStyle(color = TextoPrincipal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulPrincipal,
                        unfocusedBorderColor = TextoSecundario,
                        cursorColor = TextoPrincipal
                    )
                )
                OutlinedTextField(
                    value = odometer,
                    onValueChange = { odometer = it },
                    label = { Text("Odômetro") },
                    textStyle = TextStyle(color = TextoPrincipal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulPrincipal,
                        unfocusedBorderColor = TextoSecundario,
                        cursorColor = TextoPrincipal
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Realizado em: $performedAt", color = TextoPrincipal)
                    IconButton(onClick = {
                        val c = Calendar.getInstance()
                        DatePickerDialog(
                            context,
                            { _, y, m, d -> performedAt = "$d/${m + 1}/$y" },
                            c[Calendar.YEAR],
                            c[Calendar.MONTH],
                            c[Calendar.DAY_OF_MONTH]
                        ).show()
                    }) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Realizado em",
                            tint = AzulPrincipal
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Próxima data: $nextDueAt", color = TextoPrincipal)
                    IconButton(onClick = {
                        val c = Calendar.getInstance()
                        DatePickerDialog(
                            context,
                            { _, y, m, d -> nextDueAt = "$d/${m + 1}/$y" },
                            c[Calendar.YEAR],
                            c[Calendar.MONTH],
                            c[Calendar.DAY_OF_MONTH]
                        ).show()
                    }) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Próxima data",
                            tint = AzulPrincipal
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(
                    Note(
                        name = name.text,
                        description = description.text,
                        odometer = odometer.text.toIntOrNull() ?: 0,
                        performedAt = formatToIsoDate(performedAt),
                        nextDueAt = formatToIsoDate(nextDueAt)
                    )
                )
            }) {
                Text("Salvar", color = AzulPrincipal)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextoSecundario)
            }
        }
    )
}

data class Note(
    val id: String? = null,
    val name: String,
    val description: String,
    val odometer: Int,
    val performedAt: String,
    val nextDueAt: String
)

fun formatToIsoDate(input: String): String {
    return try {
        val inputFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormatter.parse(input)
        val outputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        outputFormatter.timeZone = TimeZone.getTimeZone("UTC")
        outputFormatter.format(date!!)
    } catch (e: Exception) {
        input
    }
}

fun formatFromIsoDate(input: String): String {
    return try {
        val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        isoFormatter.timeZone = TimeZone.getTimeZone("UTC")
        val date = isoFormatter.parse(input)
        val displayFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        displayFormatter.format(date!!)
    } catch (e: Exception) {
        input
    }
}