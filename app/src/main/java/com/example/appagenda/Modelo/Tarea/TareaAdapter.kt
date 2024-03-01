package com.example.appagenda.Modelo.Tarea

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appagenda.R
import com.example.appagenda.databinding.ItemTareaBinding

class TareaAdapter(var tasks: List<Tarea>): RecyclerView.Adapter<TareaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarea, parent, false)
        return TareaViewHolder(view)
    }

    override fun getItemCount()= tasks.size

    // renderiza cada view holder
    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        holder.render(tasks[position])
    }
}