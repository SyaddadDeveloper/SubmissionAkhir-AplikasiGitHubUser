package com.example.submissionawalaplikasigithubuser.ui.main


import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import com.example.submissionawalaplikasigithubuser.R

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Before
    fun setUp() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    //ui testing untuk mengubah tema
    fun testOpenSettingAndToggleSwitch() {
        //mengubah menjadi darkmode
        Espresso.onView(withId(R.id.menu2)).perform(click())
        Espresso.onView(withId(R.id.switch_theme)).perform(click())
        Espresso.pressBack()
        //mengubah menjadi semula
        ActivityScenario.launch(MainActivity::class.java)
        Espresso.onView(withId(R.id.menu2)).perform(click())
        Espresso.onView(withId(R.id.switch_theme)).perform(click())
        Espresso.pressBack()
    }
    @Test
    //ui test untuk mencari user
    fun testSearching() {
        Espresso.onView(withId(R.id.searchBar)).perform(click())
        Espresso.onView(isAssignableFrom(android.widget.EditText::class.java))
            .perform(typeText("Arif un"), pressImeActionButton())
        Espresso.onView(withId(R.id.rv_users)) // replace with your RecyclerView ID
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
    }
}