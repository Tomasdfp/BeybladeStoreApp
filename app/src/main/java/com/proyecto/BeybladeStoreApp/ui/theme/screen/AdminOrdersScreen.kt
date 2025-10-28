package com.proyecto.BeybladeStoreApp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.proyecto.BeybladeStoreApp.data.models.Order

@Composable
fun AdminOrdersScreen(onBack: () -> Unit) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val repo = remember { com.proyecto.BeybladeStoreApp.repository.OrdersRepository(ctx) }
    val ordersState by produceState(initialValue = emptyList<Order>(), key1 = ctx) {
        repo.ordersFlow().collect { value = it }
    }

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Órdenes", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {

        items(ordersState) { order ->
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text("Pedido #${order.id} — Total: \$${order.total}")
                    Text("Items: ${order.items.size}")
                    Text("Usuario: ${order.userEmail ?: "-"}")
                    Button(onClick = {

                        android.widget.Toast.makeText(ctx, order.items.joinToString("; ") { "prod:${it.productId} x${it.quantity}" }, android.widget.Toast.LENGTH_LONG).show()
                    }) { Text("Ver detalles") }
                    Text("Fecha: ${java.text.SimpleDateFormat.getDateTimeInstance().format(java.util.Date(order.createdAt))}")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {

                            coroutineScope.launch {
                                withContext(Dispatchers.IO) {
                                    repo.deleteOrder(order.id)
                                }
                            }
                        }) { Text("Eliminar") }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onBack) { Text("Volver") }
            Button(onClick = {
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        repo.clearOrders()
                    }
                }
            }) { Text("Borrar todas las órdenes") }
        }
    }
}

