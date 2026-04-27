package com.example.jogabonito.Repository

import com.equipo.jogabonito.network.RetrofitClient
import com.example.jogabonito.model.Partido

class PartidoRepository {
    private val api = RetrofitClient.apiService

    suspend fun obtenerPartidos(): List<Partido> = api.obtenerPartidos()
    suspend fun obtenerPartido(id: Int): Partido = api.obtenerPartido(id)
    suspend fun crearPartido(partido: Partido): Partido = api.crearPartido(partido)
    suspend fun actualizarPartido(id: Int, partido: Partido): Partido = api.actualizarPartido(id, partido)
    suspend fun eliminarPartido(id: Int) = api.eliminarPartido(id)
    suspend fun totalGolesPorEquipo(idEquipo: Int): Map<String, Any> = api.totalGolesPorEquipo(idEquipo)
    suspend fun resultadosPartidos(): List<Map<String, Any>> = api.resultadosPartidos()
}
