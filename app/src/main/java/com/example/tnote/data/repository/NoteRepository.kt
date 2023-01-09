package com.example.tnote.data.repository

import com.example.tnote.data.models.NoteEntity
import com.example.tnote.domain.models.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotesStream() : Flow<List<Note>>

    suspend fun getNote(id: Int) : Note

    suspend fun getNote(noteTitle: String) : Note

    suspend fun addNewNote(note: Note)

    suspend fun getAllNotesFromDatabase(objectId: String) : Flow<Unit>
}