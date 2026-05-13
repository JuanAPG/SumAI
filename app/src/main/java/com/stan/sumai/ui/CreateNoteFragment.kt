package com.stan.sumai.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.stan.sumai.data.NoteEntity
import com.stan.sumai.databinding.FragmentCreateNoteBinding
import com.stan.sumai.viewmodel.NoteViewModel

class CreateNoteFragment : Fragment() {

    private var _binding: FragmentCreateNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón de navegación del Toolbar → regresar sin guardar
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSave.setOnClickListener { saveNote() }
    }

    private fun saveNote() {
        val title   = binding.etTitle.text.toString().trim()
        val content = binding.etContent.text.toString().trim()

        // Limpiar errores previos
        binding.tilTitle.error   = null
        binding.tilContent.error = null

        // Validación inline con TextInputLayout (Material 3)
        if (title.isEmpty()) {
            binding.tilTitle.error = "El título no puede estar vacío"
            return
        }
        if (content.isEmpty()) {
            binding.tilContent.error = "El contenido no puede estar vacío"
            return
        }

        viewModel.insert(NoteEntity(title = title, content = content))
        findNavController().popBackStack()   // regresa a ListNotesFragment
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
