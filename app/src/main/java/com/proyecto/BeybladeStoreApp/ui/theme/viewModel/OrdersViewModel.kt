package com.proyecto.BeybladeStoreApp.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.BeybladeStoreApp.data.models.Order
import com.proyecto.BeybladeStoreApp.data.models.CartItem
import com.proyecto.BeybladeStoreApp.repository.OrdersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrdersViewModel(private val repository: OrdersRepository) : ViewModel() {
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            try {
                _orders.value = repository.getOrders()
            } catch (_: Exception) {
                _orders.value = emptyList()
            }
        }
    }

    fun checkout(items: List<CartItem>, total: Double, userEmail: String?) {
        viewModelScope.launch {
            repository.addOrder(items, total, userEmail)
            load()
        }
    }
}
