package com.example.tnote.ui.registerorlogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tnote.R
import com.example.tnote.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "RegisterActivity"
@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TNote)
        Log.e(TAG, "onCreate: called" )
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}