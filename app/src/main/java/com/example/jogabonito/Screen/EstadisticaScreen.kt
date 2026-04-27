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
import com.example.jogabonito.ViewModel.EstadisticaViewModel
import com.example.jogabonito.ViewModel.JugadorViewModel
import com.example.jogabonito.ViewModel.PartidoViewModel
import com.example.jogabonito.model.EstadisticaJugador
import com.example.jogabonito.model.Jugador
import com.example.jogabonito.model.Partido

@Composable
fun EstadisticaScreen(
    vm: EstadisticaViewModel = viewModel(),
    jugadorVm: JugadorViewModel = viewModel(),
    partidoVm: PartidoViewModel = viewModel()
) {
    val estadisticas by vm.estadisticas.observeAsState(emptyList())
    val jugadores by jugadorVm.jugadores.observeAsState(emptyList())
    val partidos by partidoVm.partidos.observeAsState(emptyList())
    val context = LocalContext.current

    var jugadorSel by remember { mutableStateOf<Jugador?>(null) }
    var partidoSel by remember { mutableStateOf<Partido?>(null) }
    var minutos by remember { mutableStateOf("") }
    var goles by remember { mutableStateOf("") }
    var asistencias by remember { mutableStateOf("") }
    var amarillas by remember { mutableStateOf("") }
    var rojas by remember { mutableStateOf("") }
    var expandirJugador by remember { mutableStateOf(false) }
    var expandirPartido by remember { mutableStateOf(false) }

    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    var estadisticaAEliminar by remember { mutableStateOf<EstadisticaJugador?>(null) }

    LaunchedEffect(true) {
        vm.obtenerEstadisticas()
        jugadorVm.obtenerJugadores()
        partidoVm.obtenerPartidos()
    }

    // ── UN SOLO LazyColumn para todo ────────────────────────────────────────
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 60.dp, bottom = 80.dp)
    ) {

        item {
            Text("Estadísticas", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))
        }

        item {
            Text("Registrar estadística", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
        }

        // Selector jugador
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { expandirJugador = true }, modifier = Modifier.fillMaxWidth()) {
                    Text("Jugador: ${jugadorSel?.nombre ?: "Seleccionar"}")
                }
                DropdownMenu(expanded = expandirJugador, onDismissRequest = { expandirJugador = false }) {
                    jugadores.forEach { j ->
                        DropdownMenuItem(
                            text = { Text("${j.nombre} (${j.equipo.nombre})") },
                            onClick = { jugadorSel = j; expandirJugador = false }
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        // Selector partido
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { expandirPartido = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        if (partidoSel != null)
                            "Partido: ${partidoSel!!.equipoLocal.nombre} vs ${partidoSel!!.equipoVisita.nombre}"
                        else "Seleccionar partido"
                    )
                }
                DropdownMenu(expanded = expandirPartido, onDismissRequest = { expandirPartido = false }) {
                    partidos.forEach { p ->
                        DropdownMenuItem(
                            text = { Text("${p.equipoLocal.nombre} vs ${p.equipoVisita.nombre} (${p.fecha})") },
                            onClick = { partidoSel = p; expandirPartido = false }
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        item {
            TextField(value = minutos, onValueChange = { minutos = it },
                label = { Text("Minutos jugados") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
        }
        item {
            TextField(value = goles, onValueChange = { goles = it },
                label = { Text("Goles") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
        }
        item {
            TextField(value = asistencias, onValueChange = { asistencias = it },
                label = { Text("Asistencias") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
        }
        item {
            TextField(value = amarillas, onValueChange = { amarillas = it },
                label = { Text("Tarjetas amarillas") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
        }
        item {
            TextField(value = rojas, onValueChange = { rojas = it },
                label = { Text("Tarjetas rojas") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
        }

        item {
            Button(
                onClick = {
                    val nueva = EstadisticaJugador(
                        jugador = jugadorSel!!,
                        partido = partidoSel!!,
                        minutosJugados = minutos.toIntOrNull() ?: 0,
                        goles = goles.toIntOrNull() ?: 0,
                        asistencias = asistencias.toIntOrNull() ?: 0,
                        tarjetasAmarillas = amarillas.toIntOrNull() ?: 0,
                        tarjetasRojas = rojas.toIntOrNull() ?: 0
                    )
                    vm.crearEstadistica(nueva) {
                        Toast.makeText(context, "Estadística registrada", Toast.LENGTH_SHORT).show()
                        jugadorSel = null; partidoSel = null
                        minutos = ""; goles = ""; asistencias = ""; amarillas = ""; rojas = ""
                    }
                },
                enabled = jugadorSel != null && partidoSel != null,
            ) {
                Icon(Icons.Filled.Add, null)
                Spacer(Modifier.width(6.dp))
                Text("Registrar estadística")
            }
            Spacer(Modifier.height(24.dp))
        }

        // ── Lista de estadísticas ────────────────────────────────────────────
        if (estadisticas.isEmpty()) {
            item { Text("No hay estadísticas registradas.") }
        } else {
            items(estadisticas) { est ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("ID: ${est.idEstadistica}", style = MaterialTheme.typography.labelSmall)
                        Text(est.jugador.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(
                            "Partido: ${est.partido.equipoLocal.nombre} vs ${est.partido.equipoVisita.nombre}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            "Minutos: ${est.minutosJugados}  |  Goles: ${est.goles}  |  Asistencias: ${est.asistencias}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            "Amarillas: ${est.tarjetasAmarillas}  |  Rojas: ${est.tarjetasRojas}",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Button(
                            onClick = {
                                estadisticaAEliminar = est
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
            text = { Text("¿Eliminar la estadística ID ${estadisticaAEliminar?.idEstadistica}?") },
            confirmButton = {
                TextButton(onClick = {
                    estadisticaAEliminar?.let {
                        vm.eliminarEstadistica(it.idEstadistica)
                        Toast.makeText(context, "Estadística eliminada", Toast.LENGTH_SHORT).show()
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