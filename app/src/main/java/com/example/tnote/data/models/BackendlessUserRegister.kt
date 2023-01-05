package com.example.tnote.data.models

import com.google.gson.annotations.SerializedName

data class BackendlessUserRegister(
 @SerializedName("email") var email: String,
 @SerializedName("password") var password: String
)
