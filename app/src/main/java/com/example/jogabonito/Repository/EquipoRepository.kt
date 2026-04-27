package com.example.jogabonito.Repository

import com.equipo.jogabonito.network.RetrofitClient
import com.example.jogabonito.model.Equipo

class EquipoRepository {
    private val api = RetrofitClient.apiService

    suspend fun obtenerEquipos(): List<Equipo> = api.obtenerEquipos()
    suspend fun obtenerEquipo(id: Int): Equipo = api.obtenerEquipo(id)
    suspend fun crearEquipo(equipo: Equipo): Equipo = api.crearEquipo(equipo)
    suspend fun actualizarEquipo(id: Int, equipo: Equipo): Equipo = api.actualizarEquipo(id, equipo)
    suspend fun eliminarEquipo(id: Int) = api.eliminarEquipo(id)
}