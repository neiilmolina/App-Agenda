package com.example.appagenda.ui.listaTareas

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.appagenda.R
import com.example.appagenda.databinding.FragmentHomeBinding
import com.example.appagenda.databinding.FragmentListaTareasBinding
import com.example.appagenda.ui.home.HomeViewModel

class ListaTareasFragment  : Fragment() {

    private var _binding: FragmentListaTareasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val listaTareasViewModel =
            ViewModelProvider(this).get(ListaTareasViewModel::class.java)

        _binding = FragmentListaTareasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}