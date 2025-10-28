package com.proyecto.BeybladeStoreApp.ui.theme.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.BeybladeStoreApp.ui.theme.viewModel.AuthViewModel
import com.proyecto.BeybladeStoreApp.util.EmailValidator
import com.proyecto.BeybladeStoreApp.repository.AuthRepository
import com.proyecto.BeybladeStoreApp.util.SharedPrefs
import com.proyecto.BeybladeStoreApp.R

@Composable
fun LoginRoute(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
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

    var loginMessage by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Ensure default user present
    LaunchedEffect(Unit) { vm.ensureDefault() }

    LoginScreen(
        onLogin = { email, password ->
            // Basic validation
            // Allow legacy short usernames 'admin' and 'user' (seeded defaults), or a valid email
            val trimmed = email.trim()
            if (!(EmailValidator.isValid(trimmed) || trimmed == "admin" || trimmed == "user")) {
                loginMessage = "Correo inválido"
                return@LoginScreen
            }
            if (password.length < 6) {
                loginMessage = "La contraseña debe tener al menos 6 caracteres"
                return@LoginScreen
            }

            loading = true
            scope.launch {
                val ok = vm.loginSuspend(email, password)
                loading = false
                if (ok) {
                    loginMessage = "Login correcto"
                    onLoginSuccess()
                } else {
                    loginMessage = "Credenciales inválidas"
                }
            }
        },
        onNavigateToRegister = onNavigateToRegister,
        loginResultMessage = if (loading) "Validando..." else loginMessage
    )
}
