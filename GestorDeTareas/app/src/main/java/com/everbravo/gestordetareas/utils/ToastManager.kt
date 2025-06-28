package com.everbravo.gestordetareas.utils

import android.content.Context
import android.widget.Toast
import com.everbravo.gestordetareas.utils.enums.ValidationResult

object ToastManager {

    fun showMeAToast(context: Context, validResult: ValidationResult): Boolean {
        if (validResult != ValidationResult.SUCCESS) {
            Toast.makeText(context, validResult.description, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}