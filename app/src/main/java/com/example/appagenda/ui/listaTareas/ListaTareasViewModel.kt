package com.example.appagenda.ui.listaTareas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.appagenda.Modelo.Tarea.Tarea
import com.example.appagenda.Modelo.Tarea.TareaAdapter
import java.util.Date

class ListaTareasViewModel : ViewModel() {
    private val _listaTareas : List<Tarea> = mutableListOf<Tarea>(
        Tarea("Prueba",Date(), "Descripcion"),
        Tarea("Pruena2", Date(), "Descripcion"),
        Tarea("Pruena2", Date(), "Descripcion"),
        Tarea("Pruena2", Date(), "Descripcion"),
        Tarea("Pruena2", Date(), "Descripcion"),
        Tarea("Pruena2", Date(), "Descripcion")
    )

    val listaTarea = _listaTareas
}