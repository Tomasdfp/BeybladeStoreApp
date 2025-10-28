package com.proyecto.BeybladeStoreApp.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.BeybladeStoreApp.data.models.CartItem
import com.proyecto.BeybladeStoreApp.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository) : ViewModel() {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items

    init {

        viewModelScope.launch {
            try {
                repository.cartItemsFlow().collect { list ->
                    _items.value = list
                }
            } catch (_: Exception) {
                _items.value = emptyList()
            }
        }
    }

    fun addToCart(productId: Int, qty: Int = 1) {
        viewModelScope.launch {
            repository.addToCart(productId, qty)
        }
    }

    fun removeFromCart(productId: Int) {
        viewModelScope.launch {
            repository.removeFromCart(productId)
        }
    }

    fun updateQuantity(productId: Int, quantity: Int) {
        viewModelScope.launch {
            repository.updateQuantity(productId, quantity)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }
}

