package com.example.jogabonito.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jogabonito.Repository.EstadisticaRepository
import com.example.jogabonito.model.EstadisticaJugador
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EstadisticaViewModel : ViewModel() {
    private val repository = EstadisticaRepository()

    val estadisticas = MutableLiveData<List<EstadisticaJugador>>(emptyList())
    val error = MutableLiveData<String?>()

    fun obtenerEstadisticas() {
        viewModelScope.launch {
            try {
                val lista = withContext(Dispatchers.IO) { repository.obtenerEstadisticas() }
                estadisticas.postValue(lista)
            } catch (e: Exception) {
                error.postValue("Error al obtener estadísticas: ${e.message}")
            }
        }
    }

    fun crearEstadistica(e: EstadisticaJugador, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repository.crearEstadistica(e) }
                obtenerEstadisticas()
                onSuccess()
            } catch (e: Exception) {
                error.postValue("Error al crear estadística: ${e.message}")
            }
        }
    }

    fun eliminarEstadistica(id: Int) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repository.eliminarEstadistica(id) }
                obtenerEstadisticas()
            } catch (e: Exception) {
                error.postValue("Error al eliminar estadística: ${e.message}")
            }
        }
    }
}