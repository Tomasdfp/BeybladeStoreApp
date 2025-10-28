package com.proyecto.BeybladeStoreApp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.Button
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.proyecto.BeybladeStoreApp.repository.OrdersRepository
import com.proyecto.BeybladeStoreApp.repository.ProductRepository
import com.proyecto.BeybladeStoreApp.data.models.Order
import androidx.compose.ui.platform.LocalContext

@Composable
fun OrdersScreen(onBack: () -> Unit = {}) {
    val ctx = LocalContext.current
    val ordersState by produceState(initialValue = emptyList<Order>(), key1 = ctx) {
        try {
            OrdersRepository(ctx).ordersFlow().collect { list ->
                value = list
            }
        } catch (t: Throwable) {
            try {
                val dir = java.io.File(ctx.filesDir, "crash_logs")
                if (!dir.exists()) dir.mkdirs()
                val f = java.io.File(dir, "produce_orders_error.txt")
                f.appendText("${java.util.Date()} - ${t.stackTraceToString()}\n")
            } catch (_: Exception) {}
            value = emptyList()
        }
    }

    val sessionState = produceState(initialValue = null as String?, key1 = ctx) {
        value = try {
            com.proyecto.BeybladeStoreApp.repository.AuthRepository(ctx).getSession()
        } catch (_: Exception) { null }
    }
    val currentUser = sessionState.value
    val roles = produceState(initialValue = emptyMap<String,String>(), key1 = ctx) {
        value = try { com.proyecto.BeybladeStoreApp.repository.AuthRepository(ctx).getRoles() } catch (_: Exception) { emptyMap() }
    }
    val isAdmin = roles.value[currentUser] == "admin"
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(if (isAdmin) "Órdenes" else "Mis órdenes", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = onBack) { Text("Volver") }
        }
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(ordersState.filter { isAdmin || it.userEmail == currentUser }) { order ->
                OrderRow(order, isAdmin = isAdmin, currentUser = currentUser, onCancel = { id ->

                    coroutineScope.launch {
                        com.proyecto.BeybladeStoreApp.repository.OrdersRepository(ctx).deleteOrder(id)
                    }
                })
            }
        }
    }
}

@Composable
private fun OrderRow(order: Order, isAdmin: Boolean, currentUser: String?, onCancel: (Int) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Text("Orden #${order.id}", style = MaterialTheme.typography.titleMedium)
    Text("Total: \$${String.format("%.2f", order.total)}")
        Text("Items: ${order.items.size}")
        Text("Fecha: ${java.text.SimpleDateFormat.getDateTimeInstance().format(java.util.Date(order.createdAt))}")
        Text("Usuario: ${order.userEmail ?: "-"}")
        if (!isAdmin && order.userEmail == currentUser) {
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { onCancel(order.id) }) { Text("Cancelar pedido") }
        }
    }
}

