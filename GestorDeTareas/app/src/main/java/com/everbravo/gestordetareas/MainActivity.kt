package com.everbravo.gestordetareas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.activity.ComponentActivity
import com.everbravo.gestordetareas.adapters.TaskAdapter
import com.everbravo.gestordetareas.persistence.dao.TaskDao
import org.json.JSONArray

/**
 * MainActivity is the entry point of the Task Manager app.
 * It displays a list of saved tasks and provides a button to add new ones.
 *
 * Tasks are stored in SharedPreferences in JSON format and loaded every time the activity resumes.
 *
 * @author Ever Bravo
 * @version 1.0
 */
class MainActivity : ComponentActivity() {

    /** ListView used to display the list of tasks. */
    private lateinit var listView: ListView

    private lateinit var tasksJsonArray: JSONArray

    /**
     * Called when the activity is first created.
     * Sets up the layout, initializes the ListView and adapter,
     * and configures the Add Task button to launch FormActivity.
     *
     * @param savedInstanceState the previously saved state of the activity, if any
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listTasks)
        val btnAddTask = findViewById<Button>(R.id.btnAddTask)
        val btnEndSession = findViewById<Button>(R.id.btnEndSession)

        // Ir a formulario
        btnAddTask.setOnClickListener {
            startActivity(Intent(this, FormActivity::class.java))
        }

        // Volver a login
        btnEndSession.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }


    /**
     * Called when the activity becomes visible to the user.
     * Reloads the task list from SharedPreferences.
     */
    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    /**
     * Loads the list of tasks from SharedPreferences.
     * Clears the current list, reads the JSON array, and updates the ListView.
     */
    private fun loadTasks() {
        val taskDao = TaskDao(this)
        val tasks = taskDao.getAllTasks()

        val adapter = TaskAdapter(this, JSONArray().apply {
            tasks.forEach {
                put(org.json.JSONObject().apply {
                    put("name", it.name)
                    put("description", it.description)
                    put("latitude", it.latitude)
                    put("longitude", it.longitude)
                })
            }
        })

        listView.adapter = adapter
    }

}
