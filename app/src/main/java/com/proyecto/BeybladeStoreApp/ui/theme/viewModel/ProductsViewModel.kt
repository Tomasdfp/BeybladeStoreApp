package com.proyecto.BeybladeStoreApp.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.BeybladeStoreApp.data.models.Product
import com.proyecto.BeybladeStoreApp.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductsViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    init {

        viewModelScope.launch {
            try {
                repository.productsFlow().collect { list ->
                    _products.value = list
                }
            } catch (_: Exception) {
                _products.value = emptyList()
            }
        }
    }


    fun load() {
        viewModelScope.launch {
            try {
                val list = repository.getProducts()
                _products.value = list
            } catch (_: Exception) {
                _products.value = emptyList()
            }
        }
    }

    fun addProduct(p: Product) {
        viewModelScope.launch {
            val current = _products.value.toMutableList()
            current.add(p)
            repository.saveProducts(current)
            _products.value = current
        }
    }
}

