package com.example.meucarro.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.meucarro.services.http.RetrofitClient
import com.example.meucarro.services.http.maintenance.MaintenanceService
import com.example.meucarro.ui.theme.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val notes = remember { mutableStateListOf<Note>() }
    val context = LocalContext.current
    var noteToEdit by remember { mutableStateOf<Note?>(null) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }


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
                    name = it.name,
                    description = it.description,
                    odometer = it.odometer,
                    performedAt = it.performedAt,
                    nextDueAt = it.nextDueAt
                )
            })
        } catch (e: Exception) {
            e.printStackTrace()
            // você pode exibir um Toast ou Snackbar aqui
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
                            Text(text = "Odômetro: ${note.odometer} km", color = TextoSecundario)
                            Text(
                                text = "Realizado em: ${note.performedAt}",
                                color = TextoSecundario
                            )
                            Text(text = "Próxima em: ${note.nextDueAt}", color = TextoSecundario)
                            Row(modifier = Modifier.align(Alignment.End)) {
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
                noteToEdit?.let {
                    val index = notes.indexOf(it)
                    if (index != -1) notes[index] = newNote
                } ?: notes.add(newNote)
                showDialog = false
                noteToEdit = null
            },
            noteToEdit = noteToEdit
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
                    notes.remove(noteToDelete)
                    noteToDelete = null
                    showDeleteDialog = false
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
    var performedAt by remember { mutableStateOf(noteToEdit?.performedAt ?: "") }
    var nextDueAt by remember { mutableStateOf(noteToEdit?.nextDueAt ?: "") }
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
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Data realizada: $performedAt", color = TextoPrincipal)
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
                            contentDescription = "Data realizada",
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
                        performedAt = performedAt,
                        nextDueAt = nextDueAt
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
    val name: String,
    val description: String,
    val odometer: Int,
    val performedAt: String,
    val nextDueAt: String
)
