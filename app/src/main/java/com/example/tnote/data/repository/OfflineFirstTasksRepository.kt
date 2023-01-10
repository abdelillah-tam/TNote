package com.example.tnote.data.repository

import com.example.tnote.data.AppDatabase
import com.example.tnote.data.models.*
import com.example.tnote.data.schedulers.TaskScheduler
import com.example.tnote.domain.models.Task
import com.example.tnote.domain.models.asEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineFirstTasksRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val taskScheduler: TaskScheduler,
    private val remoteDataSource: RemoteDataSource
) : TaskRepository{

    override fun getTasks(): Flow<List<Task>> = appDatabase
        .taskDao()
        .getAllTasks()
        .map {
            it.map(TaskEntity::asExternaleModel)
        }


    override suspend fun addNewTask(task: Task) {
        val taskEntity = task.asEntity()
        appDatabase.taskDao().addTask(taskEntity)

        val taskFromDb = appDatabase.taskDao().getTask(task.taskName!!)
        taskScheduler.scheduleTask(taskFromDb.id)
    }

    override suspend fun getAllTasksFromDatabase(objectId: String): Flow<Boolean> = flow {
        remoteDataSource
            .getAllTasks(objectId)
            .collect{
                if (it != null && it.isNotEmpty()){
                    val taskEntities = it.map(NetworkTask::asEntity)
                    appDatabase.taskDao().addAllTasks(taskEntities)
                    emit(true)
                }else{
                    emit(false)
                }
            }
    }


}