package com.example.appagenda.ui.DetallesTarea

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.appagenda.Modelo.Tarea.Tarea
import com.example.appagenda.R
import com.example.appagenda.databinding.ActivityDetallesTareaBinding
import com.example.appagenda.ui.listaTareas.ListaTareasViewModel

class DetallesTareaActivity : AppCompatActivity() {

    private var listaTareasViewModel: ListaTareasViewModel ?=null


    private lateinit var binding: ActivityDetallesTareaBinding
    companion object {
        const val TAREA_ID= "tarea_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_tarea)
        binding = ActivityDetallesTareaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listaTareasViewModel =
            ViewModelProvider(this).get(ListaTareasViewModel::class.java)

        val posicion: Int = intent.getIntExtra(TAREA_ID,-1)
        val tarea = listaTareasViewModel!!.listaTarea[posicion]
        crearUI(tarea)

    }

    private fun crearUI(tarea:Tarea){
        binding.tvTitulo.text = tarea.titulo
        binding.tvFecha.text = tarea.fecha.toString()
        binding.tvDescripcion.text = tarea.descripcion
    }
}

