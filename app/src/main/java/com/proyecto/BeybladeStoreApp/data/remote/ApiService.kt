package com.proyecto.BeybladeStoreApp.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class AuthRequest(val email: String, val password: String)
data class AuthResponse(val success: Boolean, val token: String?)

interface ApiService {
    @POST("/auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>
}

