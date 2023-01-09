package com.example.tnote.data.models

@kotlinx.serialization.Serializable
data class NetworkNote(
    val id: Int,
    val noteTitleNetwork: String?,
    val noteTextNetwork: String?,
    var isSynchronized: Boolean,
    var time: Long,
    var objectId: String
)

fun NetworkNote.asEntity() = NoteEntity(id, noteTitleNetwork, noteTextNetwork, isSynchronized, time, objectId)