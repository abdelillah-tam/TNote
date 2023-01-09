package com.example.tnote.data.repository

import android.util.Log
import com.example.tnote.data.AppDatabase
import com.example.tnote.data.restapi.UserApi
import com.example.tnote.data.models.*
import com.example.tnote.data.restapi.NoteApi
import com.example.tnote.data.restapi.TaskApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.*
import javax.inject.Inject

private const val TAG = "RemoteDataSource"

class RemoteDataSource @Inject constructor(
    private val backendlessUserApi: UserApi,
    private val backendlessNoteApi: NoteApi,
    private val backendlessTaskApi: TaskApi,
    private val appDatabase: AppDatabase
) {


    fun uploadNote(note: NetworkNote): Flow<NetworkNote?> = flow {
        var networkNoteReturner: NetworkNote? = null
        withContext(Dispatchers.IO) {

            val ret = backendlessNoteApi
                .addNote(note)
                .awaitResponse()
            if (ret.isSuccessful) {
                if (ret.body() != null) {
                    networkNoteReturner = ret.body()
                }
            }
        }

        emit(networkNoteReturner)
    }

    fun registerUsingFacebook(facebookRequest: FacebookRequestBody): Flow<FacebookRequestBody?> =
        callbackFlow {
            withContext(Dispatchers.IO) {

                backendlessUserApi.registerByFacebook(facebookRequest)
                    .enqueue(object : Callback<FacebookRequestBody> {
                        override fun onResponse(
                            call: Call<FacebookRequestBody>,
                            response: Response<FacebookRequestBody>
                        ) {
                            if (response.body() != null) {
                                trySend(response.body())
                            }
                        }

                        override fun onFailure(call: Call<FacebookRequestBody>, t: Throwable) {

                        }

                    })

            }
            awaitClose {}
        }

    fun logout(userToken: String): Flow<Boolean> = callbackFlow {
        withContext(Dispatchers.IO) {
            backendlessUserApi.logout(userToken)
                .enqueue(object : Callback<Unit> {
                    override fun onResponse(
                        call: Call<Unit>,
                        response: Response<Unit>
                    ) {
                        if (response.body() != null && response.code() == 200) {
                            trySend(true)
                        } else {
                            trySend(false)
                        }
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        trySend(false)
                    }

                })
        }
        awaitClose { channel.close() }
    }

    fun registerBackend(backendlessUserRegister: BackendlessUserRegister): Flow<BackendlessUserRegister?> =
        callbackFlow {
            withContext(Dispatchers.IO) {
                backendlessUserApi
                    .register(backendlessUserRegister)
                    .enqueue(object : Callback<BackendlessUserRegister> {
                        override fun onResponse(
                            call: Call<BackendlessUserRegister>,
                            response: Response<BackendlessUserRegister>
                        ) {
                            if (response.code() == 200 && response.body() != null) {
                                trySend(response.body())

                            }
                        }

                        override fun onFailure(call: Call<BackendlessUserRegister>, t: Throwable) {
                            Log.e(TAG, "onFailure: ${t.printStackTrace()}")
                        }

                    })
            }
            awaitClose { channel.close() }
        }

    fun loginBackend(backendlessUserLogin: BackendlessUserLogin): Flow<BackendlessUserLogin?> =
        callbackFlow {
            withContext(Dispatchers.IO) {
                backendlessUserApi.login(backendlessUserLogin)
                    .enqueue(object : Callback<BackendlessUserLogin> {
                        override fun onResponse(
                            call: Call<BackendlessUserLogin>,
                            response: Response<BackendlessUserLogin>
                        ) {
                            if (response.code() == 200 && response.body() != null) {
                                trySend(response.body())

                            }
                        }

                        override fun onFailure(call: Call<BackendlessUserLogin>, t: Throwable) {
                            Log.e(TAG, "onFailure: ${t.printStackTrace()}")
                        }

                    })

            }

            awaitClose { channel.close() }
        }

    fun checkIfValid(result: (Boolean) -> Unit) {
        runBlocking(Dispatchers.IO) {
            val userEntity = appDatabase.userDao().getUser()
            if (userEntity.isNotEmpty()) {
                backendlessUserApi
                    .checkIfValid(userEntity[0].userToken)
                    .enqueue(object : Callback<Boolean> {
                        override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                            if (response.code() == 200 && response.body() != null && response.body() != false) {
                                result(response.body()!!)
                            } else {
                                runBlocking(Dispatchers.IO) {
                                    appDatabase.clearAllTables()
                                }
                            }
                        }

                        override fun onFailure(call: Call<Boolean>, t: Throwable) {
                            Log.e(TAG, "onFailure: ${t.printStackTrace()}")
                        }

                    })
            } else {
                result(false)
            }
        }

    }

    fun saveUserInfosToDatabase(userEntity: UserEntity): Flow<Boolean> = flow {
        appDatabase
            .userDao()
            .addUser(userEntity)

    }

    fun addTaskToDatabase(networkTask: NetworkTask) : Flow<NetworkTask?> = callbackFlow {
        withContext(Dispatchers.IO){
            backendlessTaskApi.addTask(networkTask)
                .enqueue(object : Callback<NetworkTask>{
                    override fun onResponse(
                        call: Call<NetworkTask>,
                        response: Response<NetworkTask>
                    ) {
                        if (response.code() == 200 && response.body() != null){
                            trySend(response.body()!!)
                        }else{
                            trySend(null)
                        }
                    }

                    override fun onFailure(call: Call<NetworkTask>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${t.printStackTrace()}" )
                    }

                })
        }

        awaitClose { channel.close() }
    }

    fun getUser() : Flow<UserEntity> = flow {
        val user = appDatabase.userDao().getUser()[0]
        emit(user)
    }

    suspend fun getAllTasks(objectId: String) : Flow<List<NetworkTask>> = callbackFlow {
        withContext(Dispatchers.IO){
            backendlessTaskApi
                .getAllTasks(objectId)
                .enqueue(object : Callback<List<NetworkTask>>{
                    override fun onResponse(
                        call: Call<List<NetworkTask>>,
                        response: Response<List<NetworkTask>>
                    ) {
                        if (response.code() == 200 && response.body()!!.isNotEmpty()){
                            trySend(response.body()!!)
                        }else{
                            trySend(emptyList())
                        }
                    }

                    override fun onFailure(call: Call<List<NetworkTask>>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${t.printStackTrace()}" )
                    }

                })
        }

        awaitClose{channel.close()}
    }


    suspend fun getAllNotes(objectId: String) : Flow<List<NetworkNote>> = callbackFlow {
        withContext(Dispatchers.IO){
            backendlessNoteApi
                .getAllNotes(objectId)
                .enqueue(object : Callback<List<NetworkNote>>{
                    override fun onResponse(
                        call: Call<List<NetworkNote>>,
                        response: Response<List<NetworkNote>>
                    ) {
                        if (response.code() == 200 && response.body()!!.isNotEmpty()){
                            trySend(response.body()!!)
                        }else{
                            trySend(emptyList())
                        }
                    }

                    override fun onFailure(call: Call<List<NetworkNote>>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${t.printStackTrace()}" )
                    }

                })
        }
        awaitClose { channel.close() }
    }
}