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
import androidx.appcompat.app.AppCompatDelegate
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
import com.google.firebase.firestore.firestore

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
            binding.btnLogout.isVisible = true
            binding.tvRegister.isVisible = false
            binding.loginQuestion.isVisible = false
            binding.newEdad.isVisible = true
            binding.newName.isVisible = true
            binding.pedirEdad2.isVisible = true
            binding.pedirNombre2.isVisible = true
            binding.btnActualizar.isVisible = true
            binding.btnViewRegister.isVisible = false

        } else {
            binding.btnLogin.isVisible = true
            binding.btnLogout.isVisible = false
            binding.tvRegister.isVisible = true
            binding.loginQuestion.isVisible = true
            binding.newEdad.isVisible = false
            binding.newName.isVisible = false
            binding.pedirEdad2.isVisible = false
            binding.pedirNombre2.isVisible = false
            binding.btnActualizar.isVisible = false
            binding.btnViewRegister.isVisible = true
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
            switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
                val currentNightMode = AppCompatDelegate.getDefaultNightMode()
                val newNightMode = if (isChecked) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }

                // Verificar si el nuevo modo de tema es diferente al modo de tema actual
                if (currentNightMode != newNightMode) {
                    // Cambiar el modo de tema
                    AppCompatDelegate.setDefaultNightMode(newNightMode)
                    // Recrear la actividad para que el cambio de tema surta efecto
                    requireActivity().recreate()
                }
            }

            btnActualizar.setOnClickListener {
                val db = Firebase.firestore
                val user = auth.currentUser
                val nombre = binding.newName.text.toString()
                val edad = binding.newEdad.text.toString().toInt()
                val map = hashMapOf<String, Any>(
                    "name" to nombre,
                    "age" to edad,
                )

                if (user != null) {
                    db.collection("users").document(user.uid)
                        .update(map as Map<String, Any>)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "C AÑADIDOOOOO")
                            Toast.makeText(
                                context,
                                "Datos actualizados correctamente",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                }
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

    fun changeTheme(themeMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        requireActivity().recreate() // Recrear la actividad para que el cambio de tema surta efecto
    }

}