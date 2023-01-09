package com.example.tnote.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tnote.data.models.TaskEntity
import com.example.tnote.domain.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAllTasks() : Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTask(id: Int) : TaskEntity

    @Query("SELECT * FROM tasks WHERE taskName = :taskName")
    suspend fun getTask(taskName: String) : TaskEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(taskEntity: TaskEntity)

    @Insert
    suspend fun addAllTasks(taskEntities: List<TaskEntity>)

}