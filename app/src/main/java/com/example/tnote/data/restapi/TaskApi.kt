package com.example.tnote.data.restapi

import com.example.tnote.data.models.NetworkTask
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TaskApi {

    @POST("7C335094-3221-C2EB-FFE8-C90412FD8F00/1551A224-ED03-4397-A466-3E0112702410/data/tasks")
    fun addTask(@Body networkTask: NetworkTask) : Call<NetworkTask>

    @GET("7C335094-3221-C2EB-FFE8-C90412FD8F00/1551A224-ED03-4397-A466-3E0112702410/data/tasks")
    fun getAllTasks(@Query("where") objectId: String) : Call<List<NetworkTask>>

}