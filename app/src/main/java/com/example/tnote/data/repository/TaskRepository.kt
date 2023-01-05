package com.example.tnote.data.repository

import com.example.tnote.domain.models.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getTasks() : Flow<List<Task>>
    suspend fun addNewTask(task: Task)
}