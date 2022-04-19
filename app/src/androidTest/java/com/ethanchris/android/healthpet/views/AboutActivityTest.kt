package com.ethanchris.android.healthpet.views

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.MotionEvents
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.ethanchris.android.healthpet.R
import com.ethanchris.android.healthpet.ui.login.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.StringBuilder
import kotlin.random.Random


@RunWith(AndroidJUnit4::class)
class AboutActivityTest : TestCase(){

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeScreenActivity::class.java)

    @Test
    fun testRegister() {
        onView(withId(R.id.aboutButton)).perform(ViewActions.click())
    }
}