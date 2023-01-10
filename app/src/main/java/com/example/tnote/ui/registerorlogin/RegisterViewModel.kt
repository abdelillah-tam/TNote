package com.example.tnote.ui.registerorlogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tnote.data.models.BackendlessUserLogin
import com.example.tnote.data.models.BackendlessUserRegister
import com.example.tnote.data.models.FacebookRequestBody
import com.example.tnote.data.models.UserEntity
import com.example.tnote.data.repository.AuthRepository
import com.example.tnote.data.repository.NoteRepository
import com.example.tnote.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val noteRepository: NoteRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _stateBackendlessUserRegister = MutableStateFlow<BackendlessUserRegister?>(null)
    val stateBackendlessUserRegister = _stateBackendlessUserRegister.asStateFlow()

    private val _stateBackendlessUserLogin = MutableStateFlow<BackendlessUserLogin?>(null)
    val stateBackendlessUserLogin = _stateBackendlessUserLogin.asStateFlow()

    private val _stateFacebookRequestBody = MutableStateFlow<FacebookRequestBody?>(null)
    val stateFacebookRequestBody = _stateFacebookRequestBody.asStateFlow()

    fun registerUsingFacebookViewModel(facebookRequestBody: FacebookRequestBody) {
        viewModelScope.launch {
            authRepository.registerUsingFacebook(facebookRequestBody).collect {
                if (it != null) {
                    val req = it
                    _stateFacebookRequestBody.update {
                        req
                    }
                }
            }
        }
    }

    fun logoutViewModel(facebookRequestBody: FacebookRequestBody) {
        viewModelScope.launch {
            authRepository.logout(facebookRequestBody)
        }
    }

    fun registerBackendless(backendlessUserRegister: BackendlessUserRegister) {
        viewModelScope.launch {
            authRepository.register(backendlessUserRegister).collect { reg ->
                if (reg != null) {
                    _stateBackendlessUserRegister.update {
                        reg
                    }
                } else {
                    _stateBackendlessUserRegister.update {
                        null
                    }
                }
            }
        }
    }

    fun loginBackendless(backendlessUserLogin: BackendlessUserLogin) {
        viewModelScope.launch {
            authRepository
                .loginBackend(backendlessUserLogin)
                .collect { backendLog ->
                    saveNotesAndTasksFromOnlineDatabaseToRoomDb(backendLog)
                }

        }
    }

    fun saveLoginInfosToDatabase(userEntity: UserEntity) {
        viewModelScope.launch {
            authRepository
                .saveUserInfosToDatabase(userEntity)
                .collect {

                }

        }
    }

    fun checkIfValid(result: (Boolean) -> Unit) {
        viewModelScope.launch {
            authRepository
                .checkIfValid()
                .collect {
                    result(it)
                }
        }
    }

    private fun saveNotesAndTasksFromOnlineDatabaseToRoomDb(backendLog: BackendlessUserLogin?) {
        viewModelScope.launch {
            if (backendLog != null) noteRepository
                .getAllNotesFromDatabase("objectIdOfUser = '${backendLog.objectId}'")
                .combine(taskRepository.getAllTasksFromDatabase("objectIdOfUser = '${backendLog.objectId}'"))
                { notesResult, tasksResult ->
                    if (notesResult && tasksResult) {
                        _stateBackendlessUserLogin.update {
                            backendLog
                        }
                    } else {
                        _stateBackendlessUserLogin.update {
                            backendLog
                        }
                    }

                }
                .collect {

                }
        }
    }

    /*private fun saveTasksFromOnlineDatabaseToRoomDb(objectId: String){
        viewModelScope.launch {
            taskRepository
                .getAllTasksFromDatabase("objectId = '${objectId}'")
                .collect{

                }
        }
    }*/
}