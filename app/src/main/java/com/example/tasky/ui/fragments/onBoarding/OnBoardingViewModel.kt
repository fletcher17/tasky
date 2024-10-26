package com.example.tasky.ui.fragments.onBoarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.auth.AuthSignUpRequest
import com.example.tasky.auth.UserSignInRequest
import com.example.tasky.auth.UserSignInResponse
import com.example.tasky.data.Repository
import com.example.tasky.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    val repository: Repository
) : ViewModel() {

    val loginResponse: MutableLiveData<NetworkResult<UserSignInResponse>> =
        MutableLiveData()

    fun signUp(userData: AuthSignUpRequest) {
        viewModelScope.launch {
            signUpCalls(userData)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginCalls(email, password)
        }
    }


    private suspend fun signUpCalls(userData: AuthSignUpRequest) {
        loginResponse.value = NetworkResult.Loading()
        try {
            repository.remote.signUp(userData)
            loginCalls(userData.email, userData.password)
        } catch (e: Throwable) {
            loginResponse.value = NetworkResult.Error(e.message.toString())
        }
    }

    private suspend fun loginCalls(email: String, password: String) {
        loginResponse.value = NetworkResult.Loading()
        try {
            val response = repository.remote.login(UserSignInRequest(email, password))
            loginResponse.value = handleApiResponse(response)
        } catch (e: Throwable) {
            loginResponse.value = NetworkResult.Error(e.message.toString())
        }
    }

    private fun handleApiResponse(response: Response<UserSignInResponse>): NetworkResult<UserSignInResponse> {
        return when {
            response.isSuccessful -> {
                val loginResponse = response.body()!!
                NetworkResult.Success(loginResponse)
            }

            response.code() == 401 -> {
                NetworkResult.Error(response.message())
            }

            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }
}