package com.everbravo.gestordetareas.persistence.dao

import android.content.ContentValues
import android.content.Context
import com.everbravo.gestordetareas.persistence.TaskDatabaseHelper
import com.everbravo.gestordetareas.persistence.domain.Task

class TaskDao(context: Context) {
    private val dbHelper = TaskDatabaseHelper(context)

    fun insertTask(name: String, description: String, latitude: Double?, longitude: Double?) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("description", description)
            put("latitude", latitude)
            put("longitude", longitude)
        }
        db.insert(TaskDatabaseHelper.TABLE_TASKS, null, values)
        db.close()
    }

    fun getAllTasks(): List<Task> {
        val db = dbHelper.readableDatabase
        val tasks = mutableListOf<Task>()
        val cursor = db.query(
            TaskDatabaseHelper.TABLE_TASKS,
            arrayOf("id", "name", "description", "latitude", "longitude"),
            null, null, null, null, "id DESC"
        )

        with(cursor) {
            while (moveToNext()) {
                tasks.add(
                    Task(
                        id = getInt(getColumnIndexOrThrow("id")),
                        name = getString(getColumnIndexOrThrow("name")),
                        description = getString(getColumnIndexOrThrow("description")),
                        latitude = getDouble(getColumnIndexOrThrow("latitude")),
                        longitude = getDouble(getColumnIndexOrThrow("longitude"))
                    )
                )
            }
        }
        cursor.close()
        db.close()
        return tasks
    }
}
