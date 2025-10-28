package com.proyecto.BeybladeStoreApp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.proyecto.BeybladeStoreApp.util.DataStoreManager

@Composable
fun AdminUsersScreen(onBack: () -> Unit, onEditUser: (String) -> Unit = {}) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val ds = remember { DataStoreManager(ctx) }

    val usersState by produceState(initialValue = emptyMap<String, String>(), key1 = ctx) {
        value = try {
            ds.getUsersMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }

    val rolesState by produceState(initialValue = emptyMap<String, String>(), key1 = ctx) {
        value = try {
            com.proyecto.BeybladeStoreApp.repository.AuthRepository(ctx).getRoles()
        } catch (e: Exception) {
            emptyMap()
        }
    }

    val newEmail = remember { mutableStateOf("") }
    val newPass = remember { mutableStateOf("") }
    val isAdmin = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Usuarios", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(value = newEmail.value, onValueChange = { newEmail.value = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(color = androidx.compose.ui.graphics.Color.Black))
        Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(value = newPass.value, onValueChange = { newPass.value = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(color = androidx.compose.ui.graphics.Color.Black))
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                if (newEmail.value.isNotBlank() && newPass.value.isNotBlank()) {
                    coroutineScope.launch {
                        withContext(Dispatchers.IO) {
                            com.proyecto.BeybladeStoreApp.repository.AuthRepository(ctx).createUser(newEmail.value, newPass.value, if (isAdmin.value) "admin" else "user")
                        }
                    }
                    newEmail.value = ""
                    newPass.value = ""
                }
            }) { Text("Crear usuario") }

            androidx.compose.material3.Switch(checked = isAdmin.value, onCheckedChange = { isAdmin.value = it })
            Text("Es admin")
        }

        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(usersState.toList()) { pair ->
                val (email, pass) = pair
                val role = rolesState[email] ?: "user"
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(email)
                        Text("Rol: $role")
                    }
                    Row {
                        Button(onClick = { onEditUser(email) }) { Text("Editar") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                com.proyecto.BeybladeStoreApp.repository.AuthRepository(ctx).deleteUser(email)
                            }
                        }
                    }) { Text("Eliminar") }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onBack) { Text("Volver") }
    }
}

