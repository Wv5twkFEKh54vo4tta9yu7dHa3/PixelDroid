package com.h.pixeldroid

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.android.material.tabs.TabLayout
import com.h.pixeldroid.fragments.feeds.PostViewHolder
import com.h.pixeldroid.objects.Account
import com.h.pixeldroid.objects.Account.Companion.ACCOUNT_TAG
import com.h.pixeldroid.testUtility.MockServer
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class IntentTest {

    private val mockServer = MockServer()

    @get:Rule
    var globalTimeout: Timeout = Timeout.seconds(100)

    @get:Rule
    var mLoginActivityActivityTestRule =
        ActivityTestRule(
            LoginActivity::class.java
        )

    @Before
    fun before() {
        mockServer.start()
        val baseUrl = mockServer.getUrl()
        val preferences = InstrumentationRegistry.getInstrumentation()
            .targetContext.getSharedPreferences("com.h.pixeldroid.pref", Context.MODE_PRIVATE)
        preferences.edit().putString("accessToken", "azerty").apply()
        preferences.edit().putString("domain", baseUrl.toString()).apply()
        Intents.init()
    }

    @Test
    fun clickingMentionOpensProfile() {
        ActivityScenario.launch(MainActivity::class.java)

        val account = Account("1450", "deerbard_photo", "deerbard_photo",
            "https://pixelfed.social/deerbard_photo", "deerbard photography",
            "",
            "https://pixelfed.social/storage/avatars/000/000/001/450/SMSep5NoabDam1W8UDMh_avatar.png?v=4b227777d4dd1fc61c6f884f48641d02b4d121d3fd328cb08b5531fcacdabf8a",
            "https://pixelfed.social/storage/avatars/000/000/001/450/SMSep5NoabDam1W8UDMh_avatar.png?v=4b227777d4dd1fc61c6f884f48641d02b4d121d3fd328cb08b5531fcacdabf8a",
            "", "", false, emptyList(), false,
            "2018-08-01T12:58:21.000000Z", 72, 68, 27,
            null, null, false, null)
        val expectedIntent: Matcher<Intent> = CoreMatchers.allOf(
            IntentMatchers.hasExtra(ACCOUNT_TAG, account)
        )

        Thread.sleep(1000)

        //Click the mention
        Espresso.onView(ViewMatchers.withId(R.id.list))
            .perform(RecyclerViewActions.actionOnItemAtPosition<PostViewHolder>
                (0, clickClickableSpanInDescription("@Dobios")))

        //Wait a bit
        Thread.sleep(1000)

        //Check that the Profile is shown
        intended(expectedIntent)
    }

    fun clickClickableSpanInDescription(textToClick: CharSequence): ViewAction {
        return object : ViewAction {

            override fun getConstraints(): Matcher<View> {
                return Matchers.instanceOf(TextView::class.java)
            }

            override fun getDescription(): String {
                return "clicking on a ClickableSpan";
            }

            override fun perform(uiController: UiController, view: View) {
                val textView = view.findViewById<View>(R.id.description) as TextView
                val spannableString = textView.text as SpannableString

                if (spannableString.isEmpty()) {
                    // TextView is empty, nothing to do
                    throw NoMatchingViewException.Builder()
                        .includeViewHierarchy(true)
                        .withRootView(textView)
                        .build();
                }

                // Get the links inside the TextView and check if we find textToClick
                val spans = spannableString.getSpans(0, spannableString.length, ClickableSpan::class.java)
                if (spans.isNotEmpty()) {
                    var spanCandidate: ClickableSpan
                    for (span: ClickableSpan in spans) {
                        spanCandidate = span
                        val start = spannableString.getSpanStart(spanCandidate)
                        val end = spannableString.getSpanEnd(spanCandidate)
                        val sequence = spannableString.subSequence(start, end)
                        if (textToClick.toString().equals(sequence.toString())) {
                            span.onClick(textView)
                            return;
                        }
                    }
                }

                // textToClick not found in TextView
                throw NoMatchingViewException.Builder()
                    .includeViewHierarchy(true)
                    .withRootView(textView)
                    .build()

            }
        }
    }

    @Test
    fun launchesIntent() {
        // Open Drawer to click on navigation.
        ActivityScenario.launch(MainActivity::class.java)
        Espresso.onView(ViewMatchers.withId(R.id.drawer_layout))
            .check(ViewAssertions.matches(DrawerMatchers.isClosed(Gravity.LEFT))) // Left Drawer should be closed.
            .perform(DrawerActions.open()) // Open Drawer
        Espresso.onView(ViewMatchers.withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_account))

        val expectedIntent: Matcher<Intent> = CoreMatchers.allOf(
            IntentMatchers.hasAction(Intent.ACTION_VIEW),
            IntentMatchers.hasDataString(CoreMatchers.containsString("settings/home"))
        )

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withId(R.id.editButton)).perform(ViewActions.click())
        Thread.sleep(1000)

        intended(expectedIntent)
    }

    @After
    fun after() {
        Intents.release()
    }
}