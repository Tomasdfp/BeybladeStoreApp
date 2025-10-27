package com.proyecto.BeybladeStoreApp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminHubScreen(
    onAddProduct: () -> Unit,
    onManageOrders: () -> Unit,
    onManageUsers: () -> Unit,
    onManageCatalog: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Administración", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onAddProduct, modifier = Modifier.fillMaxWidth()) { Text("Agregar producto") }
        Spacer(modifier = Modifier.height(8.dp))
    Button(onClick = onManageCatalog, modifier = Modifier.fillMaxWidth()) { Text("Gestionar catálogo / stock") }
    Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onManageOrders, modifier = Modifier.fillMaxWidth()) { Text("Gestionar órdenes") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onManageUsers, modifier = Modifier.fillMaxWidth()) { Text("Gestionar usuarios") }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onBack) { Text("Volver") }
    }
}
