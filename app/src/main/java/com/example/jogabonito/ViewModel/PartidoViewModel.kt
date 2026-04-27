package com.example.jogabonito.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jogabonito.Repository.PartidoRepository
import com.example.jogabonito.model.Partido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PartidoViewModel : ViewModel() {
    private val repository = PartidoRepository()

    val partidos = MutableLiveData<List<Partido>>(emptyList())
    val error = MutableLiveData<String?>()

    fun obtenerPartidos() {
        viewModelScope.launch {
            try {
                val lista = withContext(Dispatchers.IO) { repository.obtenerPartidos() }
                partidos.postValue(lista)
            } catch (e: Exception) {
                error.postValue("Error al obtener partidos: ${e.message}")
            }
        }
    }

    fun crearPartido(partido: Partido, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repository.crearPartido(partido) }
                obtenerPartidos()
                onSuccess()
            } catch (e: Exception) {
                error.postValue("Error al crear partido: ${e.message}")
            }
        }
    }

    fun eliminarPartido(id: Int) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repository.eliminarPartido(id) }
                obtenerPartidos()
            } catch (e: Exception) {
                error.postValue("Error al eliminar partido: ${e.message}")
            }
        }
    }
}