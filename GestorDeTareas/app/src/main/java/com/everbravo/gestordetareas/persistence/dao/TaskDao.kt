package com.everbravo.gestordetareas.persistence.dao

import android.content.ContentValues
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.everbravo.gestordetareas.persistence.TaskDatabaseHelper
import com.everbravo.gestordetareas.persistence.domain.Task
import com.everbravo.gestordetareas.utils.AESCipher

class TaskDao(context: Context) {
    private val dbHelper = TaskDatabaseHelper(context)

    fun insertTask(name: String, description: String, latitude: Double?, longitude: Double?) {
        val db = dbHelper.writableDatabase
        val encryptedName = AESCipher.encrypt(name)
        val encryptedDescription = AESCipher.encrypt(description)
        val values = ContentValues().apply {
            put("name", encryptedName)
            put("description", encryptedDescription)
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
                        name = AESCipher.decrypt(getString(getColumnIndexOrThrow("name"))),
                        description = AESCipher.decrypt(getString(getColumnIndexOrThrow("description"))),
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
