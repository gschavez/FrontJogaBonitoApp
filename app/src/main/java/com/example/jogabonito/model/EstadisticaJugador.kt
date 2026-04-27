package com.example.jogabonito.model

data class EstadisticaJugador (
    val idEstadistica: Int = 0,
    val jugador: Jugador = Jugador(),
    val partido: Partido = Partido(),
    val minutosJugados: Int = 0,
    val goles: Int = 0,
    val asistencias: Int = 0,
    val tarjetasAmarillas: Int = 0,
    val tarjetasRojas: Int = 0

    )