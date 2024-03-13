package com.example.appagenda.ui.listaTareas

import ListaTareasViewModel
import TareasRespositorio
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appagenda.MainActivity
import com.example.appagenda.Modelo.Tarea.Tarea
import com.example.appagenda.Modelo.Tarea.TareaAdapter
import com.example.appagenda.R
import com.example.appagenda.databinding.FragmentListaTareasBinding
import com.example.appagenda.ui.DetallesTarea.DetallesTareaActivity
import com.example.appagenda.ui.DetallesTarea.DetallesTareaActivity.Companion.POSICION_TAREA
import kotlinx.coroutines.launch
import java.util.Date

class ListaTareasFragment : Fragment() {

    private var listaTareasViewModel: ListaTareasViewModel ?=null

    private var _binding: FragmentListaTareasBinding? = null

    private lateinit var tareaAdapter: TareaAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        listaTareasViewModel = MainActivity.listaTareasViewModel

        _binding = FragmentListaTareasBinding.inflate(inflater, container, false)

        // colocar los elementos de la lista
        binding.rvListaTareas.layoutManager = LinearLayoutManager(requireContext())

        // inicializar el adapter
        tareaAdapter =
            TareaAdapter(listaTareasViewModel!!.obtenerListaTareas()) { position -> navegarDetallesTarea(position) }

        binding.rvListaTareas.adapter = tareaAdapter

        initEvents()
        val root: View = binding.root
        return root
    }
//    override fun onResume() {
//        super.onResume()
//        // Recargar la lista de tareas cada vez que el Fragmento se muestre
//        listaTareas = listaTareasViewModel!!.obtenerListaTareas()
//        tareaAdapter.actualizarListaTareas(listaTareas)
//    }
    private fun initEvents(){
        binding.btnAdd.setOnClickListener{showDialog()}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     *
     * metodo que sirve para navegar en los detalles
     * de la tarea
     *
     *  */
    private fun navegarDetallesTarea(posicion: Int) {
        val intent = Intent(requireContext(), DetallesTareaActivity::class.java)
        intent.putExtra(POSICION_TAREA, posicion)
        startActivity(intent)
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_tarea)
        val btnDialogTarea: Button = dialog.findViewById(R.id.btnDialogTarea)
        val etTitulo: EditText = dialog.findViewById(R.id.etTitulo)
        val etFecha: EditText = dialog.findViewById(R.id.etFecha)
        val etDescripcion: EditText = dialog.findViewById(R.id.etDescripcion)

        btnDialogTarea.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val descripcion = etDescripcion.text.toString()
            val fechaString = etFecha.text.toString()
            val fecha = Tarea.parsearFecha(requireContext(), fechaString)

            if (titulo.isNotEmpty() && descripcion.isNotEmpty() && fecha != null) {
                agregarTareaConCoroutines(titulo, descripcion, fecha, listaTareasViewModel)
                dialog.dismiss()
            } else {
                // Mostrar un mensaje de error si falta algún dato
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun agregarTareaConCoroutines(
        titulo: String,
        descripcion: String,
        fecha: Date,
        listaTareasViewModel: ListaTareasViewModel?
    ) {
        // Utilizamos viewModelScope.launch para lanzar una coroutine en el contexto del ViewModel
        // Esto asegura que la coroutine sea cancelada automáticamente cuando el ViewModel se destruye
        this.listaTareasViewModel?.viewModelScope?.launch {
            try {
                // Llamamos a la función asíncrona agregarTarea del repositorio y esperamos su resultado
                TareasRespositorio.agregarTarea(titulo, descripcion, fecha, listaTareasViewModel)
                if (listaTareasViewModel != null) {
                    tareaAdapter.actualizarListaTareas(listaTareasViewModel.obtenerListaTareas())
                }

            } catch (e: Exception) {
                // Manejar errores, por ejemplo, loggear el error
                e.printStackTrace()
            }
        }
    }
}

