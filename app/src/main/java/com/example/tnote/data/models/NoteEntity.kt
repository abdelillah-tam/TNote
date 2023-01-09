package com.example.tnote.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tnote.domain.models.Note

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val noteTitle: String?,
    val noteText: String?,
    var isSynchronized: Boolean,
    var time: Long,
    var objectId: String
)

fun NoteEntity.asExternalModel() = Note(id, noteTitle, noteText, isSynchronized, time, objectId)
fun NoteEntity.asNetworkEntity() = NetworkNote(id, noteTitle, noteText, true, time, objectId)

