package com.example.appagenda.Modelo.Tarea

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.appagenda.databinding.ItemTareaBinding

class TareaViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    // referencia de la vista
    private val binding: ItemTareaBinding = ItemTareaBinding.bind(view)

    /**
     * funcion donde se realiza el item
     * tambien se inicializa el evento
     */
    fun render(tarea: Tarea, onItemSelected: (Int) -> Unit){
        binding.tvTitulo.text = tarea.titulo
        binding.tvFecha.text = tarea.fechaString
        binding.root.setOnClickListener{ onItemSelected(adapterPosition) }
    }
}