package com.stan.sumai

import com.google.common.truth.Truth.assertThat
import com.stan.sumai.data.NoteEntity
import com.stan.sumai.utils.NoteUtils
import org.junit.Test

class NotasTest {

    @Test
    fun noteEntity_defaultId_esZero() {
        // Arrange & Act
        val nota = NoteEntity(title = "Biología", content = "Las células son la unidad básica de la vida")
        // Assert
        assertThat(nota.id).isEqualTo(0L)
    }

    @Test
    fun noteEntity_defaultCreatedAt_esMayorQueZero() {
        // Arrange & Act
        val nota = NoteEntity(title = "Física", content = "Newton descubrió la gravedad")
        // Assert
        assertThat(nota.createdAt).isGreaterThan(0L)
    }

    @Test
    fun noteEntity_dosNotasConMismosCampos_sonIguales() {
        // Arrange
        val nota1 = NoteEntity(id = 1, title = "Math", content = "2+2=4", createdAt = 1000L)
        val nota2 = NoteEntity(id = 1, title = "Math", content = "2+2=4", createdAt = 1000L)
        // Assert
        assertThat(nota1).isEqualTo(nota2)
    }

    @Test
    fun noteEntity_dosNotasConDiferenteId_noSonIguales() {
        // Arrange
        val nota1 = NoteEntity(id = 1, title = "Math", content = "2+2=4", createdAt = 1000L)
        val nota2 = NoteEntity(id = 2, title = "Math", content = "2+2=4", createdAt = 1000L)
        // Assert
        assertThat(nota1).isNotEqualTo(nota2)
    }

    @Test
    fun noteEntity_titulo_seGuardaCorrectamente() {
        // Arrange
        val titulo = "Historia de México"
        // Act
        val nota = NoteEntity(title = titulo, content = "La independencia fue en 1810")
        // Assert
        assertThat(nota.title).isEqualTo(titulo)
    }

    @Test
    fun noteEntity_contenido_seGuardaCorrectamente() {
        // Arrange
        val contenido = "La mitocondria es la central energética de la célula"
        // Act
        val nota = NoteEntity(title = "Bio", content = contenido)
        // Assert
        assertThat(nota.content).isEqualTo(contenido)
    }

    @Test
    fun validateNote_tituloYContenidoValidos_retornaNull() {
        // Arrange
        val titulo = "Química Orgánica"
        val contenido = "Los alcanos son hidrocarburos saturados"
        // Act
        val resultado = NoteUtils.validateNote(titulo, contenido)
        // Assert
        assertThat(resultado).isNull()
    }

    @Test
    fun validateNote_tituloVacio_retornaMensajeDeError() {
        // Arrange
        val titulo = ""
        val contenido = "Contenido válido aquí"
        // Act
        val resultado = NoteUtils.validateNote(titulo, contenido)
        // Assert
        assertThat(resultado).isEqualTo("El título no puede estar vacío")
    }

    @Test
    fun validateNote_contenidoVacio_retornaMensajeDeError() {
        // Arrange
        val titulo = "Título válido"
        val contenido = ""
        // Act
        val resultado = NoteUtils.validateNote(titulo, contenido)
        // Assert
        assertThat(resultado).isEqualTo("El contenido no puede estar vacío")
    }

    @Test
    fun buildPrompt_contieneElTituloDeLaNota() {
        // Arrange
        val nota = NoteEntity(title = "Termodinámica", content = "El calor fluye del cuerpo caliente al frío")
        // Act
        val prompt = NoteUtils.buildPrompt(nota)
        // Assert
        assertThat(prompt).contains("Termodinámica")
    }

    @Test
    fun buildPrompt_contieneElContenidoDeLaNota() {
        // Arrange
        val nota = NoteEntity(title = "Física", content = "E = mc²")
        // Act
        val prompt = NoteUtils.buildPrompt(nota)
        // Assert
        assertThat(prompt).contains("E = mc²")
    }

    @Test
    fun isContentSummarizable_contenidoConSuficientesPalabras_retornaTrue() {
        // Arrange
        val contenido = "La fotosíntesis convierte luz solar en glucosa usando clorofila"
        // Act
        val resultado = NoteUtils.isContentSummarizable(contenido)
        // Assert
        assertThat(resultado).isTrue()
    }

    @Test
    fun isContentSummarizable_contenidoMuyCorto_retornaFalse() {
        // Arrange
        val contenido = "hola"
        // Act
        val resultado = NoteUtils.isContentSummarizable(contenido)
        // Assert
        assertThat(resultado).isFalse()
    }

    @Test
    fun noteEntity_tituloConEspacios_lanzaExcepcionAlValidar() {
        // Arrange
        val titulo = "   "
        val contenido = "Contenido válido para la prueba"
        try {
            // Act
            val error = NoteUtils.validateNote(titulo, contenido)
            if (error != null) throw IllegalArgumentException(error)
            assertThat(false).isTrue()
        } catch (e: IllegalArgumentException) {
            // Assert
            assertThat(e).isInstanceOf(IllegalArgumentException::class.java)
        }
    }
}