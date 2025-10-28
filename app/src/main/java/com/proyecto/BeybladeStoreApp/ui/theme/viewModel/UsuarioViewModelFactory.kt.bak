package com.proyecto.BeybladeStoreApp.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.proyecto.BeybladeStoreApp.repository.UsuarioRepository

class UsuarioViewModelFactory(private val repository: UsuarioRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UsuarioViewModel(repository) as T
    }
}
