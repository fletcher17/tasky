package com.example.tasky

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasky.auth.AuthRepository
import com.example.tasky.auth.AuthResult
import com.example.tasky.auth.UserSignInResponse
import com.example.tasky.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: AuthRepository
) : AndroidViewModel(application) {


    private var authResponseChannel = Channel<NetworkResult<Unit>>()
    val authResults = authResponseChannel.receiveAsFlow()

    private var loginAuthResponse = Channel<NetworkResult<UserSignInResponse>>()
    val loginAuthResults = loginAuthResponse.receiveAsFlow()

    fun signUp(name: String, email:String, password: String) {
        viewModelScope.launch {
            authResponseChannel.send(NetworkResult.Loading())
            val result = repository.signUp(name, email, password)
            authResponseChannel.send(result)
        }
    }

    fun signIn(email:String, password: String) {
        viewModelScope.launch {
            loginAuthResponse.send(NetworkResult.Loading())
            val result = repository.login(email, password)
            loginAuthResponse.send(result)
        }
    }



}