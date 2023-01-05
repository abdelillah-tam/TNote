package com.example.tnote.domain.models

import com.example.tnote.data.models.TaskEntity

data class Task(
    var id : Int = 0,
    val taskName: String?,
    var done: Boolean,
    var isSynchronized: Boolean,
    var time: Long,
    var objectId: String
)

fun Task.asEntity() = TaskEntity(taskName = taskName, done = done, isSynchronized = isSynchronized, time = time, objectId = objectId)

