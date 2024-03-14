package com.example.appagenda.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.appagenda.MainActivity
import com.example.appagenda.R
import com.example.appagenda.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = Firebase.firestore

        binding.btnRegistro.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val nombre = binding.etNombre.text.toString()
            val edad = binding.etEdad.text.toString()

            createAccount(email, password, nombre, edad)
        }
    }

    private fun createAccount(email: String, password: String, nombre: String, edad: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    val map = hashMapOf(
                        "name" to nombre,
                        "age" to edad.toInt(),
                        "email" to user?.email.toString()
                    )

                    db.collection("users").document(user!!.uid)
                        .set(map)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${user.uid}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                    // Assuming this navigation leads to HomeActivity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        applicationContext,
                        "Error en el registro",
                        Toast.LENGTH_SHORT
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

    companion object {
        private const val TAG = "RegisterActivity"
    }
}