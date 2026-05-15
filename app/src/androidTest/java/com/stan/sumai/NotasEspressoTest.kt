package com.stan.sumai

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stan.sumai.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotasInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun listaNotas_estadoVacio_esMostrado() {
        onView(withId(R.id.empty_state))
            .check(matches(isDisplayed()))
    }

    @Test
    fun fabNuevoApunte_alHacerClick_abreFormularioDeCreacion() {
        onView(withId(R.id.fab_new_note)).perform(click())
        onView(withId(R.id.btn_save))
            .check(matches(isDisplayed()))
    }

    @Test
    fun guardarApunte_sinTitulo_muestraErrorEnTitulo() {
        onView(withId(R.id.fab_new_note)).perform(click())

        onView(withId(R.id.et_content)).perform(replaceText("Contenido sin título"))
        closeSoftKeyboard()
        onView(withId(R.id.btn_save)).perform(click())

        onView(withId(R.id.til_title))
            .check(matches(isDisplayed()))
    }

    @Test
    fun guardarApunte_sinContenido_muestraErrorEnContenido() {
        onView(withId(R.id.fab_new_note)).perform(click())

        onView(withId(R.id.et_title)).perform(replaceText("Título sin contenido"))
        closeSoftKeyboard()
        onView(withId(R.id.btn_save)).perform(click())
        onView(withId(R.id.til_content))
            .check(matches(isDisplayed()))
    }

    @Test
    fun crearApunte_conDatosValidos_apareceEnLaLista() {
        onView(withId(R.id.fab_new_note)).perform(click())

        onView(withId(R.id.et_title)).perform(replaceText("Biología celular"))
        onView(withId(R.id.et_content))
            .perform(replaceText("La célula es la unidad básica de la vida"))
        closeSoftKeyboard()
        onView(withId(R.id.btn_save)).perform(click())

        onView(withText("Biología celular"))
            .check(matches(isDisplayed()))
    }
}