package com.proyecto.BeybladeStoreApp.data.local.models

data class UserProfile(
    val email: String,
    val displayName: String? = null,
    val phone: String? = null,
    val address: String? = null
)
