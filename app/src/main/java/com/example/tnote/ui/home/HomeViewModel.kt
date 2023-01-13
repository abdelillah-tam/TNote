package com.example.tnote.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tnote.data.models.UserEntity
import com.example.tnote.data.repository.AuthRepository
import com.example.tnote.data.repository.NoteRepository
import com.example.tnote.data.repository.TaskRepository
import com.example.tnote.domain.models.Note
import com.example.tnote.domain.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val noteRepository: NoteRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    //private var id: String = "Hello"

    private val _objectId = MutableStateFlow<UserEntity?>(null)
    val objectId = _objectId.asStateFlow()


    private val _tasksState = MutableStateFlow<List<Task>>(emptyList())
    val tasksState = _tasksState.asStateFlow()

    private val _notesState = MutableStateFlow<List<Note>>(emptyList())
    val notesState = _notesState.asStateFlow()

    private val _tasksTimeState = MutableStateFlow<List<Long>>(emptyList())
    val tasksTimeState = _tasksTimeState.asStateFlow()



    fun getTasksViewModel(time: Long) {
        viewModelScope.launch {
            taskRepository.getTasks(time)
                .collect{ tasksList ->
                    _tasksState.update {
                        tasksList.sortedBy {
                            it.time
                        }
                    }
                }
        }
    }

    fun getNotesViewModel(){
        viewModelScope.launch {
            noteRepository
                .getNotesStream()
                .collect{ notesList ->
                    _notesState.update {
                        notesList
                    }
                }
        }
    }

    fun getTimesViewModel(times : (List<Long>) -> Unit){
        viewModelScope.launch {
            taskRepository.getTimes()
                .collect{ tasksTime ->
                    times(tasksTime)
                }
        }
    }

    fun saveNoteViewModel(note: Note){
        viewModelScope.launch {
            noteRepository.addNewNote(note)
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