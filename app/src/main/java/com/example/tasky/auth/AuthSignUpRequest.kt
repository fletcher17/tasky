package com.example.tasky.auth

data class AuthSignUpRequest(
    val fullName: String,
    val email: String,
    val password: String
)
