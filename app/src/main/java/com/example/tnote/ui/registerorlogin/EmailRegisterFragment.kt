package com.example.tnote.ui.registerorlogin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.tnote.R
import com.example.tnote.data.models.BackendlessUserRegister
import com.example.tnote.databinding.FragmentEmailRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmailRegisterFragment : Fragment(R.layout.fragment_email_register) {


    private lateinit var binding: FragmentEmailRegisterBinding

    private val registerViewModel: RegisterViewModel by viewModels()

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEmailRegisterBinding.bind(view)
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.register.setOnClickListener {
            val backendlessUserRegister = BackendlessUserRegister(
                binding.email.editText?.text.toString(),
                binding.password.editText?.text.toString()
            )
            registerViewModel.registerBackendless(backendlessUserRegister)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                registerViewModel.stateBackendlessUserRegister.collect {
                    if (it != null && !it.email.equals("")){
                        findNavController().navigate(R.id.action_emailRegisterFragment_to_emailLoginFragment)
                    }
                }
            }
        }
    }
}