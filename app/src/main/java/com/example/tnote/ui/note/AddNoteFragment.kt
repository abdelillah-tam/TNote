package com.example.tnote.ui.note

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tnote.R
import com.example.tnote.databinding.FragmentAddNoteBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteFragment : BottomSheetDialogFragment(R.layout.fragment_add_note) {


    private val addNoteViewModel : AddNoteViewModel by viewModels()

    private lateinit var binding : FragmentAddNoteBinding

    companion object{
        const val TAG = "AddNoteFragment"
    }

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
                   /* val note = Note(noteTitle = binding.addNoteTitle.editText?.text.toString(),
                        noteText = binding.addNoteText.editText?.text.toString())

                    addNoteViewModel.addNoteViewModel(note)*/
                    true
                }
                else -> false
            }

        }
    }

}