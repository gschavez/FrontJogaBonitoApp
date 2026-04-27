package com.example.jogabonito.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jogabonito.Repository.EntrenadorRepository
import com.example.jogabonito.model.Entrenador
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EntrenadorViewModel : ViewModel() {
    private val repository = EntrenadorRepository()

    val entrenadores = MutableLiveData<List<Entrenador>>(emptyList())
    val error = MutableLiveData<String?>()

    fun obtenerEntrenadores() {
        viewModelScope.launch {
            try {
                val lista = withContext(Dispatchers.IO) { repository.obtenerEntrenadores() }
                entrenadores.postValue(lista)
            } catch (e: Exception) {
                error.postValue("Error al obtener entrenadores: ${e.message}")
            }
        }
    }

    fun crearEntrenador(entrenador: Entrenador, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repository.crearEntrenador(entrenador) }
                obtenerEntrenadores()
                onSuccess()
            } catch (e: Exception) {
                error.postValue("Error al crear entrenador: ${e.message}")
            }
        }
    }

    fun eliminarEntrenador(id: Int) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repository.eliminarEntrenador(id) }
                obtenerEntrenadores()
            } catch (e: Exception) {
                error.postValue("Error al eliminar entrenador: ${e.message}")
            }
        }
    }
}