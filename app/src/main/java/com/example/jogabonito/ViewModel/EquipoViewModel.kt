package com.example.jogabonito.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jogabonito.Repository.EquipoRepository
import com.example.jogabonito.model.Equipo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EquipoViewModel : ViewModel() {
    private val repository = EquipoRepository()

    val equipos = MutableLiveData<List<Equipo>>(emptyList())
    val error = MutableLiveData<String?>()

    fun obtenerEquipos() {
        viewModelScope.launch {
            try {
                val lista = withContext(Dispatchers.IO) { repository.obtenerEquipos() }
                equipos.postValue(lista)
            } catch (e: Exception) {
                error.postValue("Error al obtener equipos: ${e.message}")
            }
        }
    }

    fun crearEquipo(equipo: Equipo, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repository.crearEquipo(equipo) }
                obtenerEquipos()
                onSuccess()
            } catch (e: Exception) {
                error.postValue("Error al crear equipo: ${e.message}")
            }
        }
    }

    fun actualizarEquipo(id: Int, equipo: Equipo, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repository.actualizarEquipo(id, equipo) }
                obtenerEquipos()
                onSuccess()
            } catch (e: Exception) {
                error.postValue("Error al actualizar equipo: ${e.message}")
            }
        }
    }

    fun eliminarEquipo(id: Int) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repository.eliminarEquipo(id) }
                obtenerEquipos()
            } catch (e: Exception) {
                error.postValue("Error al eliminar equipo: ${e.message}")
            }
        }
    }
}