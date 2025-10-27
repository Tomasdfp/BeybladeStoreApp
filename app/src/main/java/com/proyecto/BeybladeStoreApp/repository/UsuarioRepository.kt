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
        // Prefer remote registration if API provided
        return withContext(Dispatchers.IO) {
            if (api != null) {
                try {
                    // Example endpoint - adapt to your Xano route
                    // val resp = api.register(RegisterRequest(email, password))
                    // if (resp.isSuccessful) return@withContext Result.success(Unit)
                    // For now, fallback to local
                } catch (e: Exception) {
                    // ignore and fallback
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
        // Normalize inputs (trim) to avoid invisible spaces causing failures
        val e = email.trim()
        val p = password.trim()

        // Accept built-in credentials (admin shortcut)
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
                    // network failed -> fallback to local
                    Log.w("UsuarioRepository", "Remote login failed, falling back: ${ex.message}")
                }
            }

            val user = usuarioDao.getByEmail(e) ?: return@withContext false
            user.password == p
        }
    }
}
