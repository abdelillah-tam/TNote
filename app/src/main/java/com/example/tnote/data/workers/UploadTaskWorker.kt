package com.example.tnote.data.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tnote.data.AppDatabase
import com.example.tnote.data.models.TaskEntity
import com.example.tnote.data.models.asEntity
import com.example.tnote.data.models.asExternaleModel
import com.example.tnote.data.models.asNetworkTask
import com.example.tnote.data.repository.RemoteDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "UploadTaskWorker"
@HiltWorker
class UploadTaskWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    val appDatabase: AppDatabase,
    val remoteDataSource: RemoteDataSource
) : CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {
        val id = inputData.getInt("id", -1)
        Log.e(TAG, "doWork: $id")
        if (id >= 0){
            withContext(Dispatchers.IO){
                val taskEntity = appDatabase.taskDao().getTask(id)
                if (!taskEntity.isSynchronized) {
                    return@withContext syncTask(taskEntity)
                }else{
                    return@withContext Result.failure()
                }
            }
        }
        return Result.failure()
    }

    private suspend fun syncTask(taskEntity: TaskEntity) :Result{
        remoteDataSource.addTaskToDatabase(taskEntity.asNetworkTask())
            .collect{
                if (it != null){
                    appDatabase.taskDao().addTask(it.asEntity())
                }
            }
        return Result.success()
    }

}