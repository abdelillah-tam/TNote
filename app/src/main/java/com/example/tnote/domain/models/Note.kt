package com.example.tnote.domain.models

import com.example.tnote.data.models.NoteEntity

data class Note(
    val id: Int = -1,
    val noteTitle: String?,
    val noteText: String?
)

fun Note.asEntity() = NoteEntity(noteTitle = noteTitle, noteText = noteText)