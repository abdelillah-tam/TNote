package com.example.tnote.ui.registerorlogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tnote.data.models.BackendlessUserLogin
import com.example.tnote.data.models.BackendlessUserRegister
import com.example.tnote.data.models.FacebookRequestBody
import com.example.tnote.data.models.UserEntity
import com.example.tnote.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _stateBackendlessUserRegister = MutableStateFlow<BackendlessUserRegister?>(null)
    val stateBackendlessUserRegister = _stateBackendlessUserRegister.asStateFlow()

    private val _stateBackendlessUserLogin = MutableStateFlow<BackendlessUserLogin?>(null)
    val stateBackendlessUserLogin = _stateBackendlessUserLogin.asStateFlow()

    private val _stateFacebookRequestBody = MutableStateFlow<FacebookRequestBody?>(null)
    val stateFacebookRequestBody = _stateFacebookRequestBody.asStateFlow()

    fun registerUsingFacebookViewModel(facebookRequestBody: FacebookRequestBody){
        viewModelScope.launch {
            authRepository.registerUsingFacebook(facebookRequestBody).collect{
                if (it != null){
                    val req = it
                    _stateFacebookRequestBody.update {
                        req
                    }
                }
            }
        }
    }

    fun logoutViewModel(facebookRequestBody: FacebookRequestBody){
        viewModelScope.launch {
            authRepository.logout(facebookRequestBody)
        }
    }

    fun registerBackendless(backendlessUserRegister: BackendlessUserRegister){
        viewModelScope.launch {
            authRepository.register(backendlessUserRegister).collect{ reg ->
                if (reg != null){
                    _stateBackendlessUserRegister.update {
                        reg
                    }
                }else{
                    _stateBackendlessUserRegister.update {
                        null
                    }
                }
            }
        }
    }

    fun loginBackendless(backendlessUserLogin: BackendlessUserLogin){
        viewModelScope.launch {
            authRepository
                .loginBackend(backendlessUserLogin)
                .collect{ backendLog ->
                    _stateBackendlessUserLogin.update {
                        backendLog
                    }
                }
        }
    }

    fun saveLoginInfosToDatabase(userEntity: UserEntity){
        viewModelScope.launch {
            authRepository
                .saveUserInfosToDatabase(userEntity)
                .collect{

                }
        }
    }

    fun checkIfValid(result: (Boolean) -> Unit){
        viewModelScope.launch {
            authRepository
                .checkIfValid()
                .collect{
                    result(it)
                }
        }
    }
}