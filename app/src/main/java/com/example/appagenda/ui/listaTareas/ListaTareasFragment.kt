package com.example.appagenda.ui.listaTareas

import android.app.Dialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appagenda.Modelo.Tarea.Tarea
import com.example.appagenda.Modelo.Tarea.TareaAdapter
import com.example.appagenda.R
import com.example.appagenda.databinding.FragmentListaTareasBinding
import com.example.appagenda.ui.DetallesTarea.DetallesTareaActivity
import com.example.appagenda.ui.DetallesTarea.DetallesTareaActivity.Companion.TAREA_ID

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
            TareaAdapter(listaTareasViewModel!!.listaTarea) { position -> navegarDetallesTarea(position) }

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
        intent.putExtra(TAREA_ID, posicion)
        startActivity(intent)
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_tarea)
        val btnDialogTarea: Button = dialog.findViewById(R.id.btnDialogTarea)
        val etTitulo: EditText = dialog.findViewById(R.id.etTitulo)
        val etFecha: EditText = dialog.findViewById(R.id.etFecha)
        val etDescripcion: EditText = dialog.findViewById(R.id.etDescripcion)
        var id: Int = 1

        btnDialogTarea.text = "Añadir"
        if (listaTareasViewModel?.listaTarea?.size!! > 0) {
            id = listaTareasViewModel!!.listaTarea.last().id + 1
        }

        btnDialogTarea.setOnClickListener {
            // Crear la tarea con los valores actuales de los EditText
            val tarea = Tarea(id, etTitulo.text.toString(), null, etDescripcion.text.toString())

            // Añadir la tarea al ViewModel
            listaTareasViewModel!!.addTarea(tarea)

            // Notificar al adaptador que se ha añadido una nueva tarea
            binding.rvListaTareas.adapter?.notifyDataSetChanged()

            // Cerrar el diálogo
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            // Refrescar el RecyclerView después de cerrar el diálogo
            binding.rvListaTareas.adapter?.notifyItemInserted(listaTareasViewModel!!.listaTarea.size - 1)
        }

        dialog.show()
    }

}