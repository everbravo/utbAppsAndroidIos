package com.everbravo.gestortareasjetpack

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.everbravo.gestortareasjetpack.ui.theme.GestorTareasJetpackTheme
import org.json.JSONArray

/**
 * MainActivity es la pantalla principal que muestra la lista de tareas almacenadas
 * y permite navegar al formulario para agregar nuevas tareas.
 *
 * @author Ever Bravo
 * @version 2.0 (Jetpack Compose)
 */
class MainActivity : ComponentActivity() {
    private var taskList by mutableStateOf(listOf<String>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTasks() // Carga inicial

        setContent {
            TaskListScreen(taskList = taskList, onAddTask = {
                startActivity(Intent(this, FormActivity::class.java))
            })
        }
    }

    override fun onResume() {
        super.onResume()
        loadTasks() // Recarga cada vez que regresa a esta actividad
    }

    private fun loadTasks() {
        val sharedPreferences = getSharedPreferences("Tasks", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("task_list", "[]")
        val jsonArray = JSONArray(json)
        val tempList = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            val task = jsonArray.getJSONObject(i)
            val name = task.getString("name")
            val description = task.getString("description")
            tempList.add("Título: $name\nResumen: $description")
        }
        taskList = tempList
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(taskList: List<String>, onAddTask: () -> Unit) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            Text(
                text = "Gestor de Tareas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                Button(
                    onClick = onAddTask,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Text("Agregar Nueva Tarea")
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(taskList) { task ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Text(
                                text = task,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}
