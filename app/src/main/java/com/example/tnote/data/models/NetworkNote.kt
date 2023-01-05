package com.example.tnote.data.models

@kotlinx.serialization.Serializable
data class NetworkNote(
    val id: Int,
    val noteTitleNetwork: String?,
    val noteTextNetwork: String?
)

fun NetworkNote.asEntity() = NoteEntity(id, noteTitleNetwork, noteTextNetwork)