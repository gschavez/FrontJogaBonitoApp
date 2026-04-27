package com.example.jogabonito.Screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jogabonito.ViewModel.EntrenadorViewModel
import com.example.jogabonito.ViewModel.EquipoViewModel
import com.example.jogabonito.model.Entrenador
import com.example.jogabonito.model.Equipo

@Composable
fun EntrenadorScreen(
    vm: EntrenadorViewModel = viewModel(),
    equipoVm: EquipoViewModel = viewModel()
) {
    val entrenadores by vm.entrenadores.observeAsState(emptyList())
    val equipos by equipoVm.equipos.observeAsState(emptyList())
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    var equipoSeleccionado by remember { mutableStateOf<Equipo?>(null) }
    var expandirEquipo by remember { mutableStateOf(false) }
    var busqueda by remember { mutableStateOf("") }

    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    var entrenadorAEliminar by remember { mutableStateOf<Entrenador?>(null) }

    LaunchedEffect(true) {
        vm.obtenerEntrenadores()
        equipoVm.obtenerEquipos()
    }

    val entrenadoresFiltrados = remember(busqueda, entrenadores) {
        entrenadores.filter { it.nombre.contains(busqueda, ignoreCase = true) }
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 60.dp)) {

        Text("Entrenadores", style = MaterialTheme.typography.headlineSmall)

        TextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            label = { Text("Buscar por nombre") },
            leadingIcon = { Icon(Icons.Filled.Search, null) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        Text("Agregar entrenador", style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp))

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del entrenador") },
            isError = nombre.isBlank(),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        TextField(
            value = especialidad,
            onValueChange = { especialidad = it },
            label = { Text("Especialidad") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        Box(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            OutlinedButton(
                onClick = { expandirEquipo = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(equipoSeleccionado?.nombre ?: "Seleccionar equipo")
            }
            DropdownMenu(
                expanded = expandirEquipo,
                onDismissRequest = { expandirEquipo = false }
            ) {
                equipos.forEach { equipo ->
                    DropdownMenuItem(
                        text = { Text(equipo.nombre) },
                        onClick = {
                            equipoSeleccionado = equipo
                            expandirEquipo = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                val nuevo = Entrenador(
                    nombre = nombre,
                    especialidad = especialidad,
                    equipo = equipoSeleccionado ?: Equipo()
                )
                vm.crearEntrenador(nuevo) {
                    Toast.makeText(context, "Entrenador creado", Toast.LENGTH_SHORT).show()
                    nombre = ""; especialidad = ""; equipoSeleccionado = null
                }
            },
            enabled = nombre.isNotBlank() && equipoSeleccionado != null,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Icon(Icons.Filled.Add, null)
            Spacer(Modifier.width(6.dp))
            Text("Agregar entrenador")
        }

        Spacer(Modifier.height(16.dp))

        if (entrenadoresFiltrados.isEmpty()) {
            Text("No hay entrenadores registrados.")
        } else {
            LazyColumn {
                items(entrenadoresFiltrados) { entrenador ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ID: ${entrenador.idEntrenador}", style = MaterialTheme.typography.labelSmall)
                            Text(entrenador.nombre, style = MaterialTheme.typography.titleMedium)
                            Text("Especialidad: ${entrenador.especialidad}", style = MaterialTheme.typography.bodyMedium)
                            Text("Equipo: ${entrenador.equipo.nombre}", style = MaterialTheme.typography.bodyMedium)

                            Button(
                                onClick = {
                                    entrenadorAEliminar = entrenador
                                    mostrarDialogoEliminar = true
                                },
                                modifier = Modifier.padding(top = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Icon(Icons.Filled.Delete, null)
                                Spacer(Modifier.width(6.dp))
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Eliminar al entrenador '${entrenadorAEliminar?.nombre}'?") },
            confirmButton = {
                TextButton(onClick = {
                    entrenadorAEliminar?.let {
                        vm.eliminarEntrenador(it.idEntrenador)
                        Toast.makeText(context, "Entrenador eliminado", Toast.LENGTH_SHORT).show()
                    }
                    mostrarDialogoEliminar = false
                }) { Text("Sí, eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) { Text("Cancelar") }
            }
        )
    }
}