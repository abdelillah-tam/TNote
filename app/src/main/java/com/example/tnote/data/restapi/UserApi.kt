package com.example.tnote.data.restapi

import com.example.tnote.data.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {


    @Headers("Content-Type: application/json")
    @POST("7C335094-3221-C2EB-FFE8-C90412FD8F00/1551A224-ED03-4397-A466-3E0112702410/users/oauth/facebook/login")
    fun registerByFacebook(@Body facebookRequestBody: FacebookRequestBody): Call<FacebookRequestBody>


    @Headers("Content-Type: application/json")
    @POST("7C335094-3221-C2EB-FFE8-C90412FD8F00/1551A224-ED03-4397-A466-3E0112702410/users/register")
    fun register(@Body backendlessUser: BackendlessUserRegister
    ): Call<BackendlessUserRegister>

    @Headers("Content-Type: application/json")
    @POST("7C335094-3221-C2EB-FFE8-C90412FD8F00/1551A224-ED03-4397-A466-3E0112702410/users/login")
    fun login(@Body backendlessUser: BackendlessUserLogin): Call<BackendlessUserLogin>

    @GET("7C335094-3221-C2EB-FFE8-C90412FD8F00/1551A224-ED03-4397-A466-3E0112702410/users/logout")
    fun logout(@Header("user-token") userToken: String) : Call<Unit>

    @GET("7C335094-3221-C2EB-FFE8-C90412FD8F00/1551A224-ED03-4397-A466-3E0112702410/users/isvalidusertoken/{usertoken}")
    fun checkIfValid(@Path("usertoken") userToken: String) : Call<Boolean>
}