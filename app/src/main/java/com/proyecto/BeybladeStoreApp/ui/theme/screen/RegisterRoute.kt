package com.proyecto.BeybladeStoreApp.ui.theme.screen

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.BeybladeStoreApp.ui.theme.viewModel.AuthViewModel
import com.proyecto.BeybladeStoreApp.repository.AuthRepository
import com.proyecto.BeybladeStoreApp.util.EmailValidator

@Composable
fun RegisterRoute(
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current

    val authRepo = remember { AuthRepository(context) }

    val factory = remember(authRepo) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(authRepo) as T
            }
        }
    }

    val vm: AuthViewModel = viewModel(factory = factory)

    var registerMessage by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    // Nueva variable para indicar si el registro fue exitoso
    var clearFieldsTrigger by remember { mutableStateOf(false) }

    // Ensure default user is present
    LaunchedEffect(Unit) { vm.ensureDefault() }

    RegisterScreen(
        onRegister = { email, password ->
            // Basic validation
            if (!EmailValidator.isValid(email)) {
                registerMessage = "Correo inválido"
                return@RegisterScreen
            }
            if (password.length < 6) {
                registerMessage = "La contraseña debe tener al menos 6 caracteres"
                return@RegisterScreen
            }

            loading = true
            vm.registerResult = { result ->
                loading = false
                result.fold(
                    onSuccess = {
                        registerMessage = "Registro exitoso 🎉"
                        clearFieldsTrigger = true
                    },
                    onFailure = {
                        registerMessage = it.message ?: "Error al registrar"
                    }
                )
            }
            vm.register(email, password)
        },
        onNavigateToLogin = onNavigateToLogin,
        registerResultMessage = if (loading) "Registrando..." else registerMessage,
        clearFieldsTrigger = clearFieldsTrigger,
        onFieldsCleared = { clearFieldsTrigger = false }
    )
}
