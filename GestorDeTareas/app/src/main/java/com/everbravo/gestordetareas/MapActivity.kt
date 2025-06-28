package com.everbravo.gestordetareas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.everbravo.gestordetareas.utils.SecuredPrefs
import com.everbravo.gestordetareas.utils.ToastManager
import com.everbravo.gestordetareas.utils.enums.ValidationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var name: String = "Ubicación"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isSessionActive()) {
            ToastManager.showMeAToast(this, ValidationResult.SESSION_ERROR)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        setContentView(R.layout.activity_map)
        extractIntentData()
        initMap()
        setupBackButton()
    }

    private fun isSessionActive(): Boolean {
        return SecuredPrefs.checkSession(applicationContext)
    }

    private fun extractIntentData() {
        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)
        name = intent.getStringExtra("name") ?: "Ubicación"
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupBackButton() {
        findViewById<Button>(R.id.btnBackToMain).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val location = LatLng(latitude, longitude)
        googleMap.addMarker(MarkerOptions().position(location).title(name))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
