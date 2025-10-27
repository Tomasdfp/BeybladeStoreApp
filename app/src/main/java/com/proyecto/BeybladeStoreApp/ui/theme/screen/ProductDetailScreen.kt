package com.proyecto.BeybladeStoreApp.ui.theme.screen

import androidx.compose.foundation.Image
import coil.compose.AsyncImage
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import com.proyecto.BeybladeStoreApp.data.models.Product

@Composable
fun ProductDetailScreen(
    product: Product,
    currentUser: String?,
    cartCount: Int,
    onAddToCart: (Int, Int) -> Unit,
    onCartClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onAdminClick: () -> Unit,
    onSettings: () -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().padding(0.dp)) {
        // shared top bar
        SharedTopBar(currentUser = currentUser, cartCount = cartCount, onCartClick = onCartClick, onOrdersClick = onOrdersClick, onAdminClick = onAdminClick, onSettings = onSettings, onLogout = onLogout)

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // image (use local drawable if available)
        val ctx = LocalContext.current
        val resId = product.imageResName?.let { name ->
            ctx.resources.getIdentifier(name, "drawable", ctx.packageName)
        } ?: 0

        // Support multiple images: show carousel if imageUris present, otherwise fallback to single image
        if (!product.imageUris.isNullOrEmpty()) {
            LazyRow(modifier = Modifier
                .height(240.dp)
                .fillMaxWidth(), contentPadding = PaddingValues(horizontal = 8.dp)) {
                items(product.imageUris) { uri ->
                    AsyncImage(model = uri, contentDescription = product.name, modifier = Modifier
                        .height(240.dp)
                        .width(300.dp)
                        .padding(end = 8.dp))
                }
            }
        } else {
            Box(modifier = Modifier.height(240.dp).fillMaxWidth()) {
                if (!product.imageUri.isNullOrBlank()) {
                    AsyncImage(model = product.imageUri, contentDescription = product.name, modifier = Modifier.fillMaxSize())
                } else if (resId != 0) {
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = com.proyecto.BeybladeStoreApp.R.mipmap.ic_launcher),
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(product.name, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(product.description)
        Spacer(modifier = Modifier.height(12.dp))
    Text("Precio: \$${product.price}", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(8.dp))
        Text("Stock disponible: ${product.stock}")

        Spacer(modifier = Modifier.height(12.dp))

    // Quantity selector (only for non-admin users)
    val qtyState = remember { mutableStateOf(1) }
        if (currentUser != "admin") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                androidx.compose.material3.IconButton(onClick = { if (qtyState.value > 1) qtyState.value -= 1 }) { androidx.compose.material3.Text("-") }
                androidx.compose.material3.Text("Cantidad: ${qtyState.value}")
                androidx.compose.material3.IconButton(onClick = { if (qtyState.value < product.stock) qtyState.value += 1 }) { androidx.compose.material3.Text("+") }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back button
        Button(onClick = onBack) { Text("Volver") }

        Spacer(modifier = Modifier.height(8.dp))

        // hide add to cart for admin
        if (currentUser != "admin") {
            Button(onClick = { onAddToCart(product.id, qtyState.value) }) {
                Text("Agregar al carrito")
            }
        }
    }
    }
}
