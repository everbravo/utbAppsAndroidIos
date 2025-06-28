package com.everbravo.gestordetareas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.everbravo.gestordetareas.persistence.dao.TaskDao
import com.everbravo.gestordetareas.persistence.dao.UserDao
import com.everbravo.gestordetareas.utils.HashUtils
import com.everbravo.gestordetareas.utils.SecuredPrefs
import com.everbravo.gestordetareas.utils.ToastManager
import com.everbravo.gestordetareas.utils.Validations
import com.everbravo.gestordetareas.utils.enums.ValidationResult

class RegisterActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao
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
        userDao = UserDao(this)
    }

    private fun setupListeners() {
        btnRegister.setOnClickListener { handleRegister() }
        btnBackToLogin.setOnClickListener { goToLogin() }
    }

    private fun handleRegister() {
        val username = etUsername.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        if (!ToastManager.showMeAToast(this, Validations.validateEmpty(username))) return
        if (!ToastManager.showMeAToast(this, Validations.validateEmail(email))) return
        if (!ToastManager.showMeAToast(this, Validations.validatePassword(password))) return

        ToastManager.showMeAToast(
            this,
            userDao.insertUser(username, email, password, 1)
        )
        Toast.makeText(this, ValidationResult.USERNAME_WELCOME.description+" $username !", Toast.LENGTH_SHORT).show()
        goToMain()
    }

    private fun goToMain() {
        saveInSharedPrefs()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun saveInSharedPrefs() {
        val sharedPrefs = SecuredPrefs.getSecurePrefs(applicationContext)
        sharedPrefs.edit().putBoolean("isLoggedIn", true).apply()
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
