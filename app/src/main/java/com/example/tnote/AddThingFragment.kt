package com.example.tnote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.tnote.databinding.FragmentAddThingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddThingFragment : Fragment(R.layout.fragment_add_thing) {

    private lateinit var binding : FragmentAddThingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddThingBinding.bind(view)
    }
}