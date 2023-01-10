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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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


  /*  private val noteStream: Flow<Note> = noteRepository
        .getNote(id)
        .catch {
            emit(Note("", "", ""))
        }
*/

    fun getTasksViewModel() {
        viewModelScope.launch {
            taskRepository.getTasks()
                .collect{ tasksList ->
                    _tasksState.update {
                        tasksList
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