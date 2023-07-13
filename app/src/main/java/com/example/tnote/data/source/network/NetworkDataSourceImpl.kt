package com.example.tnote.data.source.network

import android.util.Log
import com.example.tnote.data.BackendRetro
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "NotesNetworkDataSource"

class NotesNetworkDataSource @Inject constructor(
    private val backendRetro: BackendRetro
): NetworkDataSource {


    override suspend fun loadNotes(): List<NetworkNote> {
        backendRetro.loadNotes().enqueue(object : Callback<List<NetworkNote>>{
            override fun onResponse(call: Call<List<NetworkNote>>, response: Response<List<NetworkNote>>) {
                if(response.code() == 200){
                    Log.e(TAG, "onResponse: ${response.body()?.size}" )
                }
            }

            override fun onFailure(call: Call<List<NetworkNote>>, t: Throwable) {
                Log.e(TAG, "onFailure: called" )
            }

        })

        return emptyList()
    }

    override suspend fun saveNotes(networkNotes: List<NetworkNote>) {
        backendRetro.saveNotes().enqueue(object: Callback<Int>{
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if(response.code() == 200){

                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.e(TAG, "onFailure: called" )
            }

        })
    }

    override suspend fun signUp() {
    }


}