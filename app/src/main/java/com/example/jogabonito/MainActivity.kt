package com.example.jogabonito

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jogabonito.Screen.EntrenadorScreen
import com.example.jogabonito.Screen.EquipoScreen
import com.example.jogabonito.Screen.EstadisticaScreen
import com.example.jogabonito.Screen.JugadorScreen
import com.example.jogabonito.Screen.PartidoScreen

// Rutas de navegación
object Rutas {
    const val EQUIPOS = "equipos"
    const val JUGADORES = "jugadores"
    const val ENTRENADORES = "entrenadores"
    const val PARTIDOS = "partidos"
    const val ESTADISTICAS = "estadisticas"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                JogaBonitaApp()
            }
        }
    }
}

@Composable
fun JogaBonitaApp() {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val rutaActual = backStack?.destination?.route ?: Rutas.EQUIPOS

    val items = listOf(
        Triple(Rutas.EQUIPOS,      "Equipos",      Icons.Filled.Star),
        Triple(Rutas.JUGADORES,    "Jugadores",    Icons.Filled.Person),
        Triple(Rutas.ENTRENADORES, "Entrenadores", Icons.Filled.Person),
        Triple(Rutas.PARTIDOS,     "Partidos",     Icons.Filled.Star),
        Triple(Rutas.ESTADISTICAS, "Stats",        Icons.Filled.Star)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { (ruta, label, icon) ->
                    NavigationBarItem(
                        selected = rutaActual == ruta,
                        onClick = {
                            navController.navigate(ruta) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.EQUIPOS,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rutas.EQUIPOS)      { EquipoScreen() }
            composable(Rutas.JUGADORES)    { JugadorScreen() }
            composable(Rutas.ENTRENADORES) { EntrenadorScreen() }
            composable(Rutas.PARTIDOS)     { PartidoScreen() }
            composable(Rutas.ESTADISTICAS) { EstadisticaScreen() }
        }
    }
}
