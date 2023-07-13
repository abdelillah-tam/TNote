package com.example.tnote.data

import android.content.Context
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SynchronizingScheduler @Inject constructor(
    @ApplicationContext val context: Context
) {

    private val uploadConstraints = Constraints
        .Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    fun scheduleNoteSynchronizing(userToken: String){
        val inputData = Data.Builder().putString("userToken", userToken).build()
        val uploadWorkReq = OneTimeWorkRequestBuilder<SynchronizeNotes>()
            .addTag("WorkManager")
            .setConstraints(uploadConstraints)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(uploadWorkReq)
    }

}