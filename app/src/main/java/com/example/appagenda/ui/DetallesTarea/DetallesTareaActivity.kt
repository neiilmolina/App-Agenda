package com.example.appagenda.ui.DetallesTarea

import ListaTareasViewModel
import TareasRespositorio
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.appagenda.Modelo.Tarea.Tarea
import com.example.appagenda.R
import com.example.appagenda.databinding.ActivityDetallesTareaBinding

class DetallesTareaActivity : AppCompatActivity() {

    private var listaTareasViewModel: ListaTareasViewModel ?=null


    private lateinit var binding: ActivityDetallesTareaBinding
    companion object {
        const val POSICION_TAREA= "tarea_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_tarea)
        binding = ActivityDetallesTareaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listaTareasViewModel =
            ViewModelProvider(this).get(ListaTareasViewModel::class.java)

        val posicion: Int = intent.getIntExtra(POSICION_TAREA,-1)
        val tarea = listaTareasViewModel!!.listaTareas[posicion]
        crearUI(tarea)

        binding.btnEditar.setOnClickListener{
            val nuevaTarea = getTarea(tarea.id)
            nuevaTarea.fecha?.let { it1 ->
                TareasRespositorio.actualizarTarea(
                    nuevaTarea.id,
                    nuevaTarea.titulo,
                    nuevaTarea.descripcion,
                    it1
                )
            }
            crearUI(nuevaTarea)
        }
        binding.btnEliminar.setOnClickListener{
            TareasRespositorio.eliminarTarea(tarea.id)
            finish()
        }
    }


    private fun crearUI(tarea: Tarea) {
        binding.etTitulo.setText(tarea.titulo)
        binding.etFecha.setText(tarea.fechaString)
        binding.etDescripcion.setText(tarea.descripcion)
    }

    private fun getTarea (id:String): Tarea {
        val titulo = binding.etTitulo.text.toString()
        val fechaString = binding.etFecha.text.toString()
        val fecha = Tarea.parsearFecha(this,fechaString)
        val descripcion = binding.etDescripcion.text.toString()


        return Tarea(id,titulo,fecha, fechaString, descripcion)
    }


}

