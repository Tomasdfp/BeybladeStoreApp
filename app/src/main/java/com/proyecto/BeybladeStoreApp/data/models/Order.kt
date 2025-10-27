package com.proyecto.BeybladeStoreApp.data.models

data class Order(
    val id: Int,
    val items: List<CartItem>,
    val total: Double,
    val createdAt: Long = System.currentTimeMillis(),
    val userEmail: String? = null,
    val status: String = "CREATED"
)
