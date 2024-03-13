package com.example.appagenda.ui.DetallesTarea

import ListaTareasViewModel
import TareasRespositorio
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appagenda.MainActivity
import com.example.appagenda.Modelo.Tarea.Tarea
import com.example.appagenda.databinding.ActivityDetallesTareaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetallesTareaActivity : AppCompatActivity() {

    private lateinit var listaTareasViewModel: ListaTareasViewModel
    private lateinit var binding: ActivityDetallesTareaBinding
    private var tareaId: String = ""

    companion object {
        const val POSICION_TAREA = "tarea_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallesTareaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listaTareasViewModel = MainActivity.listaTareasViewModel

        val posicion: Int = intent.getIntExtra(POSICION_TAREA, -1)
        if (posicion != -1 ) {
            val listaTarea = listaTareasViewModel.obtenerListaTareas()
            val tarea = listaTarea[posicion]
            tareaId = tarea.id
            crearUI(tarea)
        }


        binding.btnEditar.setOnClickListener {
            val nuevaTarea = getTarea(tareaId)
            if (nuevaTarea != null) {
                nuevaTarea.fecha?.let { it1 ->
                    GlobalScope.launch(Dispatchers.Main) {
                        try {
                            TareasRespositorio.actualizarTarea(
                                nuevaTarea.id,
                                nuevaTarea.titulo,
                                nuevaTarea.descripcion,
                                it1
                            )
                            if (nuevaTarea != null) {
                                crearUI(nuevaTarea)
                            }
                        } catch (e: Exception) {
                            // Handle error
                        }
                    }
                }
            }
        }

        binding.btnEliminar.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    TareasRespositorio.eliminarTarea(tareaId)
                    finish()
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }

    private fun crearUI(tarea: Tarea) {
        binding.etTitulo.setText(tarea.titulo)
        binding.etFecha.setText(tarea.fechaString)
        binding.etDescripcion.setText(tarea.descripcion)
    }

    private fun getTarea(id: String): Tarea? {
        val titulo = binding.etTitulo.text.toString()
        val fechaString = binding.etFecha.text.toString()
        val fecha = Tarea.parsearFecha(this, fechaString)
        val descripcion = binding.etDescripcion.text.toString()

        return fecha?.let { Tarea(id, titulo, it, fechaString, descripcion) }
    }
}