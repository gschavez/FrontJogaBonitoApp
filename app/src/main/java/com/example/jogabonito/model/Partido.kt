package com.example.jogabonito.model

data class Partido(
    val idPartido: Int = 0,
    val fecha: String = "",         // "2024-09-10"
    val estadio: String = "",
    val equipoLocal: Equipo = Equipo(),
    val equipoVisita: Equipo = Equipo(),
    val golesLocal: Int = 0,
    val golesVisita: Int = 0
)
