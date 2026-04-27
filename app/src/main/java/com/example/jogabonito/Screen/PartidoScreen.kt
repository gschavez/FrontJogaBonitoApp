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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jogabonito.ViewModel.EquipoViewModel
import com.example.jogabonito.ViewModel.PartidoViewModel
import com.example.jogabonito.model.Equipo
import com.example.jogabonito.model.Partido

@Composable
fun PartidoScreen(
    vm: PartidoViewModel = viewModel(),
    equipoVm: EquipoViewModel = viewModel()
) {
    val partidos by vm.partidos.observeAsState(emptyList())
    val equipos by equipoVm.equipos.observeAsState(emptyList())
    val context = LocalContext.current

    var fecha by remember { mutableStateOf("") }
    var estadio by remember { mutableStateOf("") }
    var golesLocal by remember { mutableStateOf("") }
    var golesVisita by remember { mutableStateOf("") }
    var equipoLocal by remember { mutableStateOf<Equipo?>(null) }
    var equipoVisita by remember { mutableStateOf<Equipo?>(null) }
    var expandirLocal by remember { mutableStateOf(false) }
    var expandirVisita by remember { mutableStateOf(false) }
    var busqueda by remember { mutableStateOf("") }

    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    var partidoAEliminar by remember { mutableStateOf<Partido?>(null) }

    LaunchedEffect(true) {
        vm.obtenerPartidos()
        equipoVm.obtenerEquipos()
    }

    val partidosFiltrados = remember(busqueda, partidos) {
        partidos.filter {
            it.estadio.contains(busqueda, ignoreCase = true) ||
                    it.equipoLocal.nombre.contains(busqueda, ignoreCase = true) ||
                    it.equipoVisita.nombre.contains(busqueda, ignoreCase = true)
        }
    }

    // ── UN SOLO LazyColumn para todo (header + formulario + lista) ──────────
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 60.dp, bottom = 80.dp)
    ) {

        // Título
        item {
            Text("Partidos", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
        }

        // Buscador
        item {
            TextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Buscar por equipo o estadio") },
                leadingIcon = { Icon(Icons.Filled.Search, null) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
        }

        // Formulario
        item {
            Text("Registrar partido", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
        }

        item {
            TextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }

        item {
            TextField(
                value = estadio,
                onValueChange = { estadio = it },
                label = { Text("Estadio") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }

        // Selector equipo local
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { expandirLocal = true }, modifier = Modifier.fillMaxWidth()) {
                    Text("Local: ${equipoLocal?.nombre ?: "Seleccionar"}")
                }
                DropdownMenu(expanded = expandirLocal, onDismissRequest = { expandirLocal = false }) {
                    equipos.forEach { eq ->
                        DropdownMenuItem(
                            text = { Text(eq.nombre) },
                            onClick = { equipoLocal = eq; expandirLocal = false }
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        item {
            TextField(
                value = golesLocal,
                onValueChange = { golesLocal = it },
                label = { Text("Goles local") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }

        // Selector equipo visita
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { expandirVisita = true }, modifier = Modifier.fillMaxWidth()) {
                    Text("Visitante: ${equipoVisita?.nombre ?: "Seleccionar"}")
                }
                DropdownMenu(expanded = expandirVisita, onDismissRequest = { expandirVisita = false }) {
                    equipos.forEach { eq ->
                        DropdownMenuItem(
                            text = { Text(eq.nombre) },
                            onClick = { equipoVisita = eq; expandirVisita = false }
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        item {
            TextField(
                value = golesVisita,
                onValueChange = { golesVisita = it },
                label = { Text("Goles visita") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
        }

        item {
            Button(
                onClick = {
                    val nuevo = Partido(
                        fecha = fecha,
                        estadio = estadio,
                        equipoLocal = equipoLocal ?: Equipo(),
                        equipoVisita = equipoVisita ?: Equipo(),
                        golesLocal = golesLocal.toIntOrNull() ?: 0,
                        golesVisita = golesVisita.toIntOrNull() ?: 0
                    )
                    vm.crearPartido(nuevo) {
                        Toast.makeText(context, "Partido registrado", Toast.LENGTH_SHORT).show()
                        fecha = ""; estadio = ""; golesLocal = ""; golesVisita = ""
                        equipoLocal = null; equipoVisita = null
                    }
                },
                enabled = equipoLocal != null && equipoVisita != null,
            ) {
                Icon(Icons.Filled.Add, null)
                Spacer(Modifier.width(6.dp))
                Text("Registrar partido")
            }
            Spacer(Modifier.height(24.dp))
        }

        // ── Lista de partidos ────────────────────────────────────────────────
        if (partidosFiltrados.isEmpty()) {
            item { Text("No hay partidos registrados.") }
        } else {
            items(partidosFiltrados) { partido ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("ID: ${partido.idPartido}", style = MaterialTheme.typography.labelSmall)
                        Text(
                            "${partido.equipoLocal.nombre}  ${partido.golesLocal} - ${partido.golesVisita}  ${partido.equipoVisita.nombre}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Estadio: ${partido.estadio}", style = MaterialTheme.typography.bodyMedium)
                        Text("Fecha: ${partido.fecha}", style = MaterialTheme.typography.bodySmall)

                        Button(
                            onClick = {
                                partidoAEliminar = partido
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
            text = { Text("¿Eliminar el partido ID ${partidoAEliminar?.idPartido}?") },
            confirmButton = {
                TextButton(onClick = {
                    partidoAEliminar?.let {
                        vm.eliminarPartido(it.idPartido)
                        Toast.makeText(context, "Partido eliminado", Toast.LENGTH_SHORT).show()
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