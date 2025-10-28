package com.proyecto.BeybladeStoreApp.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.proyecto.BeybladeStoreApp.data.models.Order
import com.proyecto.BeybladeStoreApp.data.models.CartItem
import com.proyecto.BeybladeStoreApp.util.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrdersRepository(private val context: Context) {
    private val ds = DataStoreManager(context)
    private val gson = Gson()

    suspend fun getOrders(): MutableList<Order> {
        val json = ds.getOrdersJson()
        // We'll store orders in PRODUCTS_KEY to avoid changing DataStoreManager further in this patch
        return try {
            val type = TypeToken.getParameterized(List::class.java, Order::class.java).type
            val list: List<Order>? = gson.fromJson(json ?: "[]", type)
            list?.toMutableList() ?: mutableListOf()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    suspend fun saveOrders(orders: List<Order>) {
        val json = gson.toJson(orders)
        ds.saveOrdersJson(json)
    }

    suspend fun deleteOrder(id: Int) {
        val current = getOrders()
        val toDelete = current.firstOrNull { it.id == id }
        val updated = current.filterNot { it.id == id }
        saveOrders(updated)

        // restore stock from deleted order items
        try {
            if (toDelete != null) {
                val prodRepo = ProductRepository(context)
                val products = prodRepo.getProducts().toMutableList()
                for (ci in toDelete.items) {
                    val idx = products.indexOfFirst { it.id == ci.productId }
                    if (idx >= 0) {
                        val p = products[idx]
                        products[idx] = p.copy(stock = p.stock + ci.quantity)
                    }
                }
                prodRepo.saveProducts(products)
            }
        } catch (_: Exception) {
        }
    }

    suspend fun clearOrders() {
        saveOrders(emptyList())
    }

    fun ordersFlow(): Flow<List<Order>> {
        return ds.ordersJsonFlow().map { json ->
            try {
                val type = TypeToken.getParameterized(List::class.java, Order::class.java).type
                val list: List<Order>? = gson.fromJson(json ?: "[]", type)
                list ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun addOrder(items: List<CartItem>, total: Double, userEmail: String?) {
        val current = getOrders()
        val nextId = (current.maxByOrNull { it.id }?.id ?: 0) + 1
        val order = Order(id = nextId, items = items, total = total, userEmail = userEmail)
        current.add(order)
        saveOrders(current)

        // Deduct stock from products
        try {
            val prodRepo = ProductRepository(context)
            val products = prodRepo.getProducts().toMutableList()
            for (ci in items) {
                val idx = products.indexOfFirst { it.id == ci.productId }
                if (idx >= 0) {
                    val p = products[idx]
                    products[idx] = p.copy(stock = maxOf(0, p.stock - ci.quantity))
                }
            }
            prodRepo.saveProducts(products)
        } catch (_: Exception) {
            // ignore stock update errors
        }
    }
}
