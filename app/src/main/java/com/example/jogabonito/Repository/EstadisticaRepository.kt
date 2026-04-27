package com.example.jogabonito.Repository

import com.equipo.jogabonito.network.RetrofitClient
import com.example.jogabonito.model.EstadisticaJugador


class EstadisticaRepository {
    private val api = RetrofitClient.apiService

    suspend fun obtenerEstadisticas(): List<EstadisticaJugador> = api.obtenerEstadisticas()
    suspend fun obtenerEstadistica(id: Int): EstadisticaJugador = api.obtenerEstadistica(id)
    suspend fun crearEstadistica(e: EstadisticaJugador): EstadisticaJugador = api.crearEstadistica(e)
    suspend fun actualizarEstadistica(id: Int, e: EstadisticaJugador): EstadisticaJugador = api.actualizarEstadistica(id, e)
    suspend fun eliminarEstadistica(id: Int) = api.eliminarEstadistica(id)
}