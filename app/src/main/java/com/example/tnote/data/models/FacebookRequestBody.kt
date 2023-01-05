package com.example.tnote.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.json.Json

data class FacebookRequestBody(
    val accessToken: String,
    @SerializedName("email") var email: String,
    @SerializedName("id") val id: String,
    @SerializedName("user-token") val usertoken: String = ""
)
