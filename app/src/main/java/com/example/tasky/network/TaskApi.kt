package com.example.tasky.network

import com.example.tasky.auth.AuthSignUpRequest
import com.example.tasky.auth.UserAccessTokenResponse
import com.example.tasky.auth.UserSignInRequest
import com.example.tasky.auth.UserSignInResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface TaskApi {

    @POST("register")
    suspend fun signUp(
        @Body request: AuthSignUpRequest
    )

    @POST("login")
    suspend fun logIn(
        @Body request: UserSignInRequest
    ): Response<UserSignInResponse>

    @GET("authenticate")
    suspend fun authenticate(
        @Header("Authorization") accessToken: String
    )

    @POST("accessToken")
    suspend fun getAccessToken(
        @Query("refreshToken") token: String, @Query("userId") userId: String
    ): Response<UserAccessTokenResponse>
}