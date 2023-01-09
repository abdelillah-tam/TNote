package com.example.tnote.data.repository

import com.example.tnote.data.models.NetworkTask
import com.example.tnote.domain.models.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getTasks() : Flow<List<Task>>
    suspend fun addNewTask(task: Task)
    suspend fun getAllTasksFromDatabase(objectId: String) : Flow<Unit>
}