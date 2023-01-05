package com.example.tnote.data.workers

import android.content.Context
import android.util.Log
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.tnote.data.AppDatabase
import com.example.tnote.data.repository.RemoteDataSource
import com.google.common.util.concurrent.ListenableFuture
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "CheckIfValidWorker"
@HiltWorker
class CheckIfValidWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    val remoteDataSource: RemoteDataSource
) : ListenableWorker(context, workerParameters) {




    override fun startWork(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture { completer ->

            remoteDataSource.checkIfValid { result ->
                Log.e(TAG, "startWork: called" )
                if (result){
                    val data = Data.Builder().putBoolean("valid", result).build()
                    completer.set(Result.success(data))
                }else{
                    val data = Data.Builder().putBoolean("valid", result).build()
                    completer.set(Result.failure(data))
                }
            }

        }
    }
}



