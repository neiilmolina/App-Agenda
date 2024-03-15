package com.example.appagenda.ui.home

import ListaTareasViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.appagenda.MainActivity
import com.example.appagenda.Modelo.Tarea.Tarea
import com.example.appagenda.R
import com.example.appagenda.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import kotlin.random.Random

class HomeFragment : Fragment() {
    private var listaTareasViewModel: ListaTareasViewModel ?=null
    private var _binding: FragmentHomeBinding? = null
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]
        auth = Firebase.auth
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        with(binding) {
            btnGoToRegister.setOnClickListener {
                findNavController().navigate(R.id.vistaRegister)
            }
            if (currentUser != null) {
                btnGoToRegister.isVisible = false
                welcomeText.setText("bienvenido")
                tareasPendientes.setText("Tienes en total " + MainActivity.listaTareasViewModel.obtenerListaTareas().size + " tareas pendientes")
                val tareas = MainActivity.listaTareasViewModel.obtenerListaTareas()

            } else {
                btnGoToRegister.isVisible = true
                welcomeText.setText(R.string.welcome_msg)
                tareasPendientes.setText(R.string.register_msg)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}