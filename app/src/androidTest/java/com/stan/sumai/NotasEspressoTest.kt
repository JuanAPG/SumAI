package com.stan.sumai

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stan.sumai.ui.MainActivity
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotasInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun listaNotas_estadoVacio_esMostrado() {
        // La app inicia sin notas → el empty state debe ser visible
        onView(withId(R.id.empty_state))
            .check(matches(isDisplayed()))
    }

    @Test
    fun fabNuevoApunte_alHacerClick_abreFormularioDeCreacion() {
        onView(withId(R.id.fab_new_note)).perform(click())
        // El botón Guardar debe estar visible en la nueva pantalla
        onView(withId(R.id.btn_save))
            .check(matches(isDisplayed()))
    }

    @Test
    fun guardarApunte_sinTitulo_muestraErrorEnTitulo() {
        // Arrange: navegar a crear nota
        onView(withId(R.id.fab_new_note)).perform(click())

        // Act: escribir sólo contenido y guardar
        onView(withId(R.id.et_content)).perform(typeText("Contenido sin título"))
        closeSoftKeyboard()
        onView(withId(R.id.btn_save)).perform(click())

        // Assert: el TextInputLayout de título muestra el error
        onView(withId(R.id.til_title))
            .check(matches(isDisplayed()))
    }

    @Test
    fun guardarApunte_sinContenido_muestraErrorEnContenido() {
        // Arrange: navegar a crear nota
        onView(withId(R.id.fab_new_note)).perform(click())

        // Act: escribir sólo título y guardar
        onView(withId(R.id.et_title)).perform(typeText("Título sin contenido"))
        closeSoftKeyboard()
        onView(withId(R.id.btn_save)).perform(click())

        // Assert: el TextInputLayout de contenido sigue visible (con error)
        onView(withId(R.id.til_content))
            .check(matches(isDisplayed()))
    }

    @Test
    fun crearApunte_conDatosValidos_apareceEnLaLista() {
        // Arrange: navegar a crear nota
        onView(withId(R.id.fab_new_note)).perform(click())

        // Act: llenar campos y guardar
        onView(withId(R.id.et_title)).perform(typeText("Biología celular"))
        onView(withId(R.id.et_content))
            .perform(typeText("La célula es la unidad básica de la vida"))
        closeSoftKeyboard()
        onView(withId(R.id.btn_save)).perform(click())

        // Assert: el título aparece en el RecyclerView de la lista
        onView(withText("Biología celular"))
            .check(matches(isDisplayed()))
    }
}