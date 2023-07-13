package com.example.tnote.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tnote.data.source.network.NetworkDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthPresenter @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : ViewModel() {

    fun loginUsingFacebook(accessToken: String) : Flow<Boolean> = flow {
        viewModelScope.launch {
            networkDataSource
                .loginUsingFacebook(accessToken).collect{ user ->
                    if(user != null){
                        emit(true)
                    }
                }
        }
    }

}
