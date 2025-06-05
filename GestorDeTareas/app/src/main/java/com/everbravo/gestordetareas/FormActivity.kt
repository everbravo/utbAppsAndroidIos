package com.everbravo.gestordetareas

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import org.json.JSONArray
import org.json.JSONObject

/**
 * FormActivity allows the user to input and save a new task.
 * It includes fields for the task name and description, and buttons to save the task or go back.
 *
 * The task is stored in SharedPreferences as a JSON array. Each new task is appended to the existing list.
 *
 * @author Ever Bravo
 * @version 1.0
 */
class FormActivity : ComponentActivity() {

    /**
     * Called when the activity is starting. Initializes the layout, input fields,
     * and buttons for saving or canceling task creation.
     *
     * @param savedInstanceState the previously saved state of the activity, if any
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val etTaskName = findViewById<EditText>(R.id.etTaskName)
        val etTaskDescription = findViewById<EditText>(R.id.etTaskDescription)
        val btnSaveTask = findViewById<Button>(R.id.btnSaveTask)
        val btnBack = findViewById<Button>(R.id.btnBack)

        val sharedPreferences: SharedPreferences = getSharedPreferences("Tasks", Context.MODE_PRIVATE)

        /**
         * Saves the entered task to SharedPreferences as a JSON object.
         * Appends the new task to the existing task list and finishes the activity.
         */
        btnSaveTask.setOnClickListener {
            val name = etTaskName.text.toString()
            val description = etTaskDescription.text.toString()

            val taskJson = sharedPreferences.getString("task_list", "[]")
            val taskArray = JSONArray(taskJson)

            val newTask = JSONObject()
            newTask.put("name", name)
            newTask.put("description", description)

            taskArray.put(newTask)

            sharedPreferences.edit()
                .putString("task_list", taskArray.toString())
                .apply()

            finish()
        }

        /**
         * Finishes the activity and returns to the previous screen without saving.
         */
        btnBack.setOnClickListener {
            finish()
        }
    }
}
