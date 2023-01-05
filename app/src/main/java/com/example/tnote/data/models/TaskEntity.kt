package com.example.tnote.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tnote.domain.models.Task

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) var id : Int = 0,
    val taskName: String?,
    var done: Boolean,
    var isSynchronized: Boolean,
    var time: Long,
    var objectId: String
)


fun TaskEntity.asExternaleModel() = Task(id,taskName, done, isSynchronized, time, objectId)
fun TaskEntity.asNetworkTask() = NetworkTask(id,taskName, done, true, time, objectId)