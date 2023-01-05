package com.example.tnote.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tnote.data.models.UserEntity
import com.example.tnote.data.repository.AuthRepository
import com.example.tnote.data.repository.TaskRepository
import com.example.tnote.domain.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val authRepository: AuthRepository
) : ViewModel() {


    private val _objectId = MutableStateFlow<UserEntity?>(null)
    val objectId = _objectId.asStateFlow()

    fun addTaskViewModel(task: Task){
        viewModelScope.launch {
            taskRepository.addNewTask(task)
        }
    }

    fun getUser(){
        viewModelScope.launch {
            authRepository.getUser().collect { userEntity ->
                _objectId.update {
                    userEntity
                }
            }
        }
    }
}