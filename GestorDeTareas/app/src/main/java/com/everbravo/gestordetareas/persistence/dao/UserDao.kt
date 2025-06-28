package com.everbravo.gestordetareas.persistence.dao

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.everbravo.gestordetareas.persistence.TaskDatabaseHelper
import com.everbravo.gestordetareas.persistence.domain.Task
import com.everbravo.gestordetareas.persistence.domain.User
import com.everbravo.gestordetareas.utils.AESCipher
import com.everbravo.gestordetareas.utils.enums.ValidationResult
import org.mindrot.jbcrypt.BCrypt

class UserDao(context: Context) {
    private val dbHelper = TaskDatabaseHelper(context)

    fun insertUser(name: String, username: String, password: String, active: Int?): ValidationResult {

        if (isUserExists(username)) {
            return ValidationResult.USERNAME_ERROR
        }

        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("username", username)
            put("password", hashedPassword)
            put("active", active)
        }
        db.insert(TaskDatabaseHelper.TABLE_USERS, null, values)
        db.close()

        return ValidationResult.USER_SUCCESS
    }

    fun getUser(usernameInput: String, passwordInput: String): User? {
        val db = dbHelper.readableDatabase
        var user: User? = null

        val cursor = db.query(
            TaskDatabaseHelper.TABLE_USERS,
            arrayOf("id", "name", "username", "password", "active"),
            "username = ? AND active = 1",
            arrayOf(usernameInput),
            null, null, null
        )

        with(cursor) {
            if (moveToFirst()) {
                val storedHash = getString(getColumnIndexOrThrow("password"))

                if (BCrypt.checkpw(passwordInput, storedHash)) {
                    user = User(
                        id = getInt(getColumnIndexOrThrow("id")),
                        name = getString(getColumnIndexOrThrow("name")),
                        username = getString(getColumnIndexOrThrow("username")),
                        password = "",
                        active = getInt(getColumnIndexOrThrow("active"))
                    )
                } else {
                    user = null
                }
            }
        }

        cursor.close()
        db.close()

        return user
    }

    private fun isUserExists(username: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TaskDatabaseHelper.TABLE_USERS,
            arrayOf("username"),
            "username = ?",
            arrayOf(username),
            null, null, null
        )

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

}
