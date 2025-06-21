package com.everbravo.gestordetareas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.everbravo.gestordetareas.utils.HashUtils
import com.everbravo.gestordetareas.utils.SecuredPrefs

class RegisterActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnBackToLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initViews()
        setupListeners()
    }

    private fun initViews() {
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnBackToLogin = findViewById(R.id.btnGoToLogin)
    }

    private fun setupListeners() {
        btnRegister.setOnClickListener { handleRegister() }
        btnBackToLogin.setOnClickListener { goToLogin() }
    }

    private fun handleRegister() {
        val username = etUsername.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val hashedPassword = HashUtils.sha256(password)
        saveCredentials(email, hashedPassword)
        Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
        goToMain()
    }

    private fun saveCredentials(email: String, hashedPassword: String) {
        val sharedPrefs = SecuredPrefs.getSecurePrefs(applicationContext)
        with(sharedPrefs.edit()) {
            putString("email", email)
            putString("password", hashedPassword)
            putBoolean("isLoggedIn", true)
            apply()
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
