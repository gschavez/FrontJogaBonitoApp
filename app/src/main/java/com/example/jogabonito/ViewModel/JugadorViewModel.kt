package com.example.jogabonito.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jogabonito.Repository.JugadorRepository
import com.example.jogabonito.model.Jugador
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JugadorViewModel : ViewModel() {
    private val repository = JugadorRepository()

    val jugadores = MutableLiveData<List<Jugador>>(emptyList())
    val error = MutableLiveData<String?>()

    fun obtenerJugadores() {
        viewModelScope.launch {
            try {
                val lista = withContext(Dispatchers.IO) { repository.obtenerJugadores() }
                jugadores.postValue(lista)
            } catch (e: Exception) {
                error.postValue("Error al obtener jugadores: ${e.message}")
            }
        }
    }

    fun crearJugador(jugador: Jugador, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repository.crearJugador(jugador) }
                obtenerJugadores()
                onSuccess()
            } catch (e: Exception) {
                error.postValue("Error al crear jugador: ${e.message}")
            }
        }
    }

    fun actualizarJugador(id: Int, jugador: Jugador, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repository.actualizarJugador(id, jugador) }
                obtenerJugadores()
                onSuccess()
            } catch (e: Exception) {
                error.postValue("Error al actualizar jugador: ${e.message}")
            }
        }
    }

    fun eliminarJugador(id: Int) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repository.eliminarJugador(id) }
                obtenerJugadores()
            } catch (e: Exception) {
                error.postValue("Error al eliminar jugador: ${e.message}")
            }
        }
    }
}