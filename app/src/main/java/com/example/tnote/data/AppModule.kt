package com.example.tnote.data

import android.content.Context
import androidx.room.Room
import com.example.tnote.data.repository.*
import com.example.tnote.data.schedulers.NoteScheduler
import com.example.tnote.data.restapi.NoteApi
import com.example.tnote.data.restapi.TaskApi
import com.example.tnote.data.restapi.UserApi
import com.example.tnote.data.schedulers.TaskScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    fun getAppDatabase (@ApplicationContext appContext : Context) : AppDatabase {
        val db = Room
            .databaseBuilder(appContext, AppDatabase::class.java, "tnotedatabase")
            .build()
        return db
    }

    @Provides
    fun getNoteRepository(appDatabase: AppDatabase, noteScheduler: NoteScheduler, remoteDataSource: RemoteDataSource) : NoteRepository{
        return OfflineFirstNotesRepository(appDatabase, noteScheduler, remoteDataSource)
    }
    @Provides
    fun getTaskRepository(appDatabase: AppDatabase, taskScheduler: TaskScheduler, remoteDataSource: RemoteDataSource) : TaskRepository{
        return OfflineFirstTasksRepository(appDatabase, taskScheduler, remoteDataSource)
    }

    @Provides
    fun provideNoteScheduler(@ApplicationContext context: Context) : NoteScheduler {
        return NoteScheduler(context)
    }

    @Provides
    fun provideBackendlessUserApi() : UserApi{
        val retro = Retrofit.Builder()
            .baseUrl("https://handytreatment.backendless.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retro.create(UserApi::class.java)
    }

    @Provides
    fun provideBackendlessNoteApi() : NoteApi{
        val retro = Retrofit.Builder()
            .baseUrl("https://handytreatment.backendless.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retro.create(NoteApi::class.java)
    }

    @Provides
    fun provideBackendlessTaskApi() : TaskApi{
        val retro = Retrofit.Builder()
            .baseUrl("https://handytreatment.backendless.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retro.create(TaskApi::class.java)
    }
}