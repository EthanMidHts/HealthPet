package com.ethanchris.android.healthpet.views

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
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
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.*
import com.ethanchris.android.healthpet.R
import com.ethanchris.android.healthpet.ui.login.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import junit.framework.TestCase
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.StringBuilder
import kotlin.random.Random


@RunWith(AndroidJUnit4::class)
class LoginLandscapeActivityTest : TestCase(){

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeScreenActivity::class.java)

    @Before
    fun init() {
        activityRule.scenario.onActivity { activity ->
            run {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }
    }

    @Test
    fun testRegister() {
        FirebaseAuth.getInstance().signOut()
        onView(withId(R.id.loginButton)).perform(ViewActions.click())
        onView(withId(R.id.username)).perform(ViewActions.typeText("testcase@gmail.com"));
        closeSoftKeyboard()
        onView(withId(R.id.password)).perform(ViewActions.typeText("testcase"))
        closeSoftKeyboard()
        onView(withId(R.id.loginButton)).perform(ViewActions.click())
    }
}