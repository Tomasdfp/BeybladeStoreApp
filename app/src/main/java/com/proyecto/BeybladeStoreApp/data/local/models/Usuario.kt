package com.proyecto.BeybladeStoreApp.data.local.models

// Placeholder user model retained for compatibility. Persistence now uses DataStore.
data class Usuario(
    val id: Int = 0,
    val email: String,
    val password: String,
    val createdAt: Long = System.currentTimeMillis()
)
