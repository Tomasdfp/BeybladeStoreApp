package com.proyecto.BeybladeStoreApp.data.models

data class Product(
    val id: Int,
    val name: String,
    val description: String = "",
    val price: Double,

    val imageResName: String? = null,

    val imageUri: String? = null,

    val imageUris: List<String> = emptyList(),

    val stock: Int = 0
)

