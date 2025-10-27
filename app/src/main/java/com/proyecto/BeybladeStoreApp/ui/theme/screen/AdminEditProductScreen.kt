package com.proyecto.BeybladeStoreApp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import coil.compose.AsyncImage
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.proyecto.BeybladeStoreApp.data.models.Product
import com.proyecto.BeybladeStoreApp.repository.ProductRepository
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment

@Composable
fun AdminEditProductScreen(productId: Int, onBack: () -> Unit = {}) {
    val ctx = LocalContext.current
    val repo = remember { ProductRepository(ctx) }
    val scope = rememberCoroutineScope()

    var product by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(productId) {
        product = repo.getProducts().firstOrNull { it.id == productId }
    }

    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val imageRes = remember { mutableStateOf("") }
    val stock = remember { mutableStateOf("") }
    val imageUris = remember { mutableStateOf<List<String>>(emptyList()) }

    // launcher for multiple images
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
        if (!uris.isNullOrEmpty()) {
            val list = uris.mapNotNull { uri ->
                try { ctx.contentResolver.takePersistableUriPermission(uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION) } catch (_: Exception) {}
                uri.toString()
            }
            imageUris.value = imageUris.value + list
        }
    }

    LaunchedEffect(product) {
        product?.let { p ->
            name.value = p.name
            description.value = p.description
            price.value = p.price.toString()
            stock.value = p.stock.toString()
            imageRes.value = p.imageResName ?: ""
            imageUris.value = p.imageUris
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Editar producto", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(value = name.value, onValueChange = { name.value = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = description.value, onValueChange = { description.value = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = price.value, onValueChange = { price.value = it }, label = { Text("Precio") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = stock.value, onValueChange = { stock.value = it }, label = { Text("Stock") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = imageRes.value, onValueChange = { imageRes.value = it }, label = { Text("Nombre recurso drawable (opcional)") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { launcher.launch(arrayOf("image/*")) }) { Text("Agregar imágenes") }

        if (imageUris.value.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.foundation.lazy.LazyRow {
                    items(items = imageUris.value) { u ->
                        Box(modifier = Modifier.padding(end = 8.dp)) {
                            AsyncImage(model = u, contentDescription = "img", modifier = Modifier.size(120.dp))
                            Button(onClick = { imageUris.value = imageUris.value.filterNot { it == u } }, modifier = Modifier.align(Alignment.TopEnd)) { Text("X") }
                        }
                    }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val p = product ?: return@Button
                scope.launch {
                    val list = repo.getProducts().map {
                        if (it.id == p.id) it.copy(
                            name = name.value,
                            description = description.value,
                            price = price.value.toDoubleOrNull() ?: it.price,
                            imageResName = imageRes.value.ifBlank { null },
                            imageUris = imageUris.value,
                            stock = stock.value.toIntOrNull() ?: it.stock
                        ) else it
                    }
                    repo.saveProducts(list)
                    onBack()
                }
            }) { Text("Guardar") }

            Button(onClick = {
                // delete product
                val p = product ?: return@Button
                scope.launch {
                    val list = repo.getProducts().filterNot { it.id == p.id }
                    repo.saveProducts(list)
                    onBack()
                }
            }) { Text("Eliminar producto") }

            Button(onClick = onBack) { Text("Volver") }
        }
    }
}
