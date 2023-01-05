package com.example.tnote.data.models

import com.google.gson.annotations.SerializedName

data class BackendlessUserLogin(
    @SerializedName("objectId") val objectId: String = "",
    @SerializedName("user-token") val usertoken: String = "",
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String
)