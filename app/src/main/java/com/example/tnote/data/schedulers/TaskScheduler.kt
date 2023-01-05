package com.example.tnote.data.schedulers

import android.content.Context
import androidx.work.*
import com.example.tnote.data.workers.UploadTaskWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TaskScheduler @Inject constructor(
    @ApplicationContext val context: Context
) {

    private val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

    fun scheduleTask(id: Int){
        val inputData = Data.Builder().putInt("id", id).build()
        val uploadWorkReq = OneTimeWorkRequestBuilder<UploadTaskWorker>()
            .addTag("KickManager")
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(uploadWorkReq)
    }
}