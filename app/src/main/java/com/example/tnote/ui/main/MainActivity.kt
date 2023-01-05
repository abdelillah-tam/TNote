package com.example.tnote.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.tnote.R
import com.example.tnote.ui.registerorlogin.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.Interceptor

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private var res = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TNote)
        mainViewModel.checkIfValid { result ->
            res = result
            if (result) {
                setContentView(R.layout.activity_main)
            }else{
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        splash.setKeepOnScreenCondition {
            false
        }

    }


}