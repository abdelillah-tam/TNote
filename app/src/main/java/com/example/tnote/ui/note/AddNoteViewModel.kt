package com.example.tnote.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tnote.data.repository.NoteRepository
import com.example.tnote.data.repository.OfflineFirstNotesRepository
import com.example.tnote.domain.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {


    fun addNoteViewModel(note: Note){
        viewModelScope.launch {
            noteRepository.addNewNote(note)
        }
    }
}