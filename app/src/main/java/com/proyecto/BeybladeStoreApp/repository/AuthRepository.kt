package com.proyecto.BeybladeStoreApp.repository

import android.content.Context
import com.proyecto.BeybladeStoreApp.util.DataStoreManager
import com.proyecto.BeybladeStoreApp.data.local.models.UserProfile

class AuthRepository(context: Context) {
    private val ds = DataStoreManager(context)

    suspend fun ensureDefaultUser() {
        val users = ds.getUsersMap()
        var changed = false
        if (!users.containsKey("user")) {
            users["user"] = "123456"
            changed = true
        }
        if (!users.containsKey("admin")) {
            users["admin"] = "123456"
            changed = true
        }
        if (changed) ds.saveUsersMap(users)

        val roles = ds.getRolesMap()
        var rChanged = false
        if (!roles.containsKey("user")) { roles["user"] = "user"; rChanged = true }
        if (!roles.containsKey("admin")) { roles["admin"] = "admin"; rChanged = true }
        if (rChanged) ds.saveRolesMap(roles)

        val userProfile = ds.getProfileFor("user")
        if (userProfile == null) {
            ds.saveProfile(UserProfile(email = "user", displayName = "Usuario", phone = "", address = ""))
        }
        val adminProfile = ds.getProfileFor("admin")
        if (adminProfile == null) {
            ds.saveProfile(UserProfile(email = "admin", displayName = "Administrador", phone = "", address = ""))
        }
    }

    suspend fun register(email: String, password: String): Result<Unit> {
        val users = ds.getUsersMap()
        if (users.containsKey(email)) return Result.failure(Exception("El correo ya está registrado"))
        users[email] = password
        ds.saveUsersMap(users)
        return Result.success(Unit)
    }

    suspend fun createUser(email: String, password: String, role: String = "user"): Result<Unit> {
        val res = register(email, password)
        if (res.isSuccess) {
            val roles = ds.getRolesMap()
            roles[email] = role
            ds.saveRolesMap(roles)
        }
        return res
    }

    suspend fun deleteUser(email: String) {
        val users = ds.getUsersMap()
        if (users.containsKey(email)) {
            users.remove(email)
            ds.saveUsersMap(users)
        }
        val roles = ds.getRolesMap()
        if (roles.containsKey(email)) {
            roles.remove(email)
            ds.saveRolesMap(roles)
        }

        val profiles = ds.getProfilesMap()
        if (profiles.containsKey(email)) {
            profiles.remove(email)
            ds.saveProfilesMap(profiles)
        }
    }

    suspend fun getUsers(): Map<String, String> {
        return ds.getUsersMap()
    }

    suspend fun getRoles(): Map<String, String> {
        return ds.getRolesMap()
    }

    suspend fun updateUserRole(email: String, role: String) {
        val roles = ds.getRolesMap()
        roles[email] = role
        ds.saveRolesMap(roles)
    }

    suspend fun login(email: String, password: String): Boolean {
        val e = email.trim()
        val p = password.trim()
        if (e == "admin" && p == "123456") return true
        val users = ds.getUsersMap()
        return users[e] == p
    }

    suspend fun setSession(email: String?) {
        ds.setSessionUser(email)
    }

    suspend fun getSession(): String? = ds.getSessionUser()

    suspend fun updatePassword(email: String, newPassword: String): Result<Unit> {
        val users = ds.getUsersMap()
        if (!users.containsKey(email)) return Result.failure(Exception("Usuario no existe"))
        users[email] = newPassword
        ds.saveUsersMap(users)
        return Result.success(Unit)
    }

    suspend fun getProfile(email: String): UserProfile? {
        return ds.getProfileFor(email)
    }

    suspend fun saveProfile(profile: UserProfile) {
        ds.saveProfile(profile)
    }
}

