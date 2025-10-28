package com.proyecto.BeybladeStoreApp.util

import android.content.Context

object SharedPrefs {
    private const val PREFS_NAME = "mylogin_prefs"
    private const val KEY_REMOTE_ENABLED = "remote_enabled"

    fun isRemoteEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_REMOTE_ENABLED, false)
    }

    fun setRemoteEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_REMOTE_ENABLED, enabled).apply()
    }
}
