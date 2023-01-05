package com.example.tnote.data.restapi

import com.example.tnote.data.models.NetworkNote
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NoteApi {


    @POST("7C335094-3221-C2EB-FFE8-C90412FD8F00/1551A224-ED03-4397-A466-3E0112702410/data/notes")
    fun addNote(@Body networkNote: NetworkNote): Call<NetworkNote>

}