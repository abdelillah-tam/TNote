package com.example.tnote.ui.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.util.TimeUtils
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.window.layout.WindowMetricsCalculator
import com.example.tnote.R
import com.example.tnote.databinding.HomeFragmentBinding
import com.example.tnote.domain.models.Day
import com.example.tnote.domain.models.Note
import com.example.tnote.ui.task.TaskBottomSheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.sql.Time
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.*
import javax.inject.Inject

private const val TAG = "HomeFragment"

enum class WindowSizeClass { COMPACT, MEDIUM, EXPANDED }

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {


    private lateinit var binding: HomeFragmentBinding

    @Inject
    lateinit var noteListRecAdapter: NoteListRecAdapter

    @Inject
    lateinit var taskListRecAdapter: TaskListRecAdapter

    @Inject
    lateinit var tasksDayAdapter: TasksDayAdapter

    private val homeViewModel: HomeViewModel by viewModels()

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeFragmentBinding.bind(view)
        homeViewModel.getUser()

        homeViewModel.getNotesViewModel()

        calendarDays()


        val bottom = BottomSheetBehavior.from(binding.standardSheet.oerrt)
        bottom.isHideable = true
        bottom.state = BottomSheetBehavior.STATE_HIDDEN

        binding.standardSheet.addNoteToolbar.setNavigationOnClickListener {
            bottom.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.standardSheet.addNoteToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save_note -> {
                    val note = Note(
                        noteTitle = binding.standardSheet.addNoteTitle.editText!!.text.toString(),
                        noteText = binding.standardSheet.addNoteText.editText!!.text.toString(),
                        isSynchronized = false,
                        time = Calendar.getInstance().timeInMillis,
                        objectId = homeViewModel.objectId.value!!.objectId
                    )
                    homeViewModel.saveNoteViewModel(note)
                    true
                }
                else -> false
            }
        }
        val container: ViewGroup = binding.root
        container.addView(object : View(requireContext()) {
            override fun onConfigurationChanged(newConfig: Configuration?) {
                super.onConfigurationChanged(newConfig)
                computeWindowSizeClass()
            }
        })
        computeWindowSizeClass()

        binding.addNoteFloat.hide()
        binding.addThingFloat.hide()

        val gestureDetector = GestureDetectorCompat(requireContext(), object : GestureDetector.OnGestureListener{
            override fun onDown(p0: MotionEvent): Boolean {
                return false
            }

            override fun onShowPress(p0: MotionEvent) {

            }

            override fun onSingleTapUp(p0: MotionEvent): Boolean {
                val viewPosition = binding.days.findChildViewUnder(p0.x, p0.y)
                if (viewPosition != null){
                    val position = binding.days.getChildAdapterPosition(viewPosition)
                    val day = tasksDayAdapter.getDay(position)
                    homeViewModel.getTasksViewModel(day.startOfNextDay)
                }
                return true
            }

            override fun onScroll(
                p0: MotionEvent,
                p1: MotionEvent,
                p2: Float,
                p3: Float
            ): Boolean {
                return false
            }

            override fun onLongPress(p0: MotionEvent) {

            }

            override fun onFling(
                p0: MotionEvent,
                p1: MotionEvent,
                p2: Float,
                p3: Float
            ): Boolean {
                return false
            }

        })

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

        binding.days.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = tasksDayAdapter
            addOnItemTouchListener(object: RecyclerView.SimpleOnItemTouchListener(){
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    gestureDetector.onTouchEvent(e)
                    return false
                }
            })
        }




        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                homeViewModel.tasksState
                    .combine(homeViewModel.notesState) { tasks, notes->
                        if (tasks.isEmpty()) {
                            binding.noThingsToDo.visibility = MaterialTextView.VISIBLE
                            binding.tasksRecylerList.visibility = RecyclerView.INVISIBLE
                        } else {
                            taskListRecAdapter.setTaskList(tasks)
                            binding.noThingsToDo.visibility = MaterialTextView.INVISIBLE
                            binding.tasksRecylerList.visibility = RecyclerView.VISIBLE

                        }

                        if (notes.isEmpty()){
                            binding.noNotes.visibility = MaterialTextView.VISIBLE
                            binding.notesRecyclerList.visibility = RecyclerView.INVISIBLE
                        }else{
                            noteListRecAdapter.setNotes(notes)
                            binding.noNotes.visibility = MaterialTextView.INVISIBLE
                            binding.notesRecyclerList.visibility = RecyclerView.VISIBLE
                        }

                    }
                    .collect {
                        Log.e(TAG, "onViewCreated: $it" )
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
                bottom.let {
                    bottom.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }

        binding.addThingFloat.setOnClickListener {
            if (it.isShown) {
                val modalBottomSheet = TaskBottomSheet()
                modalBottomSheet.show(requireActivity().supportFragmentManager, TaskBottomSheet.TAG)
            }
        }


    }


    private fun computeWindowSizeClass() {
        val metrics = WindowMetricsCalculator
            .getOrCreate()
            .computeCurrentWindowMetrics(requireActivity())

        val widthDp = metrics.bounds.width() / resources.displayMetrics.density
        Log.e(TAG, "computeWindowSizeClass: width : $widthDp")

        val widthWindowSizeClass = when {
            widthDp < 600f -> WindowSizeClass.COMPACT
            widthDp < 840f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }


        val heightDp = metrics.bounds.height() /
                resources.displayMetrics.density
        Log.e(TAG, "computeWindowSizeClass: height : $heightDp")
        val heightWindowSizeClass = when {
            heightDp < 480f -> WindowSizeClass.COMPACT
            heightDp < 900f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }

    }


    private fun calendarDays() {
        val dateFormat = SimpleDateFormat("MMM\ndd")
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)


        val days = mutableListOf<Day>()

        homeViewModel.getTimesViewModel{
            if(it.isNotEmpty()){
                it.forEachIndexed { index, time ->
                    if (index == 0){
                        calendar.timeInMillis = time
                        calendar.add(Calendar.DATE, 1)
                        val finalDate = dateFormat.format(Date(time))
                        days.add(Day(finalDate, calendar.timeInMillis))
                        homeViewModel.getTasksViewModel(calendar.timeInMillis)
                    }else{
                        if (time >= calendar.timeInMillis){
                            calendar.timeInMillis = time
                            calendar.add(Calendar.DATE, 1)
                            val finalDate = dateFormat.format(Date(time))
                            days.add(Day(finalDate, calendar.timeInMillis))
                        }
                    }
                }
                tasksDayAdapter.addDays(days)
            }
        }



    }
}