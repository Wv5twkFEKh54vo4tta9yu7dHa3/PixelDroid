package com.h.pixeldroid

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasDataString
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {
    @get:Rule
    var globalTimeout: Timeout = Timeout.seconds(100)
    @get:Rule
    var activityRule: ActivityScenarioRule<LoginActivity>
            = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun clickConnect() {
        onView(withId(R.id.connect_instance_button)).check(matches(withText("Connect to Pixelfed")))
    }

    @Test
    fun invalidURL() {
        onView(withId(R.id.editText)).perform(ViewActions.replaceText("/jdi"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.connect_instance_button)).perform(scrollTo()).perform(click())
        onView(withId(R.id.editText)).check(matches(hasErrorText("Invalid domain")))
    }

    @Test
    fun notPixelfedInstance() {
        onView(withId(R.id.editText)).perform(ViewActions.replaceText("localhost"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.connect_instance_button)).perform(scrollTo()).perform(click())
        onView(withId(R.id.editText)).check(matches(hasErrorText("Could not register the application with this server")))
    }
}

@RunWith(AndroidJUnit4::class)
class LoginCheckIntent {
    @get:Rule
    var globalTimeout: Timeout = Timeout.seconds(100)
    @get:Rule
    val intentsTestRule = ActivityTestRule(LoginActivity::class.java)

    @Before
    fun before() {
        Intents.init()
    }

    @Test
    fun launchesOAuthIntent() {
        ActivityScenario.launch(LoginActivity::class.java)
        val expectedIntent: Matcher<Intent> = allOf(
            hasAction(ACTION_VIEW),
            hasDataString(containsString("pixelfed.social"))
        )
        Thread.sleep(1000)

        onView(withId(R.id.editText)).perform(scrollTo()).perform(ViewActions.replaceText("pixelfed.social"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.connect_instance_button)).perform(scrollTo()).perform(click())

        Thread.sleep(3000)

        intended(expectedIntent)

    }
    @Test
    fun launchesInstanceInfo() {
        ActivityScenario.launch(LoginActivity::class.java)
        val expectedIntent: Matcher<Intent> = allOf(
            hasAction(ACTION_VIEW),
            hasDataString(containsString("pixelfed.org/join"))
        )

        onView(withId(R.id.whatsAnInstanceTextView)).perform(scrollTo()).perform(click())

        Thread.sleep(3000)

        intended(expectedIntent)

    }

    @After
    fun after() {
        Intents.release()
    }
}

@RunWith(AndroidJUnit4::class)
class AfterIntent {
    @get:Rule
    var globalTimeout: Timeout = Timeout.seconds(100)

    @get:Rule
    val rule = ActivityTestRule(LoginActivity::class.java)
    private var launchedActivity: Activity? = null

    @Before
    fun setup() {
        val preferences = InstrumentationRegistry.getInstrumentation()
            .targetContext.getSharedPreferences("com.h.pixeldroid.pref", Context.MODE_PRIVATE)
        preferences.edit().putString("domain", "http://localhost").apply()
        val intent = Intent(ACTION_VIEW, Uri.parse("oauth2redirect://com.h.pixeldroid?code=sdfdqsf"))
        launchedActivity = rule.launchActivity(intent)
    }

    @Test
    fun usesIntent() {

        Thread.sleep(5000)

        onView(withId(R.id.editText)).check(matches(
            anyOf(hasErrorText("Error getting token"),
            hasErrorText("Could not authenticate"))))

    }
}