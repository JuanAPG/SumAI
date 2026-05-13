package com.stan.sumai.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stan.sumai.data.NoteEntity
import com.stan.sumai.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * NoteAdapter extiende ListAdapter (no RecyclerView.Adapter como DogAdapter)
 * para aprovechar DiffUtil y animaciones de lista automáticas.
 *
 * Patrón ViewBinding: ItemNoteBinding.inflate() en onCreateViewHolder,
 * igual que DogViewHolder usa ItemDogBinding.bind().
 */
class NoteAdapter(
    private val onClick: (NoteEntity) -> Unit
) : ListAdapter<NoteEntity, NoteAdapter.NoteViewHolder>(DiffCallback) {

    // ── DiffUtil ──────────────────────────────────────────────────────────────
    companion object DiffCallback : DiffUtil.ItemCallback<NoteEntity>() {
        override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean =
            oldItem == newItem
    }

    // ── ViewHolder ────────────────────────────────────────────────────────────
    inner class NoteViewHolder(
        private val binding: ItemNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        fun bind(note: NoteEntity) {
            binding.tvTitle.text = note.title
            binding.tvContentPreview.text = note.content
            binding.tvDate.text = dateFormat.format(Date(note.createdAt))
            // Click listener propaga la nota al Fragment (navegación con ID)
            binding.root.setOnClickListener { onClick(note) }
        }
    }

    // ── Adapter overrides ─────────────────────────────────────────────────────
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
