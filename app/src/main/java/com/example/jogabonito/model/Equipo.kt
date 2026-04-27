package com.example.jogabonito.model

data class Equipo(
    val idEquipo: Int = 0,
    val nombre: String = "",
    val ciudad: String = "",
    val fundacion: String = ""  // LocalDate llega como String desde JSON, ej: "2001-06-15"
)