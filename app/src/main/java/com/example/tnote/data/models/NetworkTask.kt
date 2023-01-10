package com.example.tnote.data.models

@kotlinx.serialization.Serializable
data class NetworkTask(
    val id: Int,
    val taskName: String?,
    var done: Boolean,
    var isSynchronized: Boolean,
    var time: Long,
    var objectIdOfUser: String
)

fun NetworkTask.asEntity() = TaskEntity(
    id = id,
    taskName = taskName,
    done = done,
    isSynchronized = isSynchronized,
    time = time,
    objectId = objectIdOfUser
)