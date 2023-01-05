package com.example.tnote.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tnote.data.repository.NoteRepository
import com.example.tnote.data.repository.TaskRepository
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
    noteRepository: NoteRepository
) : ViewModel() {

    //private var id: String = "Hello"

    private val _tasksState = MutableStateFlow<List<Task>>(emptyList())
    val tasksState = _tasksState.asStateFlow()

    init {
        getTasksViewModel()
    }
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
}