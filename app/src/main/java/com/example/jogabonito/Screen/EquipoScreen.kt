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
import com.example.jogabonito.model.Equipo

@Composable
fun EquipoScreen(vm: EquipoViewModel = viewModel()) {
    val equipos by vm.equipos.observeAsState(emptyList())
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var fundacion by remember { mutableStateOf("") }  // Formato: "2001-06-15"
    var busqueda by remember { mutableStateOf("") }

    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    var equipoAEliminar by remember { mutableStateOf<Equipo?>(null) }

    LaunchedEffect(true) { vm.obtenerEquipos() }

    val equiposFiltrados = remember(busqueda, equipos) {
        equipos.filter { it.nombre.contains(busqueda, ignoreCase = true) }
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 60.dp)) {

        Text("Equipos", style = MaterialTheme.typography.headlineSmall)

        // Buscar
        TextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            label = { Text("Buscar por nombre") },
            leadingIcon = { Icon(Icons.Filled.Search, null) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        Text(
            "Agregar equipo",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp)
        )

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del equipo") },
            leadingIcon = { Icon(Icons.Filled.Star, null) },
            isError = nombre.isBlank(),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        TextField(
            value = ciudad,
            onValueChange = { ciudad = it },
            label = { Text("Ciudad") },
            leadingIcon = { Icon(Icons.Filled.LocationOn, null) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        TextField(
            value = fundacion,
            onValueChange = { fundacion = it },
            label = { Text("Fundación (YYYY-MM-DD)") },
            leadingIcon = { Icon(Icons.Filled.DateRange, null) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        Button(
            onClick = {
                val nuevo = Equipo(nombre = nombre, ciudad = ciudad, fundacion = fundacion)
                vm.crearEquipo(nuevo) {
                    Toast.makeText(context, "Equipo creado", Toast.LENGTH_SHORT).show()
                    nombre = ""; ciudad = ""; fundacion = ""
                }
            },
            enabled = nombre.isNotBlank(),
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Icon(Icons.Filled.Add, null)
            Spacer(Modifier.width(6.dp))
            Text("Agregar equipo")
        }

        Spacer(Modifier.height(16.dp))

        if (equiposFiltrados.isEmpty()) {
            Text("No hay equipos registrados.")
        } else {
            LazyColumn {
                items(equiposFiltrados) { equipo ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ID: ${equipo.idEquipo}", style = MaterialTheme.typography.labelSmall)
                            Text(equipo.nombre, style = MaterialTheme.typography.titleMedium)
                            Text("Ciudad: ${equipo.ciudad}", style = MaterialTheme.typography.bodyMedium)
                            Text("Fundación: ${equipo.fundacion}", style = MaterialTheme.typography.bodySmall)

                            Button(
                                onClick = {
                                    equipoAEliminar = equipo
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
            text = { Text("¿Eliminar el equipo '${equipoAEliminar?.nombre}'?") },
            confirmButton = {
                TextButton(onClick = {
                    equipoAEliminar?.let {
                        vm.eliminarEquipo(it.idEquipo)
                        Toast.makeText(context, "Equipo eliminado", Toast.LENGTH_SHORT).show()
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
