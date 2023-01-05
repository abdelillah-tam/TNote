package com.example.tnote.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tnote.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {



    fun checkIfValid(result : (Boolean) -> Unit){
        viewModelScope.launch {
            authRepository
                .checkIfValid()
                .collect {
                    result(it)
                }
        }
    }

}