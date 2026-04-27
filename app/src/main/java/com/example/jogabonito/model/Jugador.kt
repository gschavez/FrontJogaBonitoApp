package com.example.jogabonito.model

data class Jugador (
    val idJugador: Int = 0,
    val nombre: String = "",
    val posicion: String = "",
    val dorsal: Int = 0,
    val fechaNac: String = "",  // "2000-03-21"
    val nacionalidad: String = "",
    val equipo: Equipo = Equipo()
)

