package com.proyecto.BeybladeStoreApp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun RegisterScreen(
    onRegister: (email: String, password: String) -> Unit,
    onNavigateToLogin: () -> Unit,
    registerResultMessage: String?,
    clearFieldsTrigger: Boolean,
    onFieldsCleared: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(clearFieldsTrigger) {
        if (clearFieldsTrigger) {
            email = ""
            password = ""
            confirm = ""
            onFieldsCleared()
            localError = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Registro",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(Modifier.height(16.dp))


        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(color = androidx.compose.ui.graphics.Color.Black)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it

                if (localError != null) localError = null
            },
            label = { Text("Contraseña") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation =if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val text = if (showPassword) "Ocultar" else "Mostrar"
                TextButton(onClick = { showPassword = !showPassword }) { Text(text) }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            textStyle = androidx.compose.ui.text.TextStyle(color = androidx.compose.ui.graphics.Color.Black)
        )

        OutlinedTextField(
            value = confirm,
            onValueChange = {
                confirm = it

                if (localError != null) localError = null
            },
            label = { Text("Confirmar contraseña") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation =if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val text = if (showPassword) "Ocultar" else "Mostrar"
                TextButton(onClick = { showPassword = !showPassword }) { Text(text) }
            },
            textStyle = androidx.compose.ui.text.TextStyle(color = androidx.compose.ui.graphics.Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button( modifier = Modifier.fillMaxWidth(),
            onClick = {
            if (password == confirm) {
                onRegister(email, password)
            } else {
                localError = "Las contraseñas no coinciden"
            }

        }) {
            Text("Registrar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        registerResultMessage?.let {
            Text(text = it, color = if (it.contains("exitoso")) Color.Green else Color.Red)
        }

        localError?.let {
            Text(text = it, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}

