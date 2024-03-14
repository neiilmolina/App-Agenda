package com.example.appagenda.ui.DetallesTarea

import ListaTareasViewModel
import TareasRespositorio
import TareasRespositorio.user
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.appagenda.MainActivity
import com.example.appagenda.Modelo.Tarea.Tarea
import com.example.appagenda.databinding.ActivityDetallesTareaBinding
import com.example.appagenda.ui.listaTareas.ListaTareasFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetallesTareaActivity : AppCompatActivity() {

    private lateinit var listaTareasViewModel: ListaTareasViewModel
    private lateinit var binding: ActivityDetallesTareaBinding
    private var tareaId: String = ""
    var auth: FirebaseAuth = Firebase.auth
    val user = auth.currentUser

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
                    GlobalScope.launch(Dispatchers.Main) {
                        try {
                            if (nuevaTarea != null) {
                                val tareaActual = listaTareasViewModel.obtenerListaTareas().find { t -> t.id == nuevaTarea.id }

                                if(tareaActual != nuevaTarea){
                                    crearUI(nuevaTarea)
                                    TareasRespositorio.actualizarTarea(
                                        nuevaTarea.id,
                                        nuevaTarea.titulo,
                                        nuevaTarea.descripcion,
                                        nuevaTarea.fecha
                                    )
                                    listaTareasViewModel.editTarea(nuevaTarea)
                                    ListaTareasFragment.tareaAdapter.actualizarListaTareas(listaTareasViewModel.obtenerListaTareas())
                                    mostrarToast("Cambios realizados")
                                } else {
                                    mostrarToast("No se ven cambios")
                                }
                            }
                        } catch (e: Exception) {
                            // Handle error
                        }
                    }
            }
        }

        binding.btnEliminar.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    TareasRespositorio.eliminarTarea(tareaId)
                    listaTareasViewModel.eliminarTarea(tareaId)
                    ListaTareasFragment.tareaAdapter.actualizarListaTareas(listaTareasViewModel.obtenerListaTareas())
                    mostrarToast("Tarea eliminada")
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
        val idUsuario = user?.uid

        return Tarea(id, titulo, fecha!!, fechaString, descripcion, idUsuario!!)
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this@DetallesTareaActivity, mensaje, Toast.LENGTH_SHORT).show()
    }

}