package com.everbravo.gestordetareas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.everbravo.gestordetareas.adapters.TaskAdapter
import com.everbravo.gestordetareas.persistence.dao.TaskDao
import com.everbravo.gestordetareas.utils.SecuredPrefs
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : ComponentActivity() {

    private lateinit var listView: ListView
    private lateinit var btnAddTask: Button
    private lateinit var btnEndSession: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isSessionActive()) {
            redirectToLogin("Sesión no válida. Inicia sesión nuevamente.")
            return
        }

        initViews()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        if (!isSessionActive()) {
            redirectToLogin("Sesión finalizada. Inicia sesión nuevamente.")
        } else {
            loadTasks()
        }
    }

    private fun initViews() {
        listView = findViewById(R.id.listTasks)
        btnAddTask = findViewById(R.id.btnAddTask)
        btnEndSession = findViewById(R.id.btnEndSession)
    }

    private fun initListeners() {
        btnAddTask.setOnClickListener {
            startActivity(Intent(this, FormActivity::class.java))
        }

        btnEndSession.setOnClickListener {
            SecuredPrefs.getSecurePrefs(applicationContext)
                .edit()
                .putBoolean("isLoggedIn", false)
                .apply()
            redirectToLogin("Sesión cerrada correctamente.")
        }
    }

    private fun isSessionActive(): Boolean {
        return SecuredPrefs.checkSession(applicationContext)
    }

    private fun redirectToLogin(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun loadTasks() {
        val taskDao = TaskDao(this)
        val tasks = taskDao.getAllTasks()

        val jsonArray = JSONArray().apply {
            tasks.forEach {
                put(JSONObject().apply {
                    put("name", it.name)
                    put("description", it.description)
                    put("latitude", it.latitude)
                    put("longitude", it.longitude)
                })
            }
        }

        listView.adapter = TaskAdapter(this, jsonArray)
    }
}
