package com.stan.sumai.utils

import com.stan.sumai.data.NoteEntity

object NoteUtils {


    fun buildPrompt(note: NoteEntity): String =
        """
        Eres un asistente para estudiantes. Resume el siguiente apunte de forma clara y concisa.
        Usa máximo 3 puntos clave con viñetas (•).
        
        Título: ${note.title}
        
        Contenido:
        ${note.content}
        """.trimIndent()
    fun validateNote(title: String, content: String): String? {
        if (title.isBlank()) return "El título no puede estar vacío"
        if (content.isBlank()) return "El contenido no puede estar vacío"
        return null
    }

    fun isContentSummarizable(content: String, minWords: Int = 5): Boolean {
        return content.trim().split("\\s+".toRegex()).size >= minWords
    }
}