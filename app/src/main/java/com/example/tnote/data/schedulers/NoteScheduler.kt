package com.example.tnote.data.schedulers

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.tnote.data.workers.UploadNoteWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NoteScheduler @Inject constructor(
    @ApplicationContext val context: Context
) {

    private val uploadConstraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

    fun scheduleNote(id: Int){
        val inputData = Data.Builder().putInt("id", id).build()
        val uploadWorkReq = OneTimeWorkRequestBuilder<UploadNoteWorker>().addTag("WorkManager")
            .setConstraints(uploadConstraints)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(uploadWorkReq)
    }

}