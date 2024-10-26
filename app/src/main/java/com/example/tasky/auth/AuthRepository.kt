package com.example.tasky.auth

import com.example.tasky.util.NetworkResult

interface AuthRepository {

    suspend fun signUp(name: String, email: String, password: String): NetworkResult<UserSignInResponse>
    suspend fun login(email: String, password: String): NetworkResult<UserSignInResponse>
    suspend fun authenticate(): AuthResult<Unit>
    suspend fun getAccessToken(): AuthResult<Unit>

}