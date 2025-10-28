package com.proyecto.BeybladeStoreApp.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.BeybladeStoreApp.repository.UsuarioRepository
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    fun login(email: String, password: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val ok = repository.login(email, password)
            callback(ok)
        }
    }

    fun register(email: String, password: String, callback: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            val res = repository.register(email, password)
            callback(res)
        }
    }
}

