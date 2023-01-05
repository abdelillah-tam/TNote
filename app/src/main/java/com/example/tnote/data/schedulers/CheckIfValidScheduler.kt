package com.example.tnote.data.schedulers

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.tnote.data.workers.CheckIfValidWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

private const val TAG = "CheckIfValidScheduler"
class CheckIfValidScheduler @Inject constructor(
    @ApplicationContext val context: Context
) {

    private val checkingConstraints =
        Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


    fun scheduleChecking(): Flow<UUID> = flow {
        Log.e(TAG, "scheduleChecking: called" )
        val checkWorkReq = OneTimeWorkRequestBuilder<CheckIfValidWorker>().addTag("Checking")
            .setConstraints(checkingConstraints)
            .build()

        WorkManager
            .getInstance(context)
            .enqueue(checkWorkReq)

        emit(checkWorkReq.id)

    }

}