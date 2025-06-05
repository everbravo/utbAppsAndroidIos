package com.everbravo.gestordetareas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.ComponentActivity
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

    /** Adapter to bind the task list to the ListView. */
    private lateinit var adapter: ArrayAdapter<String>

    /** Mutable list of task descriptions to be shown in the UI. */
    private val taskList = mutableListOf<String>()

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

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, taskList)
        listView.adapter = adapter

        btnAddTask.setOnClickListener {
            startActivity(Intent(this, FormActivity::class.java))
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
        taskList.clear()
        val sharedPreferences = getSharedPreferences("Tasks", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("task_list", "[]")
        val jsonArray = JSONArray(json)

        for (i in 0 until jsonArray.length()) {
            val task = jsonArray.getJSONObject(i)
            val name = task.getString("name")
            val description = task.getString("description")
            taskList.add("Title: $name\nSummary: $description")
        }

        adapter.notifyDataSetChanged()
    }

    /**
     * Called when the activity is no longer visible to the user.
     * This method clears the saved task list from SharedPreferences
     * to ensure that the task list is deleted when the activity stops.
     */
    override fun onStop() {
        super.onStop()
        val sharedPreferences = getSharedPreferences("Tasks", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("task_list").apply()
    }

}
