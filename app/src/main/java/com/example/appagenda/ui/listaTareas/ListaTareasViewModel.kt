package com.example.appagenda.ui.listaTareas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.appagenda.Modelo.Tarea.Tarea
import com.example.appagenda.Modelo.Tarea.TareaAdapter
import java.util.Date

class ListaTareasViewModel : ViewModel() {
    // inicializar una lista de tareas
    private val _listaTareas : List<Tarea> = mutableListOf<Tarea>(
        Tarea( 1,"Prueba",Date(), "Descripcion"),
        Tarea(2,"Prueba2", Date(), "Descripcion"),
        Tarea(3,"Prueba3", Date(), "Descripcion"),
        Tarea(4,"Prueba4", Date(), "Descripcion"),
        Tarea(5,"Prueba5", Date(), "Descripcion"),
        Tarea(6,"Prueba6", Date(), "Descripcion")
    )

    // lista publica de la lista de tareas
    val listaTarea = _listaTareas

}