package com.example.appagenda.ui.listaTareas

import DetallesTareaActivity
import DetallesTareaActivity.Companion.POSICION_TAREA
import ListaTareasViewModel
import TareasRespositorio
import android.app.Dialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appagenda.Modelo.Tarea.Tarea
import com.example.appagenda.Modelo.Tarea.TareaAdapter
import com.example.appagenda.R
import com.example.appagenda.databinding.FragmentListaTareasBinding
import kotlinx.coroutines.launch
import java.util.Date

class ListaTareasFragment : Fragment() {

    private var listaTareasViewModel: ListaTareasViewModel ?=null

    private var _binding: FragmentListaTareasBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        listaTareasViewModel =
            ViewModelProvider(this).get(ListaTareasViewModel::class.java)

        _binding = FragmentListaTareasBinding.inflate(inflater, container, false)

        // colocar los elementos de la lista
        binding.rvListaTareas.layoutManager = LinearLayoutManager(requireContext())
        // inicializar el adapter
        binding.rvListaTareas.adapter =
            TareaAdapter(listaTareasViewModel!!.obtenerListaTareas()) { position -> navegarDetallesTarea(position) }

        initEvents()
        val root: View = binding.root
        return root
    }

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
                agregarTareaConCoroutines(titulo, descripcion, fecha)
                dialog.dismiss()
            } else {
                // Mostrar un mensaje de error si falta algún dato
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun agregarTareaConCoroutines(titulo: String, descripcion: String, fecha: Date) {
        // Utilizamos viewModelScope.launch para lanzar una coroutine en el contexto del ViewModel
        // Esto asegura que la coroutine sea cancelada automáticamente cuando el ViewModel se destruye
        listaTareasViewModel?.viewModelScope?.launch {
            try {
                // Llamamos a la función asíncrona agregarTarea del repositorio y esperamos su resultado
                TareasRespositorio.agregarTarea(titulo, descripcion, fecha)
            } catch (e: Exception) {
                // Manejar errores, por ejemplo, loggear el error
                e.printStackTrace()
            }
        }
    }




}