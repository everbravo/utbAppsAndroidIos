package com.everbravo.gestordetareas.persistence.domain

data class User (
    val id: Int,
    val name: String,
    val username: String,
    val password: String,
    val active: Int?
)