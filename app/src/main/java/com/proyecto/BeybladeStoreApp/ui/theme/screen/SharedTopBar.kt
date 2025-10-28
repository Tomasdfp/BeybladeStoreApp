package com.proyecto.BeybladeStoreApp.ui.theme.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedTopBar(
    currentUser: String?,
    cartCount: Int,
    onCartClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onAdminClick: () -> Unit,
    onSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val menuExpanded = remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("Bienvenido a BeybladeStore") },
        actions = {

            if (currentUser != "admin") {
                IconButton(onClick = onCartClick) {
                    BadgedBox(badge = { if (cartCount > 0) Badge { Text(cartCount.toString()) } }) {
                        Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "Carrito")
                    }
                }
            }

            IconButton(onClick = { menuExpanded.value = true }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Menu")
            }

            DropdownMenu(expanded = menuExpanded.value, onDismissRequest = { menuExpanded.value = false }) {
                if (currentUser != "admin") DropdownMenuItem(text = { Text("Carrito") }, onClick = { menuExpanded.value = false; onCartClick() })
                DropdownMenuItem(text = { Text(if (currentUser == "admin") "Órdenes" else "Mis órdenes") }, onClick = { menuExpanded.value = false; onOrdersClick() })
                if (currentUser == "admin") {
                    DropdownMenuItem(text = { Text("Administrar") }, onClick = { menuExpanded.value = false; onAdminClick() })
                }
                DropdownMenuItem(text = { Text("Ajustes") }, onClick = { menuExpanded.value = false; onSettings() })
                DropdownMenuItem(text = { Text("Cerrar sesión") }, onClick = { menuExpanded.value = false; onLogout() })
            }
        }
    )
}

