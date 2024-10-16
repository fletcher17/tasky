package com.example.tasky.auth

interface AuthRepository {

    suspend fun signUp(name: String, email: String, password: String): AuthResult<Unit>
    suspend fun login(email: String, password: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
    suspend fun getAccessToken(): AuthResult<Unit>
}