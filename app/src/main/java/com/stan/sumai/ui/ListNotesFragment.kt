package com.stan.sumai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stan.sumai.R
import com.stan.sumai.databinding.FragmentListNotesBinding
import com.stan.sumai.viewmodel.NoteViewModel

class ListNotesFragment : Fragment() {

    private var _binding: FragmentListNotesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoteViewModel by viewModels()
    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeNotes()

        binding.fabNewNote.setOnClickListener {
            findNavController().navigate(R.id.action_list_to_create)
        }
    }

    private fun setupRecyclerView() {
        adapter = NoteAdapter { note ->
            val bundle = Bundle().apply { putLong("noteId", note.id) }
            findNavController().navigate(R.id.action_list_to_detail, bundle)
        }
        binding.recyclerNotes.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ListNotesFragment.adapter
        }
    }

    private fun observeNotes() {
        viewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            adapter.submitList(notes)

            val isEmpty = notes.isNullOrEmpty()
            binding.emptyState.visibility    = if (isEmpty) View.VISIBLE else View.GONE
            binding.recyclerNotes.visibility = if (isEmpty) View.GONE   else View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
