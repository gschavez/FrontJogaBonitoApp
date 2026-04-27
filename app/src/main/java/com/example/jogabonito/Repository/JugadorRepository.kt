package com.example.jogabonito.Repository

import com.equipo.jogabonito.network.RetrofitClient
import com.example.jogabonito.model.Jugador

class JugadorRepository {
    private val api = RetrofitClient.apiService

    suspend fun obtenerJugadores(): List<Jugador> = api.obtenerJugadores()
    suspend fun obtenerJugador(id: Int): Jugador = api.obtenerJugador(id)
    suspend fun crearJugador(jugador: Jugador): Jugador = api.crearJugador(jugador)
    suspend fun actualizarJugador(id: Int, jugador: Jugador): Jugador = api.actualizarJugador(id, jugador)
    suspend fun eliminarJugador(id: Int) = api.eliminarJugador(id)
    suspend fun jugadoresPorEquipo(idEquipo: Int): List<Jugador> = api.jugadoresPorEquipo(idEquipo)
    suspend fun jugadoresConMasGoles(minGoles: Int): List<Jugador> = api.jugadoresConMasGoles(minGoles)
}