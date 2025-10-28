package com.proyecto.BeybladeStoreApp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.proyecto.BeybladeStoreApp.data.models.CartItem

@Composable
fun CartScreen(
    onBack: () -> Unit,
    onCartClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onAdminClick: () -> Unit,
    onSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val ctx = LocalContext.current


    val productRepo = remember { com.proyecto.BeybladeStoreApp.repository.ProductRepository(ctx) }
    val productFactory = remember(productRepo) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return com.proyecto.BeybladeStoreApp.ui.theme.viewModel.ProductsViewModel(productRepo) as T
            }
        }
    }
    val productsVm: com.proyecto.BeybladeStoreApp.ui.theme.viewModel.ProductsViewModel = viewModel(factory = productFactory)
    val products by productsVm.products.collectAsState()


    val cartRepo = remember { com.proyecto.BeybladeStoreApp.repository.CartRepository(ctx) }
    val cartFactory = remember(cartRepo) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return com.proyecto.BeybladeStoreApp.ui.theme.viewModel.CartViewModel(cartRepo) as T
            }
        }
    }
    val cartVm: com.proyecto.BeybladeStoreApp.ui.theme.viewModel.CartViewModel = viewModel(factory = cartFactory)
    val items by cartVm.items.collectAsState()


    val ordersRepo = remember { com.proyecto.BeybladeStoreApp.repository.OrdersRepository(ctx) }
    val ordersFactory = remember(ordersRepo) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return com.proyecto.BeybladeStoreApp.ui.theme.viewModel.OrdersViewModel(ordersRepo) as T
            }
        }
    }
    val ordersVm: com.proyecto.BeybladeStoreApp.ui.theme.viewModel.OrdersViewModel = viewModel(factory = ordersFactory)

    val sessionState = produceState(initialValue = null as String?, key1 = ctx) {
        value = try {
            com.proyecto.BeybladeStoreApp.repository.AuthRepository(ctx).getSession()
        } catch (t: Throwable) {
            null
        }
    }
    val currentUser = sessionState.value
    val cartCount = items.size

    Column(modifier = Modifier.fillMaxSize()) {
    SharedTopBar(currentUser = currentUser, cartCount = cartCount, onCartClick = onCartClick, onOrdersClick = onOrdersClick, onAdminClick = onAdminClick, onSettings = onSettings, onLogout = onLogout)

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Carrito", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items) { ci ->
                val prod = products.firstOrNull { it.id == ci.productId }
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(prod?.name ?: "Producto")
                        Text("Cantidad: ${ci.quantity}")
                    }
                    Column {
                        IconButton(onClick = { cartVm.updateQuantity(ci.productId, ci.quantity + 1) }) { Icon(imageVector = Icons.Filled.Add, contentDescription = "+") }
                        IconButton(onClick = { cartVm.updateQuantity(ci.productId, ci.quantity - 1) }) { Icon(imageVector = Icons.Filled.Remove, contentDescription = "-") }
                    }
                }
            }
        }


        val total = remember(items, products) {
            items.fold(0.0) { acc, ci -> acc + (products.firstOrNull { it.id == ci.productId }?.price ?: 0.0) * ci.quantity }
        }
    Text("Total: \$${String.format("%.2f", total)}", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onBack() }) {
                Text("Volver")
            }
            Button(onClick = {

                ordersVm.checkout(items, total, currentUser)
                cartVm.clearCart()
                android.widget.Toast.makeText(ctx, "Compra realizada y guardada", android.widget.Toast.LENGTH_SHORT).show()
            }) {
                Text("Pagar")
            }
        }
    }
    }
}

