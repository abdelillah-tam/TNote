package com.example.tnote.data.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tnote.data.AppDatabase
import com.example.tnote.data.models.NoteEntity
import com.example.tnote.data.models.asEntity
import com.example.tnote.data.models.asNetworkEntity
import com.example.tnote.data.repository.RemoteDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltWorker
class UploadNoteWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    val appDb: AppDatabase,
    val remoteDataSource: RemoteDataSource

) : CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {
        val id = inputData.getInt("id", -1)
        Log.e("TAG", "doWork: $id")
        if (id >= 0) {
            withContext(Dispatchers.IO) {
                val note = appDb.noteDao().getNote(id)

                syncNote(note)

            }
            return Result.success()
        }
        return Result.failure()
    }


    private suspend fun syncNote(noteEntity: NoteEntity) {
        remoteDataSource.uploadNote(noteEntity.asNetworkEntity()).collect {
            if (it != null) {
                appDb.noteDao().addNote(it.asEntity())
            }
        }
    }
}