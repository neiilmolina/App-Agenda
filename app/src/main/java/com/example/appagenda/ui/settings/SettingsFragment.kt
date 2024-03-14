package com.example.appagenda.ui.settings

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.appagenda.MainActivity
import com.example.appagenda.databinding.FragmentSettingsBinding
import com.example.appagenda.ui.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding:FragmentSettingsBinding get() = _binding!!

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {


            btnLogout.setOnClickListener {
                signOut()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
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

        }
        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    private fun signOut() {
        auth.signOut()
        MainActivity.listaTareasViewModel.vaciarLista()
//        val intent = Intent(requireContext())

        onStart()
    }

    fun changeTheme(themeMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        requireActivity().recreate() // Recrear la actividad para que el cambio de tema surta efecto
    }

}