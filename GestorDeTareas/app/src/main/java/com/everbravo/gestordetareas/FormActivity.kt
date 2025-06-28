package com.everbravo.gestordetareas

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import com.everbravo.gestordetareas.persistence.dao.TaskDao
import com.everbravo.gestordetareas.utils.SecuredPrefs
import com.everbravo.gestordetareas.utils.ToastManager
import com.everbravo.gestordetareas.utils.Validations
import com.everbravo.gestordetareas.utils.enums.ValidationResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class FormActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var taskDao: TaskDao

    private lateinit var etTaskName: EditText
    private lateinit var etTaskDescription: EditText
    private lateinit var btnSaveTask: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        if (!isUserLoggedIn()) return

        initViews()
        initLocationService()
        initListeners()
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPrefs = SecuredPrefs.getSecurePrefs(applicationContext)
        val loggedIn = sharedPrefs.getBoolean("isLoggedIn", false)
        if (!loggedIn) {
            ToastManager.showMeAToast(this, ValidationResult.SESSION_ERROR)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        return loggedIn
    }

    private fun initViews() {
        etTaskName = findViewById(R.id.etTaskName)
        etTaskDescription = findViewById(R.id.etTaskDescription)
        btnSaveTask = findViewById(R.id.btnSaveTask)
        btnBack = findViewById(R.id.btnBack)
        taskDao = TaskDao(this)
    }

    private fun initLocationService() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun initListeners() {
        btnSaveTask.setOnClickListener {
            if (hasLocationPermission()) {
                saveTaskWithLocation()
            } else {
                requestLocationPermission()
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        ToastManager.showMeAToast(this, ValidationResult.LOCALIZATION_ERROR)
    }

    @SuppressLint("MissingPermission")
    private fun saveTaskWithLocation() {
        val name = etTaskName.text.toString()
        val description = etTaskDescription.text.toString()

        if (!ToastManager.showMeAToast(this, Validations.validateEmpty(name))) return
        if (!ToastManager.showMeAToast(this, Validations.validateEmpty(description))) return

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                val latitude = location?.latitude ?: 0.0
                val longitude = location?.longitude ?: 0.0

                taskDao.insertTask(name, description, latitude, longitude)
                ToastManager.showMeAToast(this, ValidationResult.TASK_SUCCESS)
                finish()
            }
            .addOnFailureListener {
                ToastManager.showMeAToast(this, ValidationResult.TASK_ERROR)
            }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
