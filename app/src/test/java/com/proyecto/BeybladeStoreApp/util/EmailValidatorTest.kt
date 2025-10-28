package com.proyecto.BeybladeStoreApp.util

import org.junit.Assert.*
import org.junit.Test

class EmailValidatorTest {
    @Test
    fun validEmails() {
        val good = listOf(
            "user@example.com",
            "first.last@example.co",
            "a+b@example.io",
            "user_123@example-domain.com"
        )
        good.forEach { email -> assertTrue("should be valid: $email", EmailValidator.isValid(email)) }
    }

    @Test
    fun invalidEmails() {
        val bad = listOf(
            "",
            "plainaddress",
            "@no-local-part.com",
            "no-at-sign.com",
            "user@.invalid",
            "user@invalid.",
            "user@inv@lid.com"
        )
        bad.forEach { email -> assertFalse("should be invalid: $email", EmailValidator.isValid(email)) }
    }
}

