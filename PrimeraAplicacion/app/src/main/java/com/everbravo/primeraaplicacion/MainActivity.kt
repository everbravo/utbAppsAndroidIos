package com.everbravo.primeraaplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

/**
 * MainActivity is the entry point of the application.
 * It displays the main layout and handles navigation to SecondActivity.
 *
 * @author Ever Bravo
 * @since 31/05/2025
 * @version 1.0
 * */
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     * previously being shut down then this Bundle contains the data it most
     * recently supplied. Otherwise, it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnNavigate = findViewById<Button>(R.id.btnNavigate)
        btnNavigate.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}
