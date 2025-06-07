package com.everbravo.gestortareasjetpack

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.json.JSONArray
import org.json.JSONObject

/**
 * FormActivity Compose version: Pantalla para ingresar y guardar una nueva tarea.
 */
class FormActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FormScreen(onFinish = { finish() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(onFinish: () -> Unit) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                showError = false
            },
            label = { Text("Nombre de la tarea") },
            modifier = Modifier.fillMaxWidth(),
            isError = showError && name.isBlank()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
                showError = false
            },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 5,
            isError = showError && description.isBlank()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (name.isNotBlank() && description.isNotBlank()) {
                    try {
                        val sharedPreferences = context.getSharedPreferences("Tasks", Context.MODE_PRIVATE)
                        val taskJson = sharedPreferences.getString("task_list", "[]") ?: "[]"
                        val taskArray = JSONArray(taskJson)

                        val newTask = JSONObject().apply {
                            put("name", name)
                            put("description", description)
                        }

                        taskArray.put(newTask)

                        sharedPreferences.edit()
                            .putString("task_list", taskArray.toString())
                            .apply()

                        onFinish() // Vuelve a la pantalla anterior
                    } catch (e: Exception) {
                        e.printStackTrace() // Loguea cualquier error inesperado
                    }
                } else {
                    showError = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { onFinish() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}
