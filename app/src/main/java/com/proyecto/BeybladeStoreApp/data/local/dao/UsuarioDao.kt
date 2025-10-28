package com.proyecto.BeybladeStoreApp.data.local.dao

import com.proyecto.BeybladeStoreApp.data.local.models.Usuario


interface UsuarioDao {
    fun getByEmail(email: String): Usuario? = null
    fun insertUsuario(usuario: Usuario) {}
    fun exists(email: String): Boolean = false
}

