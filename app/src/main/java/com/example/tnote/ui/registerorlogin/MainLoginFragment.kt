package com.example.tnote.ui.registerorlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tnote.ui.main.MainActivity
import com.example.tnote.R
import com.example.tnote.data.models.FacebookRequestBody
import com.example.tnote.data.models.UserEntity
import com.example.tnote.databinding.FragmentMainLoginBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainLoginFragment : Fragment(R.layout.fragment_main_login) {


    private lateinit var binding : FragmentMainLoginBinding

    private lateinit var callbackManager: CallbackManager

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainLoginBinding.bind(view)

        callbackManager = CallbackManager.Factory.create()
        val login = LoginManager.getInstance()


        login.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                Log.e("TAG", "onCancel: called")
            }

            override fun onError(error: FacebookException) {
                Log.e("TAG", "onError: called")
            }

            override fun onSuccess(result: LoginResult) {
                val reg = FacebookRequestBody(result.accessToken.token, "", "")
                registerViewModel.registerUsingFacebookViewModel(reg)
            }

        })

        binding.loginWithEmail.setOnClickListener {
            findNavController().navigate(R.id.action_mainRegisterFragment_to_emailLoginFragment)
        }

        binding.loginWithFacebook.setOnClickListener {
            login.logInWithReadPermissions(this, listOf("email", "public_profile"))
        }

        binding.registernewaccount.setOnClickListener {
            findNavController().navigate(R.id.action_mainRegisterFragment_to_emailRegisterFragment)
        }

        lifecycleScope.launch{
            registerViewModel
                .stateFacebookRequestBody
                .collect{
                    if (it != null){
                        registerViewModel.saveLoginInfosToDatabase(UserEntity(it.usertoken, it.email))
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}