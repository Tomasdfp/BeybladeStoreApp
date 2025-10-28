package com.proyecto.BeybladeStoreApp.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.proyecto.BeybladeStoreApp.data.models.CartItem
import com.proyecto.BeybladeStoreApp.util.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepository(private val context: Context) {
    private val ds = DataStoreManager(context)
    private val gson = Gson()

    suspend fun getCartItems(): MutableList<CartItem> {
        val json = ds.getCartJson() ?: "[]"
        return try {
            val type = TypeToken.getParameterized(List::class.java, CartItem::class.java).type
            val list: List<CartItem>? = gson.fromJson(json, type)
            list?.toMutableList() ?: mutableListOf()
        } catch (e: Exception) {
            mutableListOf()
        }
    }


    fun cartItemsFlow(): Flow<List<CartItem>> {
        return ds.cartJsonFlow().map { json ->
            try {
                val type = TypeToken.getParameterized(List::class.java, CartItem::class.java).type
                val list: List<CartItem>? = gson.fromJson(json ?: "[]", type)
                list ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun saveCartItems(items: List<CartItem>) {
        val json = gson.toJson(items)
        ds.saveCartJson(json)
    }

    suspend fun addToCart(productId: Int, qty: Int = 1) {
        val items = getCartItems()
        val existing = items.indexOfFirst { it.productId == productId }
        if (existing >= 0) {
            val it = items[existing]
            items[existing] = it.copy(quantity = it.quantity + qty)
        } else {
            items.add(CartItem(productId = productId, quantity = qty))
        }
        saveCartItems(items)
    }

    suspend fun removeFromCart(productId: Int) {
        val items = getCartItems()
        items.removeAll { it.productId == productId }
        saveCartItems(items)
    }

    suspend fun updateQuantity(productId: Int, quantity: Int) {
        val items = getCartItems()
        val idx = items.indexOfFirst { it.productId == productId }
        if (idx >= 0) {
            if (quantity <= 0) items.removeAt(idx) else items[idx] = items[idx].copy(quantity = quantity)
            saveCartItems(items)
        }
    }

    suspend fun clearCart() {
        saveCartItems(emptyList())
    }
}

