package com.proyecto.BeybladeStoreApp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlinx.coroutines.launch
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.proyecto.BeybladeStoreApp.data.models.Product

@Composable
fun AdminCatalogScreen(onBack: () -> Unit, onEditProduct: (Int) -> Unit = {}) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val repo = remember { com.proyecto.BeybladeStoreApp.repository.ProductRepository(ctx) }
    val products by produceState(initialValue = emptyList<Product>(), key1 = ctx) {
        try {
            repo.productsFlow().collect { list -> value = list }
        } catch (t: Throwable) {
            value = emptyList()
        }
    }

    val scope = rememberCoroutineScope()


    val editableStocks = remember { mutableStateMapOf<Int, Int>() }
    LaunchedEffect(products) {
        editableStocks.clear()
        for (p in products) editableStocks[p.id] = p.stock
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Catálogo (Admin)", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(products) { p ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onEditProduct(p.id) }, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(p.name, style = MaterialTheme.typography.titleMedium)
                        Text("Stock: ${editableStocks[p.id] ?: p.stock}")
                    }
                    Row {
                        Button(onClick = {
                            editableStocks[p.id] = (editableStocks[p.id] ?: p.stock) + 1
                        }) { Text("+") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            editableStocks[p.id] = maxOf(0, (editableStocks[p.id] ?: p.stock) - 1)
                        }) { Text("-") }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {

                scope.launch {
                    val current = repo.getProducts().map { p ->
                        val s = editableStocks[p.id]
                        if (s != null && s != p.stock) p.copy(stock = s) else p
                    }
                    repo.saveProducts(current)
                }
            }) { Text("Guardar cambios") }

            Button(onClick = onBack) { Text("Volver") }
        }
    }
}

