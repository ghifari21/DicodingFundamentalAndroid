package com.fundamentalandroid.githubuserapp.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.fundamentalandroid.githubuserapp.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun navToFavoriteActivity() {
        onView(withId(R.id.favorite_menu)).check(matches(isDisplayed()))
        onView(withId(R.id.favorite_menu)).perform(click())

        onView(withId(R.id.activity_favorite)).check(matches(isDisplayed()))
    }

    @Test
    fun navToSettingActivity() {
        onView(withId(R.id.setting_menu)).check(matches(isDisplayed()))
        onView(withId(R.id.setting_menu)).perform(click())

        onView(withId(R.id.activity_setting)).check(matches(isDisplayed()))
    }
}