package com.stan.sumai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.stan.sumai.data.AppDatabase
import com.stan.sumai.data.NoteEntity
import com.stan.sumai.data.NoteRepository
import com.stan.sumai.network.Content
import com.stan.sumai.network.GeminiRequest
import com.stan.sumai.network.Part
import com.stan.sumai.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository = NoteRepository(
        AppDatabase.getInstance(application).noteDao()
    )

    val allNotes: LiveData<List<NoteEntity>> = repository.allNotes

    private val _selectedNote = MutableLiveData<NoteEntity?>()
    val selectedNote: LiveData<NoteEntity?> get() = _selectedNote

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _summary = MutableLiveData<String>()
    val summary: LiveData<String> get() = _summary

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage
    
    fun insert(note: NoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(note)
        }
    }

    fun loadNoteById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val note = repository.getNoteById(id)
            withContext(Dispatchers.Main) {
                _selectedNote.value = note
            }
        }
    }
    
    fun generateSummary(note: NoteEntity) {
        viewModelScope.launch {

            _isLoading.value    = true
            _summary.value      = ""
            _errorMessage.value = ""

            try {
                val responseText = withContext(Dispatchers.IO) {
                    val prompt = buildPrompt(note)
                    val request = GeminiRequest(
                        contents = listOf(
                            Content(parts = listOf(Part(text = prompt)))
                        )
                    )
                    RetrofitClient.service.generateContent(request)
                }

                if (responseText.isSuccessful) {
                    val text = responseText.body()
                        ?.candidates?.firstOrNull()
                        ?.content?.parts?.firstOrNull()
                        ?.text

                    if (!text.isNullOrBlank()) {
                        _summary.value = text
                    } else {
                        _errorMessage.value = "Gemini no devolvió respuesta"
                    }
                } else {
                    val errorBody = responseText.errorBody()?.string() ?: "Sin detalles"
                    _errorMessage.value = when (responseText.code()) {
                        429 -> "Has alcanzado el límite de peticiones. Espera un minuto."
                        400 -> "Petición inválida"
                        403 -> "API key inválida o sin permisos"
                        else -> "Error ${responseText.code()}: $errorBody"
                    }
                }

            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"

            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun buildPrompt(note: NoteEntity): String =
        """
        Eres un asistente para estudiantes. Resume el siguiente apunte de forma clara y concisa.
        Usa máximo 3 puntos clave con viñetas (•).
        
        Título: ${note.title}
        
        Contenido:
        ${note.content}
        """.trimIndent()
}
