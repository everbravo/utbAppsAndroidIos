package com.everbravo.gestordetareas.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.everbravo.gestordetareas.MapActivity
import com.everbravo.gestordetareas.R
import org.json.JSONArray

class TaskAdapter(
    private val context: Context,
    private val tasks: JSONArray
) : BaseAdapter() {

    override fun getCount(): Int = tasks.length()

    override fun getItem(position: Int): Any = tasks.getJSONObject(position)

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.task_list_item, parent, false)

        val task = tasks.getJSONObject(position)

        val txtTitle = view.findViewById<TextView>(R.id.txtTaskTitle)
        val txtDescription = view.findViewById<TextView>(R.id.txtTaskDescription)
        val btnMap = view.findViewById<Button>(R.id.btnShowMap)

        txtTitle.text = task.getString("name")
        txtDescription.text = task.getString("description")

        btnMap.setOnClickListener {
            val intent = Intent(context, MapActivity::class.java).apply {
                putExtra("latitude", task.optDouble("latitude", 0.0))
                putExtra("longitude", task.optDouble("longitude", 0.0))
                putExtra("name", task.optString("name", "Ubicación"))
            }
            context.startActivity(intent)
        }

        return view
    }
}
