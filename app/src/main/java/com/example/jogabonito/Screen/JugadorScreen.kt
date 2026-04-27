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
import com.example.jogabonito.ViewModel.EquipoViewModel
import com.example.jogabonito.ViewModel.JugadorViewModel
import com.example.jogabonito.model.Equipo
import com.example.jogabonito.model.Jugador

@Composable
fun JugadorScreen(
    vm: JugadorViewModel = viewModel(),
    equipoVm: EquipoViewModel = viewModel()
) {
    val jugadores by vm.jugadores.observeAsState(emptyList())
    val equipos by equipoVm.equipos.observeAsState(emptyList())
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var posicion by remember { mutableStateOf("") }
    var dorsal by remember { mutableStateOf("") }
    var fechaNac by remember { mutableStateOf("") }
    var nacionalidad by remember { mutableStateOf("") }
    var equipoSeleccionado by remember { mutableStateOf<Equipo?>(null) }
    var expandirEquipo by remember { mutableStateOf(false) }
    var busqueda by remember { mutableStateOf("") }

    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    var jugadorAEliminar by remember { mutableStateOf<Jugador?>(null) }

    LaunchedEffect(true) {
        vm.obtenerJugadores()
        equipoVm.obtenerEquipos()
    }

    val jugadoresFiltrados = remember(busqueda, jugadores) {
        jugadores.filter { it.nombre.contains(busqueda, ignoreCase = true) }
    }

    // ── UN SOLO LazyColumn para todo ────────────────────────────────────────
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 60.dp, bottom = 80.dp)
    ) {

        item {
            Text("Jugadores", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
        }

        item {
            TextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Buscar por nombre") },
                leadingIcon = { Icon(Icons.Filled.Search, null) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
        }

        item {
            Text("Agregar jugador", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
        }

        item {
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                isError = nombre.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }

        item {
            TextField(
                value = posicion,
                onValueChange = { posicion = it },
                label = { Text("Posición (ej: Delantero)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }

        item {
            TextField(
                value = dorsal,
                onValueChange = { dorsal = it },
                label = { Text("Dorsal (número)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }

        item {
            TextField(
                value = fechaNac,
                onValueChange = { fechaNac = it },
                label = { Text("Fecha nacimiento (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }

        item {
            TextField(
                value = nacionalidad,
                onValueChange = { nacionalidad = it },
                label = { Text("Nacionalidad") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }

        // Selector equipo
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
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
                            onClick = { equipoSeleccionado = equipo; expandirEquipo = false }
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        item {
            Button(
                onClick = {
                    val nuevo = Jugador(
                        nombre = nombre,
                        posicion = posicion,
                        dorsal = dorsal.toIntOrNull() ?: 0,
                        fechaNac = fechaNac,
                        nacionalidad = nacionalidad,
                        equipo = equipoSeleccionado ?: Equipo()
                    )
                    vm.crearJugador(nuevo) {
                        Toast.makeText(context, "Jugador creado", Toast.LENGTH_SHORT).show()
                        nombre = ""; posicion = ""; dorsal = ""
                        fechaNac = ""; nacionalidad = ""; equipoSeleccionado = null
                    }
                },
                enabled = nombre.isNotBlank() && equipoSeleccionado != null,
            ) {
                Icon(Icons.Filled.Add, null)
                Spacer(Modifier.width(6.dp))
                Text("Agregar jugador")
            }
            Spacer(Modifier.height(24.dp))
        }

        // ── Lista de jugadores ───────────────────────────────────────────────
        if (jugadoresFiltrados.isEmpty()) {
            item { Text("No hay jugadores registrados.") }
        } else {
            items(jugadoresFiltrados) { jugador ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("ID: ${jugador.idJugador}", style = MaterialTheme.typography.labelSmall)
                        Text("#${jugador.dorsal} ${jugador.nombre}", style = MaterialTheme.typography.titleMedium)
                        Text("Posición: ${jugador.posicion}", style = MaterialTheme.typography.bodyMedium)
                        Text("Equipo: ${jugador.equipo.nombre}", style = MaterialTheme.typography.bodyMedium)
                        Text("Nacimiento: ${jugador.fechaNac}", style = MaterialTheme.typography.bodySmall)
                        Text("Nacionalidad: ${jugador.nacionalidad}", style = MaterialTheme.typography.bodySmall)

                        Button(
                            onClick = {
                                jugadorAEliminar = jugador
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

    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Eliminar a '${jugadorAEliminar?.nombre}'?") },
            confirmButton = {
                TextButton(onClick = {
                    jugadorAEliminar?.let {
                        vm.eliminarJugador(it.idJugador)
                        Toast.makeText(context, "Jugador eliminado", Toast.LENGTH_SHORT).show()
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