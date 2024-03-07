package com.example.appagenda.ui.listaTareas

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.appagenda.Modelo.Tarea.Tarea
import java.util.Date


class ListaTareasViewModel : ViewModel() {
    // inicializar una lista de tareas
    private val _listaTareas = mutableListOf<Tarea>(
        Tarea( 1,"Prueba",Date(), "Descripcion"),
        Tarea(2,"Prueba2", Date(), "Descripcion"),
        Tarea(3,"Prueba3", Date(), "Descripcion"),
        Tarea(4,"Prueba4", Date(), "Descripcion"),
        Tarea(5,"Prueba5", Date(), "Descripcion"),
        Tarea(6,"Prueba6", Date(), "Descripcion")
    )

    // lista publica de la lista de tareas
    val listaTarea = _listaTareas

    /**
     * Metodo el cual se edita una tarea con el id que se requiere
     * @param id recoge el id
     * @param tareaEditada parametro el cual sirve como modelo para la tarea que se va a editar
     */

    public fun editTareas(tareaEditada:Tarea){
        val tareaIndex: Tarea? = _listaTareas.find { tarea -> tarea.id == tareaEditada.id }

        if (tareaIndex != null) {
            tareaIndex.titulo = tareaEditada.titulo
            tareaIndex.fecha = tareaEditada.fecha
            tareaIndex.descripcion = tareaEditada.descripcion
        }
    }

    /**
     * Metodo el cual añade una nueva tarea en la lista de tareas
     * @param tarea modelo de tarea para añadir a la lista
     */
    public fun addTareas(tarea: Tarea){
        val add = _listaTareas.add(tarea)
        var mensaje = "Error al añadir la tarea"
        if(add) mensaje = "Tarea añadida"

//        val toast = Toast.makeText(context, mensaje, Toast.LENGTH_SHORT)
//        toast.show()
    }
}