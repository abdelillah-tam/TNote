package com.example.tnote.ui.home

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.PaintCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.tnote.R
import com.example.tnote.domain.models.Task
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView
import javax.inject.Inject

class TaskListRecAdapter
@Inject constructor() : RecyclerView.Adapter<TaskListRecAdapter.TaskViewHolder>() {

    private var taskList = mutableListOf<Task>()

    inner class TaskViewHolder(itemView: View) : ViewHolder(itemView) {
        val taskCheckbox = itemView.findViewById(R.id.task_checker) as MaterialCheckBox
        val taskText = itemView.findViewById(R.id.task_text) as MaterialTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        if (taskList.isNotEmpty() && position < taskList.size) {
            val task = taskList[position]

            holder.taskText.text = task.taskName
            if (task.done) {
                holder.taskCheckbox.isChecked = task.done
                holder.taskText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.taskCheckbox.isChecked = task.done
                holder.taskText.paintFlags = Paint.ANTI_ALIAS_FLAG
            }

            holder.taskCheckbox.addOnCheckedStateChangedListener { checkBox, state ->
                if (state == MaterialCheckBox.STATE_CHECKED) {
                    holder.taskText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    holder.taskText.paintFlags = Paint.ANTI_ALIAS_FLAG
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (taskList.isEmpty()) {
            0
        }
        else if(taskList.size < 3){
            taskList.size
        }
        else{
            4
        }
    }

    fun setTaskList(list: List<Task>) {
        val diff = Diff(taskList, list)
        val diffResult = DiffUtil.calculateDiff(diff)

        taskList.clear()
        taskList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    class Diff(
        val oldTaskList: List<Task>,
        val newTaskList: List<Task>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldTaskList.size
        }

        override fun getNewListSize(): Int {
            return newTaskList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldTaskList[oldItemPosition].taskName.equals(newTaskList[newItemPosition].taskName)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldTaskList[oldItemPosition].equals(newItemPosition)
        }
    }
}