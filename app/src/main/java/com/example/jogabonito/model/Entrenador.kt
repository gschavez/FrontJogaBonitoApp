package com.example.jogabonito.model

data class Entrenador(
    val idEntrenador: Int = 0,
    val nombre: String = "",
    val especialidad: String = "",
    val equipo: Equipo = Equipo()
)