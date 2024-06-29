package com.app.newsviewr.espresso

import com.app.newsviewr.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.app.newsviewr.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class NewsFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_isListFragmentVisible() {
        onView(withId(R.id.rv_news)).check(matches(isDisplayed()))
    }

    @Test
    fun test_optionsMenuAction() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)

        onView(withText(R.string.recent)).check(matches(isDisplayed()))
        onView(withText(R.string.popular)).check(matches(isDisplayed()))

        onView(withText(R.string.popular)).perform(click())

        onView(withId(R.id.rv_news)).check(matches(isDisplayed()))
    }
}