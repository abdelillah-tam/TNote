package com.example.tnote.data

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.tnote.data.source.local.NoteDao
import com.example.tnote.data.source.network.NetworkDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SynchronizeNotes @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    val noteRepository: NoteRepository
) : CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {
        val userToken = inputData.getString("userToken")
        if(userToken != null){
            noteRepository.synchronizeNotes(userToken)
            return Result.success()
        }

        return Result.failure()
    }


}
