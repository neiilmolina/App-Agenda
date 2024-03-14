package com.example.appagenda.ui.register

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.appagenda.MainActivity
import com.example.appagenda.R
import com.example.appagenda.databinding.FragmentLoginBinding
import com.example.appagenda.databinding.FragmentRegisterBinding
import com.example.appagenda.ui.login.LoginFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private lateinit var auth: FirebaseAuth
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding: FragmentRegisterBinding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnRegistro.setOnClickListener {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                createAccount(email,password)
            }
        }

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val db = Firebase.firestore
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser

                    val nombre = binding.etNombre.text.toString()
                    val edad = binding.etEdad.text.toString().toInt()
                    val map = hashMapOf(
                        "name" to nombre,
                        "age" to edad,
                        "email" to user!!.email.toString()
                    )

                    db.collection("users").document(user.uid)
                        .set(map)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "C AÑADIDOOOOO")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }

                    // acceder al usuario por medio del uid y a sus tareas
                    // db.collection("users").document(user.uid).collection("tareas").get

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = binding.etEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.etEmail.error = "Required."
            valid = false
        } else {
            binding.etEmail.error = null
        }

        val nombre = binding.etNombre.text.toString()
        if (TextUtils.isEmpty(nombre)) {
            binding.etNombre.error = "Required."
            valid = false
        } else {
            binding.etNombre.error = null
        }

        val edad = binding.etEdad.text.toString()
        if (TextUtils.isEmpty(edad)) {
            binding.etEdad.error = "Required."
            valid = false
        } else {
            binding.etEdad.error = null
        }

        val password = binding.etPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.etPassword.error = "Required."
            valid = false
        } else {
            binding.etPassword.error = null
        }

        val repetirPassword = binding.etRepetirPassword.text.toString()
        if (TextUtils.isEmpty(repetirPassword)) {
            binding.etRepetirPassword.error = "Required."
            valid = false
        } else {
            binding.etRepetirPassword.error = null
        }

        // Validar que la contraseña y su repetición coincidan
        if (password != repetirPassword) {
            binding.etRepetirPassword.error = "Passwords don't match."
            valid = false
        }

        return valid
    }

}