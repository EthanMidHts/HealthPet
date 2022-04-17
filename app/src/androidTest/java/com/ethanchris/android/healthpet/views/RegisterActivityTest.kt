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
class RegisterActivityTest : TestCase(){

    private lateinit var scenario: ActivityScenario<RegisterActivity>

    @get:Rule
    val activityRule = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun testRegister() {
        FirebaseAuth.getInstance().signOut()
        val email = StringBuilder()
        val randomNums = List(15) { Random.nextInt(65, 91) }
        for (i in 0..14) {
            email.append(randomNums[i].toChar())
        }
        email.append("-testcase@gmail.com")

        onView(withId(R.id.emailEditText)).perform(ViewActions.typeText(email.toString()));
        onView(withId(R.id.passwordEditText)).perform(ViewActions.typeText("testcase"))
        onView(withId(R.id.purpleRadioButton)).perform(ViewActions.click())
        closeSoftKeyboard()
        onView(withId(R.id.petNameEditText)).perform(ViewActions.typeText("testcase pet name"))
        closeSoftKeyboard()
        onView(withId(R.id.registerButton)).perform(ViewActions.click())
    }

    @After
    fun cleanup() {
        FirebaseAuth.getInstance().currentUser?.delete()
    }
}