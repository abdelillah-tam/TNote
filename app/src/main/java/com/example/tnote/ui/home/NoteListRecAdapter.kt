package com.example.tnote.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.tnote.R
import com.example.tnote.domain.models.Note
import com.google.android.material.textview.MaterialTextView
import javax.inject.Inject

class NoteListRecAdapter
@Inject constructor() : RecyclerView.Adapter<NoteListRecAdapter.NoteViewHolder>() {

    private val notesList = mutableListOf<Note>()

    inner class NoteViewHolder(itemView: View) : ViewHolder(itemView) {
        val noteTitle = itemView.findViewById(R.id.note_title) as MaterialTextView
        val noteText = itemView.findViewById(R.id.note_text) as MaterialTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        if (notesList.isNotEmpty()) {
            val note = notesList[position]

            holder.noteTitle.text = note.noteTitle
            holder.noteText.text = note.noteText
        }
    }

    override fun getItemCount(): Int {
        return if (notesList.isEmpty()) {
            0
        } else 7
    }

    fun setNotes(notes: List<Note>) {
        val diff = Diff(notesList, notes)
        val diffResult = DiffUtil.calculateDiff(diff)

        notesList.clear()
        notesList.addAll(notes)
        diffResult.dispatchUpdatesTo(this)
    }

    class Diff(
        val oldList: List<Note>,
        val newList: List<Note>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return oldList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].noteTitle.equals(newList[newItemPosition].noteText)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].equals(newList[newItemPosition])
        }

    }
}