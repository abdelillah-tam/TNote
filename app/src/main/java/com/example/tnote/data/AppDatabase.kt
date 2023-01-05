package com.example.tnote.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tnote.data.models.NoteEntity
import com.example.tnote.data.models.TaskEntity
import com.example.tnote.data.models.UserEntity

@Database(entities = [TaskEntity::class, NoteEntity::class, UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun noteDao() : NoteDao
    abstract fun taskDao() : TaskDao
    abstract fun userDao() : UserDao


}