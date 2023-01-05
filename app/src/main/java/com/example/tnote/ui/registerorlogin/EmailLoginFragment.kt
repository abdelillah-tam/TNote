package com.example.tnote.ui.registerorlogin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.tnote.ui.main.MainActivity
import com.example.tnote.R
import com.example.tnote.data.models.BackendlessUserLogin
import com.example.tnote.data.models.UserEntity
import com.example.tnote.databinding.FragmentEmailLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmailLoginFragment : Fragment(R.layout.fragment_email_login) {


    private lateinit var binding: FragmentEmailLoginBinding

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEmailLoginBinding.bind(view)

        binding.login.setOnClickListener {
            val backendlessUserLogin = BackendlessUserLogin(
                login = binding.emailLogin.editText!!.text.toString(),
                password = binding.passwordLogin.editText!!.text.toString()
            )

            registerViewModel.loginBackendless(backendlessUserLogin)
        }

        lifecycleScope.launch {
            registerViewModel.stateBackendlessUserLogin.collect{
                if (it != null) {
                    registerViewModel.saveLoginInfosToDatabase(UserEntity(it.usertoken, it.objectId))
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }
    }
}