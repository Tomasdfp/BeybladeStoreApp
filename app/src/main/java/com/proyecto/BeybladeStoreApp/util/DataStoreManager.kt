package com.proyecto.BeybladeStoreApp.util

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.proyecto.BeybladeStoreApp.data.local.models.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "beyblade_prefs")

class DataStoreManager(private val context: Context) {
    private val gson = Gson()

    companion object {
        val USERS_KEY = stringPreferencesKey("users_json")
        val ROLES_KEY = stringPreferencesKey("user_roles_json")
        val SESSION_KEY = stringPreferencesKey("session_user")
        val CART_KEY = stringPreferencesKey("cart_json")
        val ORDERS_KEY = stringPreferencesKey("orders_json")
        val PRODUCTS_KEY = stringPreferencesKey("products_json")
        val PROFILES_KEY = stringPreferencesKey("profiles_json")
    }

    suspend fun getUsersMap(): MutableMap<String, String> {
        val prefs = context.dataStore.data.first()
        val json = prefs[USERS_KEY] ?: "{}"

        return try {
            val type = com.google.gson.reflect.TypeToken.getParameterized(Map::class.java, String::class.java, String::class.java).type
            val parsed: Map<String, String>? = gson.fromJson(json, type)
            parsed?.toMutableMap() ?: mutableMapOf()
        } catch (e: Exception) {
            mutableMapOf()
        }
    }

    suspend fun saveUsersMap(map: Map<String, String>) {
        val json = gson.toJson(map)
        context.dataStore.edit { prefs ->
            prefs[USERS_KEY] = json
        }
    }

    suspend fun setSessionUser(email: String?) {
        context.dataStore.edit { prefs ->
            if (email == null) prefs.remove(SESSION_KEY) else prefs[SESSION_KEY] = email
        }
    }

    suspend fun getSessionUser(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[SESSION_KEY]
    }


    suspend fun saveCartJson(json: String) {
        context.dataStore.edit { prefs ->
            prefs[CART_KEY] = json
        }
    }

    suspend fun getCartJson(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[CART_KEY]
    }


    fun cartJsonFlow(): Flow<String?> = context.dataStore.data.map { prefs -> prefs[CART_KEY] }

    fun productCatalogJsonFlow(): Flow<String?> = context.dataStore.data.map { prefs -> prefs[PRODUCTS_KEY] }

    fun ordersJsonFlow(): Flow<String?> = context.dataStore.data.map { prefs -> prefs[ORDERS_KEY] }

    suspend fun getProfilesMap(): MutableMap<String, String> {
        val prefs = context.dataStore.data.first()
        val json = prefs[PROFILES_KEY] ?: "{}"
        return try {
            val type = com.google.gson.reflect.TypeToken.getParameterized(Map::class.java, String::class.java, String::class.java).type
            val parsed: Map<String, String>? = gson.fromJson(json, type)
            parsed?.toMutableMap() ?: mutableMapOf()
        } catch (e: Exception) {
            mutableMapOf()
        }
    }

    suspend fun saveProfilesMap(map: Map<String, String>) {
        val json = gson.toJson(map)
        context.dataStore.edit { prefs ->
            prefs[PROFILES_KEY] = json
        }
    }

    suspend fun getProfileFor(email: String): UserProfile? {
        val map = getProfilesMap()
        val json = map[email] ?: return null
        return try {
            gson.fromJson(json, UserProfile::class.java)
        } catch (e: Exception) { null }
    }

    suspend fun saveProfile(profile: UserProfile) {
        val map = getProfilesMap()
        map[profile.email] = gson.toJson(profile)
        saveProfilesMap(map)
    }


    suspend fun saveProductCatalogJson(json: String) {
        context.dataStore.edit { prefs ->
            prefs[PRODUCTS_KEY] = json
        }
    }

    suspend fun saveOrdersJson(json: String) {
        context.dataStore.edit { prefs ->
            prefs[ORDERS_KEY] = json
        }
    }

    suspend fun getProductCatalogJson(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[PRODUCTS_KEY]
    }

    suspend fun getRolesMap(): MutableMap<String, String> {
        val prefs = context.dataStore.data.first()
        val json = prefs[ROLES_KEY] ?: "{}"
        return try {
            val type = com.google.gson.reflect.TypeToken.getParameterized(Map::class.java, String::class.java, String::class.java).type
            val parsed: Map<String, String>? = gson.fromJson(json, type)
            parsed?.toMutableMap() ?: mutableMapOf()
        } catch (e: Exception) {
            mutableMapOf()
        }
    }

    suspend fun saveRolesMap(map: Map<String, String>) {
        val json = gson.toJson(map)
        context.dataStore.edit { prefs ->
            prefs[ROLES_KEY] = json
        }
    }

    suspend fun getOrdersJson(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[ORDERS_KEY]
    }
}

