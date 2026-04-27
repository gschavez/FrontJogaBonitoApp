package com.example.jogabonito.Repository

import com.equipo.jogabonito.network.RetrofitClient
import com.example.jogabonito.model.Entrenador
import retrofit2.Retrofit


class EntrenadorRepository {
    private val api = RetrofitClient.apiService

    suspend fun obtenerEntrenadores(): List<Entrenador> = api.obtenerEntrenadores()
    suspend fun obtenerEntrenador(id: Int): Entrenador = api.obtenerEntrenador(id)
    suspend fun crearEntrenador(entrenador: Entrenador): Entrenador = api.crearEntrenador(entrenador)
    suspend fun actualizarEntrenador(id: Int, entrenador: Entrenador): Entrenador = api.actualizarEntrenador(id, entrenador)
    suspend fun eliminarEntrenador(id: Int) = api.eliminarEntrenador(id)
}