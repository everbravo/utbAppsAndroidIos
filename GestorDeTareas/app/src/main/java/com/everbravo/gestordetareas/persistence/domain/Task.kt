package com.everbravo.gestordetareas.persistence.domain

data class Task(
    val id: Int,
    val name: String,
    val description: String,
    val latitude: Double?,
    val longitude: Double?
)