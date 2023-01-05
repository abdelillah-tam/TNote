package com.example.tnote.data.repository


import android.content.Context
import android.util.Log
import androidx.concurrent.futures.await
import androidx.lifecycle.asFlow
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.tnote.data.models.BackendlessUserLogin
import com.example.tnote.data.models.BackendlessUserRegister
import com.example.tnote.data.models.FacebookRequestBody
import com.example.tnote.data.models.UserEntity
import com.example.tnote.data.schedulers.CheckIfValidScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

private const val TAG = "AuthRepository"
class AuthRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val remoteDataSource: RemoteDataSource,
    private val checkIfValidScheduler: CheckIfValidScheduler
) {


    fun registerUsingFacebook(facebookRequestBody: FacebookRequestBody): Flow<FacebookRequestBody?> =
        flow {
            remoteDataSource.registerUsingFacebook(facebookRequestBody).collect {
                emit(it)
            }
        }

    fun logout(facebookRequestBody: FacebookRequestBody) {
        //remoteDataSource.logout(facebookRequestBody)
    }

    fun loginBackend(backendlessUserLogin: BackendlessUserLogin): Flow<BackendlessUserLogin?> =
        flow {
            remoteDataSource
                .loginBackend(backendlessUserLogin)
                .collect {
                    emit(it)
                }
        }

    fun register(backendlessUserRegister: BackendlessUserRegister): Flow<BackendlessUserRegister?> =
        flow {
            remoteDataSource
                .registerBackend(backendlessUserRegister)
                .collect {
                    emit(it)
                }
        }

    fun saveUserInfosToDatabase(userEntity: UserEntity) : Flow<Boolean> = flow {
        remoteDataSource
            .saveUserInfosToDatabase(userEntity)
            .collect{

            }
    }

    fun checkIfValid() : Flow<Boolean> = callbackFlow {
        checkIfValidScheduler.scheduleChecking().collect{ uuid ->

            WorkManager.getInstance(context)
                .getWorkInfoByIdLiveData(uuid)
                .asFlow()
                .collect{ workInfo ->
                    if (workInfo.state == WorkInfo.State.SUCCEEDED){
                        val resultData = workInfo.outputData.getBoolean("valid", false)
                        Log.e(TAG, "checkIfValid: $resultData" )
                        trySend(resultData)
                    }else if(workInfo.state == WorkInfo.State.FAILED){
                        trySend(false)
                    }
                }


        }



        awaitClose{channel.close()}
    }

    fun getUser() : Flow<UserEntity> = flow {
        remoteDataSource
            .getUser()
            .collect{
                emit(it)
            }
    }
}