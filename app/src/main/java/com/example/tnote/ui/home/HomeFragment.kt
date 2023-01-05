package com.example.tnote.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tnote.R
import com.example.tnote.databinding.HomeFragmentBinding
import com.example.tnote.ui.task.TaskBottomSheet
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {


    private lateinit var binding: HomeFragmentBinding

    @Inject
    lateinit var noteListRecAdapter: NoteListRecAdapter

    @Inject
    lateinit var taskListRecAdapter: TaskListRecAdapter

    private val homeViewModel: HomeViewModel by viewModels()

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeFragmentBinding.bind(view)

        binding.addNoteFloat.hide()
        binding.addThingFloat.hide()



        binding.notesRecyclerList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = noteListRecAdapter
        }
        binding.tasksRecylerList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = taskListRecAdapter
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                homeViewModel.tasksState
                    .collect {
                        if (it.size == 0){
                            binding.noThingsToDo.visibility = MaterialTextView.VISIBLE
                            binding.tasksRecylerList.visibility = RecyclerView.INVISIBLE
                        }else{
                            taskListRecAdapter.setTaskList(it)
                            binding.noThingsToDo.visibility = MaterialTextView.INVISIBLE
                            binding.tasksRecylerList.visibility = RecyclerView.VISIBLE

                        }
                    }
            }
        }

        binding.addFloat.setOnClickListener {
            if (binding.addNoteFloat.isShown
                && binding.addNoteFloat.isExtended
                && binding.addThingFloat.isShown
                && binding.addThingFloat.isExtended
            ) {
                binding.addNoteFloat.shrink()
                binding.addNoteFloat.hide()
                binding.addThingFloat.shrink()
                binding.addThingFloat.hide()
            } else {
                binding.addNoteFloat.show()
                binding.addNoteFloat.extend()
                binding.addThingFloat.show()
                binding.addThingFloat.extend()
            }
        }

        binding.addNoteFloat.setOnClickListener {
            if (it.isShown) {
                findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
            }
        }

        binding.addThingFloat.setOnClickListener {
            if (it.isShown) {
                val modalBottomSheet = TaskBottomSheet()
                modalBottomSheet.show(requireActivity().supportFragmentManager, TaskBottomSheet.TAG)
            }
        }


    }


}