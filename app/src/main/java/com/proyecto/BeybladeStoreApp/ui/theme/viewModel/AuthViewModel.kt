package com.proyecto.BeybladeStoreApp.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.BeybladeStoreApp.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {
    var loginResult: ((Boolean) -> Unit)? = null
    var registerResult: ((Result<Unit>) -> Unit)? = null

    fun ensureDefault() {
        viewModelScope.launch {
            repo.ensureDefaultUser()
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            val res = repo.register(email, password)
            registerResult?.invoke(res)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val ok = repo.login(email, password)
            if (ok) repo.setSession(email.trim())
            loginResult?.invoke(ok)
        }
    }


    suspend fun loginSuspend(email: String, password: String): Boolean {
        val ok = repo.login(email, password)
        if (ok) repo.setSession(email.trim())
        return ok
    }

    fun logout() {
        viewModelScope.launch {
            repo.setSession(null)
        }
    }
}

