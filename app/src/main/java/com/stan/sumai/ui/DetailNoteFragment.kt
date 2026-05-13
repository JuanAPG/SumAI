package com.stan.sumai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.stan.sumai.R
import com.stan.sumai.databinding.FragmentDetailNoteBinding
import com.stan.sumai.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailNoteFragment : Fragment() {

    private var _binding: FragmentDetailNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteId = arguments?.getLong("noteId") ?: run {
            findNavController().popBackStack()
            return
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // Carga el apunte por ID
        viewModel.getNoteById(noteId).observe(viewLifecycleOwner) { note ->
            note ?: return@observe
            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            binding.tvTitle.text   = note.title
            binding.tvContent.text = note.content
            binding.tvDate.text    = sdf.format(Date(note.createdAt))

            binding.btnSummarize.setOnClickListener {
                viewModel.generateSummary(note.content)
            }
        }

        observeSummaryState()
    }

    /**
     * Tres observers paralelos — espejo exacto del patrón DogApp:
     *
     *   DogApp                        SumAI
     *   ─────────────────────────     ─────────────────────────
     *   progressBar  VISIBLE/GONE  →  progressSummary VISIBLE/GONE
     *   tvError      VISIBLE/GONE  →  tvError         VISIBLE/GONE
     *   rvDogs       VISIBLE/GONE  →  tvSummary       VISIBLE/GONE
     */
    private fun observeSummaryState() {

        // 1. Carga — muestra spinner y oculta resultados/error
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressSummary.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnSummarize.isEnabled     = !loading
            if (loading) {
                binding.tvSummary.visibility = View.GONE
                binding.tvError.visibility   = View.GONE
            }
        }

        // 2. Resumen generado — muestra texto, oculta error
        viewModel.summary.observe(viewLifecycleOwner) { text ->
            if (!text.isNullOrEmpty()) {
                binding.tvSummary.text       = text
                binding.tvSummary.visibility = View.VISIBLE
                binding.tvError.visibility   = View.GONE
            }
        }

        // 3. Error — muestra tvError, oculta resumen (igual que DogApp showError())
        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrEmpty()) {
                binding.tvError.text       = msg
                binding.tvError.visibility = View.VISIBLE
                binding.tvSummary.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
