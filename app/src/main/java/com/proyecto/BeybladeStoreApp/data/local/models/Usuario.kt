package com.proyecto.BeybladeStoreApp.data.local.models


data class Usuario(
    val id: Int = 0,
    val email: String,
    val password: String,
    val createdAt: Long = System.currentTimeMillis()
)

