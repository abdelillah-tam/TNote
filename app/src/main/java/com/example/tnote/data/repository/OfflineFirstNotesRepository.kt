package com.example.tnote.data.repository

import com.example.tnote.data.AppDatabase
import com.example.tnote.data.models.NoteEntity
import com.example.tnote.data.models.asExternalModel
import com.example.tnote.data.schedulers.NoteScheduler
import com.example.tnote.domain.models.Note
import com.example.tnote.domain.models.asEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineFirstNotesRepository @Inject constructor(
    private val appDb : AppDatabase,
    private val noteScheduler: NoteScheduler
) : NoteRepository {


    override fun getNotesStream(): Flow<List<Note>> = appDb
        .noteDao()
        .getAllNotes()
        .map {
            it.map(NoteEntity::asExternalModel)
        }

    override suspend fun getNote(id: Int) : Note = appDb
        .noteDao()
        .getNote(id)
        .asExternalModel()

    override suspend fun getNote(noteTitle: String): Note = appDb
        .noteDao()
        .getNote(noteTitle)
        .asExternalModel()

    override suspend fun addNewNote(note: Note) {
        val noteEntity = note.asEntity()
        appDb.noteDao().addNote(noteEntity)

        val noteFromDb = getNote(noteEntity.noteTitle!!)
        noteScheduler.scheduleNote(noteFromDb.id)

    }
}