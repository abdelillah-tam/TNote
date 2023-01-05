package com.example.tnote.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tnote.R
import com.example.tnote.databinding.FragmentAddNoteBinding
import com.example.tnote.domain.models.Note
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteFragment : Fragment(R.layout.fragment_add_note) {


    private val addNoteViewModel : AddNoteViewModel by viewModels()

    private lateinit var binding : FragmentAddNoteBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddNoteBinding.bind(view)

        binding.addNoteToolbar.setNavigationIcon(R.drawable.ic_back)
        binding.addNoteToolbar.setNavigationIconTint(ResourcesCompat.getColor(resources,R.color.black_2, null))
        binding.addNoteToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.addNoteToolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.save_note ->{
                    val note = Note(noteTitle = binding.addNoteTitle.editText?.text.toString(), noteText = binding.addNoteText.editText?.text.toString())

                    addNoteViewModel.addNoteViewModel(note)
                    true
                }
                else -> false
            }

        }
    }

}