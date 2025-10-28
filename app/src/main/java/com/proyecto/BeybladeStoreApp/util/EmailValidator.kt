package com.proyecto.BeybladeStoreApp.util

object EmailValidator {

    private val EMAIL_REGEX = """^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$""".toRegex()

    fun isValid(email: String?): Boolean {
        if (email.isNullOrBlank()) return false
        return EMAIL_REGEX.matches(email.trim())
    }
}

