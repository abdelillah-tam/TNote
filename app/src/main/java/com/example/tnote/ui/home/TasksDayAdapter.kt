package com.example.tnote.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tnote.R
import com.example.tnote.domain.models.Day
import com.google.android.material.textview.MaterialTextView
import javax.inject.Inject

class TasksDayAdapter @Inject constructor() : RecyclerView.Adapter<TasksDayAdapter.DayHolder>() {

    private var listOfDays = mutableListOf<Day>()

    inner class DayHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayMaterialTextView = itemView.findViewById(R.id.day) as MaterialTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.calendar_item, parent, false)
        return DayHolder(view)
    }

    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        if (listOfDays.isNotEmpty()) {
            val date = listOfDays[position]

            holder.dayMaterialTextView.text = date.day

        }
    }

    override fun getItemCount(): Int {
        val size = listOfDays.size
        return size
    }

    fun getDay(position: Int) : Day{
        return listOfDays[position]
    }
    fun addDays(days: List<Day>) {
        val diff = Diff(listOfDays, days)
        val calculate = DiffUtil.calculateDiff(diff)

        listOfDays.clear()
        listOfDays.addAll(days)
        calculate.dispatchUpdatesTo(this)
    }

    class Diff(val oldList: List<Day>, val newList: List<Day>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].day.equals(newList[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].equals(newList[newItemPosition])
        }
    }
}