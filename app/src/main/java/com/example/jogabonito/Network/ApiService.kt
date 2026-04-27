package com.example.jogabonito.Network

import com.example.jogabonito.model.Entrenador
import com.example.jogabonito.model.Equipo
import com.example.jogabonito.model.EstadisticaJugador
import com.example.jogabonito.model.Jugador
import com.example.jogabonito.model.Partido
import retrofit2.http.*


// NOTA: 10.0.2.2 es la IP del host (tu PC) vista desde el emulador Android
// Si usas dispositivo físico, cambia por la IP local de tu PC, ej: "http://192.168.1.X:8080"

interface ApiService {

    // ─── EQUIPOS ────────────────────────────────────────────────────────────
    @GET("api/equipos")
    suspend fun obtenerEquipos(): List<Equipo>

    @GET("api/equipos/{id}")
    suspend fun obtenerEquipo(@Path("id") id: Int): Equipo

    @POST("api/equipos")
    suspend fun crearEquipo(@Body equipo: Equipo): Equipo

    @PUT("api/equipos/{id}")
    suspend fun actualizarEquipo(@Path("id") id: Int, @Body equipo: Equipo): Equipo

    @DELETE("api/equipos/{id}")
    suspend fun eliminarEquipo(@Path("id") id: Int)

    // ─── JUGADORES ──────────────────────────────────────────────────────────
    @GET("api/jugadores")
    suspend fun obtenerJugadores(): List<Jugador>

    @GET("api/jugadores/{id}")
    suspend fun obtenerJugador(@Path("id") id: Int): Jugador

    @POST("api/jugadores")
    suspend fun crearJugador(@Body jugador: Jugador): Jugador

    @PUT("api/jugadores/{id}")
    suspend fun actualizarJugador(@Path("id") id: Int, @Body jugador: Jugador): Jugador

    @DELETE("api/jugadores/{id}")
    suspend fun eliminarJugador(@Path("id") id: Int)

    // Consultas nativas de jugadores
    @GET("api/jugadores/equipo/{idEquipo}")
    suspend fun jugadoresPorEquipo(@Path("idEquipo") idEquipo: Int): List<Jugador>

    @GET("api/jugadores/goles-mayor/{minGoles}")
    suspend fun jugadoresConMasGoles(@Path("minGoles") minGoles: Int): List<Jugador>

    // ─── ENTRENADORES ───────────────────────────────────────────────────────
    @GET("api/entrenadores")
    suspend fun obtenerEntrenadores(): List<Entrenador>

    @GET("api/entrenadores/{id}")
    suspend fun obtenerEntrenador(@Path("id") id: Int): Entrenador

    @POST("api/entrenadores")
    suspend fun crearEntrenador(@Body entrenador: Entrenador): Entrenador

    @PUT("api/entrenadores/{id}")
    suspend fun actualizarEntrenador(@Path("id") id: Int, @Body entrenador: Entrenador): Entrenador

    @DELETE("api/entrenadores/{id}")
    suspend fun eliminarEntrenador(@Path("id") id: Int)

    // ─── PARTIDOS ───────────────────────────────────────────────────────────
    @GET("api/partidos")
    suspend fun obtenerPartidos(): List<Partido>

    @GET("api/partidos/{id}")
    suspend fun obtenerPartido(@Path("id") id: Int): Partido

    @POST("api/partidos")
    suspend fun crearPartido(@Body partido: Partido): Partido

    @PUT("api/partidos/{id}")
    suspend fun actualizarPartido(@Path("id") id: Int, @Body partido: Partido): Partido

    @DELETE("api/partidos/{id}")
    suspend fun eliminarPartido(@Path("id") id: Int)

    // Consultas nativas de partidos
    @GET("api/partidos/goles-equipo/{idEquipo}")
    suspend fun totalGolesPorEquipo(@Path("idEquipo") idEquipo: Int): Map<String, Any>

    @GET("api/partidos/resultados")
    suspend fun resultadosPartidos(): List<Map<String, Any>>

    // ─── ESTADÍSTICAS ───────────────────────────────────────────────────────
    @GET("api/estadisticas")
    suspend fun obtenerEstadisticas(): List<EstadisticaJugador>

    @GET("api/estadisticas/{id}")
    suspend fun obtenerEstadistica(@Path("id") id: Int): EstadisticaJugador

    @POST("api/estadisticas")
    suspend fun crearEstadistica(@Body estadistica: EstadisticaJugador): EstadisticaJugador

    @PUT("api/estadisticas/{id}")
    suspend fun actualizarEstadistica(@Path("id") id: Int, @Body estadistica: EstadisticaJugador): EstadisticaJugador

    @DELETE("api/estadisticas/{id}")
    suspend fun eliminarEstadistica(@Path("id") id: Int)
}