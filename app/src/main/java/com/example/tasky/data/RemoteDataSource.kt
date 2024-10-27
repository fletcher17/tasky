package com.example.tasky.data

import com.example.tasky.auth.AuthSignUpRequest
import com.example.tasky.auth.UserSignInRequest
import com.example.tasky.auth.UserSignInResponse
import com.example.tasky.network.TaskApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val taskApi: TaskApi
) {

    suspend fun signUp(requestBody: AuthSignUpRequest) {
        return taskApi.signUp(requestBody)
    }

    suspend fun login(requestBody: UserSignInRequest): Response<UserSignInResponse> {
        return taskApi.logIn(requestBody)
    }

    suspend fun authenticate(accessToken: String) {
        return taskApi.authenticate(accessToken)
    }
}