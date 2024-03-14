package com.example.appagenda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appagenda.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Si hay un usuario logeado, navega a MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Si no hay un usuario logeado, navega a LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}