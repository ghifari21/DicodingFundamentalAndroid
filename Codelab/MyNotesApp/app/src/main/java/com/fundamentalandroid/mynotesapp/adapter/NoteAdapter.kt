package com.fundamentalandroid.mynotesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fundamentalandroid.mynotesapp.R
import com.fundamentalandroid.mynotesapp.databinding.ItemNoteBinding
import com.fundamentalandroid.mynotesapp.entity.Note

class NoteAdapter(private val onItemClickCallback: OnItemClickCallback) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    var listNotes = ArrayList<Note>()
        set(listNotes) {
            if (listNotes.size > 0) {
                this.listNotes.clear()
            }
            this.listNotes.addAll(listNotes)
        }

    fun addItem(note: Note) {
        this.listNotes.add(note)
        notifyItemInserted(this.listNotes.size - 1)
    }

    fun updateItem(position: Int, note: Note) {
        this.listNotes[position] = note
        notifyItemChanged(position, note)
    }

    fun removeItem(position: Int) {
        this.listNotes.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listNotes.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }

    override fun getItemCount(): Int = this.listNotes.size

    interface OnItemClickCallback {
        fun onItemClicked(selectedNote: Note?, position: Int?)
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemNoteBinding.bind(itemView)
        fun bind(note: Note) {
            binding.apply {
                tvItemTitle.text = note.title
                tvItemDate.text = note.date
                tvItemDescription.text = note.description
                cvItemNote.setOnClickListener {
                    onItemClickCallback.onItemClicked(note, adapterPosition)
                }
            }
        }
    }
}