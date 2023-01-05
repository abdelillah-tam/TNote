package com.example.tnote.ui.task

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import com.example.tnote.R
import com.example.tnote.data.models.TaskEntity
import com.example.tnote.databinding.AddTaskBinding
import com.example.tnote.domain.models.Task
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskBottomSheet : BottomSheetDialogFragment(R.layout.add_task) {


    private lateinit var binding : AddTaskBinding

    private val taskViewModel : TaskViewModel by viewModels()

    companion object{
        const val TAG = "TaskBottomSheet"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddTaskBinding.bind(view)

        taskViewModel.getUser()

        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date:")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        if (binding.taskEditText.editText!!.requestFocus()){
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }


        binding.pickDate.setOnClickListener {
            datePicker.show(requireActivity().supportFragmentManager, TAG)
        }

        binding.addTaskNow.setOnClickListener {
            val task = Task(taskName = binding.taskEditText.editText!!.text.toString(),
                done = false, isSynchronized = false, time = datePicker.selection!!, objectId = taskViewModel.objectId.value!!.objectId)

            taskViewModel.addTaskViewModel(task)
            dismiss()
        }
    }
}