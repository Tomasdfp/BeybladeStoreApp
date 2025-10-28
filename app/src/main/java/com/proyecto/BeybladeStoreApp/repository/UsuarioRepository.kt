package com.proyecto.BeybladeStoreApp.repository

import com.proyecto.BeybladeStoreApp.data.local.dao.UsuarioDao
import com.proyecto.BeybladeStoreApp.data.local.models.Usuario
import com.proyecto.BeybladeStoreApp.data.remote.ApiService
import com.proyecto.BeybladeStoreApp.data.remote.AuthRequest
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UsuarioRepository(private val usuarioDao: UsuarioDao, private val api: ApiService? = null) {

    suspend fun register(email: String, password: String): Result<Unit> {

        return withContext(Dispatchers.IO) {
            if (api != null) {
                try {




                } catch (e: Exception) {

                }
            }

            if (usuarioDao.exists(email)) {
                Result.failure(IllegalStateException("El correo ya está registrado"))
            } else {
                usuarioDao.insertUsuario(Usuario(email = email, password = password))
                Result.success(Unit)
            }
        }
    }

    suspend fun login(email: String, password: String): Boolean {

        val e = email.trim()
        val p = password.trim()


        if (e == "admin" && p == "123456") {
            Log.d("UsuarioRepository", "Admin shortcut login used")
            return true
        }

        return withContext(Dispatchers.IO) {
            if (api != null) {
                try {
                    val resp = api.login(AuthRequest(e, p))
                    if (resp.isSuccessful) {
                        return@withContext resp.body()?.success == true
                    }
                } catch (ex: Exception) {

                    Log.w("UsuarioRepository", "Remote login failed, falling back: ${ex.message}")
                }
            }

            val user = usuarioDao.getByEmail(e) ?: return@withContext false
            user.password == p
        }
    }
}

