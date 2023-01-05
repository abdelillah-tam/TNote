package com.example.tnote.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userentity")
data class UserEntity(
    @PrimaryKey var userToken: String,
    var objectId: String
)
