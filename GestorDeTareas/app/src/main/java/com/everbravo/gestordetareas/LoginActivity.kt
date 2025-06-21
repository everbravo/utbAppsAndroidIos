package com.everbravo.gestordetareas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.everbravo.gestordetareas.utils.HashUtils
import com.everbravo.gestordetareas.utils.SecuredPrefs

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
        initListeners()
    }

    private fun initViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnGoToRegister)
    }

    private fun initListeners() {
        btnLogin.setOnClickListener { handleLogin() }
        btnRegister.setOnClickListener { goToRegister() }
    }

    private fun handleLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (!validateInputs(email, password)) return

        val sharedPrefs = SecuredPrefs.getSecurePrefs(applicationContext)
        val savedEmail = sharedPrefs.getString("email", null)
        val savedPassword = sharedPrefs.getString("password", null)
        val hashedInput = HashUtils.sha256(password)

        if (email == savedEmail && hashedInput == savedPassword) {
            sharedPrefs.edit().putBoolean("isLoggedIn", true).apply()
            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
            goToMain()
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        return if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa todos los campos", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun goToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
