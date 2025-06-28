package com.everbravo.gestordetareas.utils

import android.util.Patterns
import com.everbravo.gestordetareas.utils.enums.ValidationResult

object Validations {

    private fun isValidInput(input: String): ValidationResult {
        val invalidChars = listOf("'", "\"", ";", "--", "/*", "*/", "xp_")
        for (char in invalidChars) {
            if (input.contains(char)) {
                return ValidationResult.LABEL_INVALID
            }
        }
        return ValidationResult.SUCCESS
    }

    fun validateEmpty(username: String): ValidationResult {
        if (username.isEmpty()) {
            return ValidationResult.LABEL_EMPTY
        }
        if (username.length < 3) {
            return ValidationResult.LABEL_TOO_SHORT
        }
        return isValidInput(username)
    }

    fun validateEmail(email: String): ValidationResult {
        if (email.isEmpty()) {
            return ValidationResult.EMAIL_EMPTY
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult.EMAIL_INVALID
        }
        return isValidInput(email)
    }

    fun validatePassword(password: String): ValidationResult {
        if (password.isEmpty()) {
            return ValidationResult.PASSWORD_EMPTY
        }
        if (password.length < 6) {
            return ValidationResult.PASSWORD_TOO_SHORT
        }
        return isValidInput(password)
    }
}