package com.everbravo.primeraaplicacion

import android.os.Bundle
import androidx.activity.ComponentActivity

/**
 * SecondActivity is the secondary screen of the application.
 * It is launched from MainActivity and displays the layout defined in activity_second.xml.
 *
 * @author Ever Bravo
 * @since 31/05/2025
 * @version 1.0
 */
class SecondActivity : ComponentActivity() {

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If non-null, this activity is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }
}
