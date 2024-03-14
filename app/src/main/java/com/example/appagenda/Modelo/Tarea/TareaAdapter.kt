package com.example.appagenda.Modelo.Tarea

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appagenda.R

class TareaAdapter(var tareas: List<Tarea>, private val onItemSelected: (Int) -> Unit): RecyclerView.Adapter<TareaViewHolder>() {

    // metodo donde pinta la lista en la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarea, parent, false)
        return TareaViewHolder(view)
    }

    // devuelve el nº de elementos de la lista
    override fun getItemCount()= tareas.size

    /**
     * renderiza cada view holder inicializando con
     * la posicion un elemento de la lista
     * y una función lambda para hacer el evento
     * */
    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        holder.render(tareas[position], onItemSelected)
    }

    fun actualizarListaTareas(nuevasTareas: List<Tarea>) {
        tareas = nuevasTareas
        notifyDataSetChanged()
    }
}