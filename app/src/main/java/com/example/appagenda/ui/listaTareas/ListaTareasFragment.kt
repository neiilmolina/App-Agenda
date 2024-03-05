package com.example.appagenda.ui.listaTareas

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appagenda.Modelo.Tarea.TareaAdapter
import com.example.appagenda.databinding.FragmentListaTareasBinding
import com.example.appagenda.ui.DetallesTarea.DetallesTareaActivity
import com.example.appagenda.ui.DetallesTarea.DetallesTareaActivity.Companion.TAREA_ID

class ListaTareasFragment : Fragment() {


    private var _binding: FragmentListaTareasBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val listaTareasViewModel =
            ViewModelProvider(this).get(ListaTareasViewModel::class.java)

        _binding = FragmentListaTareasBinding.inflate(inflater, container, false)

        // colocar los elementos de la lista
        binding.rvListaTareas.layoutManager = LinearLayoutManager(requireContext())
        // inicializar el adapter
        binding.rvListaTareas.adapter =
            TareaAdapter(listaTareasViewModel.listaTarea) { position -> navegarDetallesTarea(position) }

        initEvents()
        val root: View = binding.root
        return root
    }

    private fun initEvents(){
        binding.btnAdd.setOnClickListener{navegarDetallesTarea(1)}
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
        intent.putExtra(TAREA_ID, posicion)
        startActivity(intent)
    }

}