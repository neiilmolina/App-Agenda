package com.example.appagenda.ui.settings

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.appagenda.MainActivity
import com.example.appagenda.R
import com.example.appagenda.databinding.FragmentSettingsBinding
import com.example.appagenda.ui.DetallesTarea.DetallesTareaActivity
import com.example.appagenda.ui.listaTareas.ListaTareasFragment
import com.example.appagenda.ui.login.LoginFragment
import com.example.appagenda.ui.register.RegisterFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private lateinit var auth: FirebaseAuth
    private val loginFragment = LoginFragment()
    private val registerFragment = RegisterFragment()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding:FragmentSettingsBinding get() = _binding!!



    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if (currentUser != null) {
           binding.btnLogin.isVisible = false
        } else {
            binding.btnLogin.isVisible = true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            btnLogin.setOnClickListener {
                findNavController().navigate(R.id.vistaLog)
            }

            btnViewRegister.setOnClickListener {
                findNavController().navigate(R.id.vistaRegister)
            }

            btnLogout.setOnClickListener {
                signOut()
            }
/*
            btnLogin.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, loginFragment )
                    .addToBackStack(null) // Esto agrega el fragmento actual a la pila de retroceso
                    .commit()
            }
            btnViewRegister.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, registerFragment)
                    .addToBackStack(null) // Esto agrega el fragmento actual a la pila de retroceso
                    .commit()
            }
*/
        }
        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    private fun signOut() {
        auth.signOut()
        onStart()
    }


}