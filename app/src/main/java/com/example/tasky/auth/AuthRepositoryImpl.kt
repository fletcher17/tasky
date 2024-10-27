package com.example.tasky.auth

import android.util.Log
import com.example.tasky.data.DataStoreRepository
import com.example.tasky.network.TaskApi
import com.example.tasky.util.NetworkResult
import kotlinx.coroutines.flow.first
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val api: TaskApi,
    private val dataStoreRepository: DataStoreRepository
) : AuthRepository {
    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): NetworkResult<Unit> {

        return try {
            api.signUp(
                AuthSignUpRequest(
                    name, email, password
                )
            )
            login(email, password)
            NetworkResult.Success()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                NetworkResult.Error(e.message.toString())
            } else if (e.code() == 409) {
                NetworkResult.Error(e.message.toString())
            } else {
                NetworkResult.Error(e.message.toString())
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message.toString())
        }
    }

    override suspend fun login(email: String, password: String): NetworkResult<UserSignInResponse> {
        return try {
            val response = api.logIn(
                UserSignInRequest(
                    email, password
                )
            )
            val userData = response.body()
            if (userData != null) {
                dataStoreRepository.saveUserDetails(
                    userData.accessToken,
                    userData.accessTokenExpirationTimestamp,
                    userData.fullName,
                    userData.refreshToken,
                    userData.userId
                )
            }
            Log.d(
                "response",
                "${response} and ${response.body()} and ${response.message()} and code ${response.code()}"
            )
            NetworkResult.Success(response.body()!!)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                Log.d(
                    "response error 401",
                    " and ${e.message()} and code ${e.code()}"
                )
                NetworkResult.Error(e.message.toString())
            } else {
                NetworkResult.Error(e.message.toString())
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message.toString())
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = dataStoreRepository.readCurrentUserDetails.first().accessToken
            api.authenticate(token)
            AuthResult.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnKnownError()
            }
        } catch (e: Exception) {
            AuthResult.UnKnownError()
        }
    }

    override suspend fun getAccessToken(
    ): AuthResult<Unit> {
        return try {
            val userDetails = dataStoreRepository.readCurrentUserDetails.first()
            api.getAccessToken(
                userDetails.refreshToken, userDetails.userId
            )
            AuthResult.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnKnownError()
            }
        } catch (e: Exception) {
            AuthResult.UnKnownError()
        }
    }
}