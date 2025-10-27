package com.proyecto.BeybladeStoreApp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Switch
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.proyecto.BeybladeStoreApp.repository.AuthRepository
import com.proyecto.BeybladeStoreApp.data.local.models.UserProfile
import kotlinx.coroutines.launch

@Composable
fun AdminEditUserScreen(userEmail: String, onBack: () -> Unit = {}) {
    val ctx = LocalContext.current
    val repo = remember { AuthRepository(ctx) }
    val scope = rememberCoroutineScope()

    var displayName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(userEmail) {
        val p = repo.getProfile(userEmail)
        if (p != null) {
            displayName = p.displayName ?: ""
            phone = p.phone ?: ""
            address = p.address ?: ""
        }
        val roles = repo.getRoles()
        isAdmin = roles[userEmail] == "admin"
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Editar usuario: $userEmail", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(value = displayName, onValueChange = { displayName = it }, label = { Text("Nombre para mostrar") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = newPassword, onValueChange = { newPassword = it }, label = { Text("Nueva contraseña (opcional)") }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Es admin")
            Switch(checked = isAdmin, onCheckedChange = { isAdmin = it })
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                scope.launch {
                    // save profile
                    repo.saveProfile(UserProfile(email = userEmail, displayName = displayName.ifBlank { null }, phone = phone.ifBlank { null }, address = address.ifBlank { null }))
                    if (newPassword.isNotBlank()) repo.updatePassword(userEmail, newPassword)
                    repo.updateUserRole(userEmail, if (isAdmin) "admin" else "user")
                    message = "Guardado"
                }
            }) { Text("Guardar") }

            Button(onClick = onBack) { Text("Volver") }
        }

        Spacer(modifier = Modifier.height(12.dp))
        if (message.isNotBlank()) Text(message)
    }
}
