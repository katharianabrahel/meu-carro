package com.example.meucarro.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    val notes = remember { mutableStateListOf<Note>() }
    var noteToEdit by remember { mutableStateOf<Note?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meu Carro") },
                actions = {
                    IconButton(onClick = { navController.navigate("login") }) {
                        Icon(Icons.Default.Person, contentDescription = "Login")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                noteToEdit = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Criar Nota")
            }
        }
    ) { padding ->
        Surface(modifier = Modifier.padding(padding)) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                items(notes) { note ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(text = note.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = note.description)
                            Text(text = "Odômetro: ${note.odometer} km")
                            Text(text = "Realizado em: ${note.performedAt}")
                            Text(text = "Próxima em: ${note.nextDueAt}")
                            Row(modifier = Modifier.align(Alignment.End)) {
                                IconButton(onClick = {
                                    noteToEdit = note
                                    showDialog = true
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                                }
                                IconButton(onClick = { notes.remove(note) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Excluir")
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
}

@Composable
fun CreateNoteDialog(onDismiss: () -> Unit, onSave: (Note) -> Unit, noteToEdit: Note? = null) {
    var name by remember { mutableStateOf(TextFieldValue(noteToEdit?.name ?: "")) }
    var description by remember { mutableStateOf(TextFieldValue(noteToEdit?.description ?: "")) }
    var odometer by remember { mutableStateOf(TextFieldValue(noteToEdit?.odometer?.toString() ?: "")) }
    var performedAt by remember { mutableStateOf(noteToEdit?.performedAt ?: "") }
    var nextDueAt by remember { mutableStateOf(noteToEdit?.nextDueAt ?: "") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (noteToEdit != null) "Editar Nota" else "Nova Nota") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descrição") })
                OutlinedTextField(value = odometer, onValueChange = { odometer = it }, label = { Text("Odômetro") })
                Button(onClick = {
                    val c = Calendar.getInstance()
                    DatePickerDialog(context, { _, y, m, d -> performedAt = "$d/${m+1}/$y" }, c[Calendar.YEAR], c[Calendar.MONTH], c[Calendar.DAY_OF_MONTH]).show()
                }) { Text("Selecionar data realizada: $performedAt") }
                Button(onClick = {
                    val c = Calendar.getInstance()
                    DatePickerDialog(context, { _, y, m, d -> nextDueAt = "$d/${m+1}/$y" }, c[Calendar.YEAR], c[Calendar.MONTH], c[Calendar.DAY_OF_MONTH]).show()
                }) { Text("Selecionar próxima data: $nextDueAt") }
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
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
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
