package com.example.tasky.auth

sealed class AuthResult<T>(
    val data: T? = null,
    val message: T? = null
) {
    class Authorized<T>(data: T? = null) : AuthResult<T>(data)
    class Unauthorized<T>(message: T? = null) : AuthResult<T>(message)
    class UnKnownError<T> : AuthResult<T>()
    class Loading<T> : AuthResult<T>()
}