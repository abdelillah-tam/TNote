package com.example.tnote.data.source

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class User(
    val objectId : String = "",
    val email : String = "",
    @SerializedName("user-token") val userToken : String = ""
)
