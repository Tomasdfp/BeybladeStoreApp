package com.proyecto.BeybladeStoreApp.data.models

data class Product(
    val id: Int,
    val name: String,
    val description: String = "",
    val price: Double,
    // Local drawable resource name (without extension), e.g. "gu967808_6_1"
    val imageResName: String? = null,
    // Optional image URI stored as string (content:// or file://) when admin uploads an image
    val imageUri: String? = null,
    // Optional multiple image URIs (kept for compatibility with recent admin UI)
    val imageUris: List<String> = emptyList(),
    // Stock count for the product
    val stock: Int = 0
)
