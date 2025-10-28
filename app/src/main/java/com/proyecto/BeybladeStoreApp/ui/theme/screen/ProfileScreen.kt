package com.proyecto.BeybladeStoreApp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.proyecto.BeybladeStoreApp.repository.AuthRepository
import com.proyecto.BeybladeStoreApp.data.local.models.UserProfile
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(onBack: () -> Unit = {}) {
    val ctx = LocalContext.current
    val repo = remember { AuthRepository(ctx) }
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf<String?>(null) }
    var displayName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val session = repo.getSession()
        email = session
        if (session != null) {
            val p = repo.getProfile(session)
            if (p != null) {
                displayName = p.displayName ?: ""
                phone = p.phone ?: ""
                address = p.address ?: ""
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mi perfil", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(value = displayName, onValueChange = { displayName = it }, label = { Text("Nombre para mostrar") }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(color = androidx.compose.ui.graphics.Color.Black))
        Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(color = androidx.compose.ui.graphics.Color.Black))
        Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Dirección de envío") }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(color = androidx.compose.ui.graphics.Color.Black))
        Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(value = newPassword, onValueChange = { newPassword = it }, label = { Text("Nueva contraseña (opcional)") }, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(color = androidx.compose.ui.graphics.Color.Black))

        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                scope.launch {
                    val e = email
                    if (e != null) {
                        repo.saveProfile(UserProfile(email = e, displayName = displayName.ifBlank { null }, phone = phone.ifBlank { null }, address = address.ifBlank { null }))
                        if (newPassword.isNotBlank()) {
                            repo.updatePassword(e, newPassword)
                        }
                        message = "Guardado"
                    } else {
                        message = "No hay sesión activa"
                    }
                }
            }) { Text("Guardar") }

            Button(onClick = onBack) { Text("Volver") }
        }

        Spacer(modifier = Modifier.height(12.dp))
        if (message.isNotBlank()) Text(message)
    }
}

