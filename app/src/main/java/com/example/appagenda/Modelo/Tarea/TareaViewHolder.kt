package com.example.appagenda.Modelo.Tarea

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appagenda.R
import com.example.appagenda.databinding.ItemTareaBinding

class TareaViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    
//    private val binding: ItemTareaBinding?= null

    private val tvTitulo: TextView = view.findViewById(R.id.tvTitulo)
    private val tvFecha: TextView = view.findViewById(R.id.tvFecha)
    fun render(tarea: Tarea){
        tvTitulo.text = tarea.titulo
        tvFecha.text = tarea.fecha.toString()
    }
}