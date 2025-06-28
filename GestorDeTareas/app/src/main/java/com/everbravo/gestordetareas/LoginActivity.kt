package com.everbravo.gestordetareas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.everbravo.gestordetareas.persistence.dao.UserDao
import com.everbravo.gestordetareas.persistence.domain.User
import com.everbravo.gestordetareas.utils.HashUtils
import com.everbravo.gestordetareas.utils.SecuredPrefs
import com.everbravo.gestordetareas.utils.ToastManager
import com.everbravo.gestordetareas.utils.Validations
import com.everbravo.gestordetareas.utils.enums.ValidationResult

class LoginActivity : AppCompatActivity() {

    private lateinit var userDao: UserDao
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
        userDao = UserDao(this)
    }

    private fun initListeners() {
        btnLogin.setOnClickListener { handleLogin() }
        btnRegister.setOnClickListener { goToRegister() }
    }

    private fun handleLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (!ToastManager.showMeAToast(this, Validations.validateEmail(email))) return
        if (!ToastManager.showMeAToast(this, Validations.validatePassword(password))) return

        val sharedPrefs = SecuredPrefs.getSecurePrefs(applicationContext)

        val user: User? = userDao.getUser(email, password)

        if (user != null) {
            sharedPrefs.edit().putBoolean("isLoggedIn", true).apply()
            Toast.makeText(this, "Bienvenido ${user.name}", Toast.LENGTH_SHORT).show()
            goToMain()
        } else {
            ToastManager.showMeAToast(this, ValidationResult.USER_ERROR)
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
