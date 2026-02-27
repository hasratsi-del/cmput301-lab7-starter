package com.example.androiduitesting;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShowActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        // Increase timeout for idling
        IdlingPolicies.setMasterPolicyTimeout(10, TimeUnit.SECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10, TimeUnit.SECONDS);

        // Initialize Intents for testing activity launches
        Intents.init();
    }

    @After
    public void tearDown() {
        // Clean up Intents after tests
        Intents.release();
    }

    /**
     * Test 1: Check if activity correctly switches to ShowActivity
     * when a city is clicked
     */
    @Test
    public void testActivitySwitchesCorrectly() {
        // First, add a city to the list
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_name)).perform(ViewActions.typeText("Edmonton"));
        onView(withId(R.id.button_confirm)).perform(click());

        // Click on the city we just added
        onView(withText("Edmonton")).perform(click());

        // Verify that ShowActivity was launched
        intended(hasComponent(ShowActivity.class.getName()));
    }

    /**
     * Test 2: Test if the city name passed to ShowActivity is consistent
     * with what was clicked
     */
    @Test
    public void testCityNameConsistency() {
        // Add a city
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_name)).perform(ViewActions.typeText("Edmonton"));
        onView(withId(R.id.button_confirm)).perform(click());

        // Click on the city
        onView(withText("Edmonton")).perform(click());

        // Verify the intent has the correct city name extra
        intended(allOf(
                hasComponent(ShowActivity.class.getName()),
                hasExtra(equalTo("CITY_NAME"), equalTo("Edmonton"))
        ));

        // Wait for the new activity to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check that the city name is displayed in ShowActivity
        onView(withId(R.id.city_text)).check(matches(withText("Edmonton")));
    }

    /**
     * Test 3: Test the back button functionality
     */
    @Test
    public void testBackButton() {
        // Add a city
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_name)).perform(ViewActions.typeText("Edmonton"));
        onView(withId(R.id.button_confirm)).perform(click());

        // Navigate to ShowActivity
        onView(withText("Edmonton")).perform(click());

        // Wait for activity to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click the back button in ShowActivity
        onView(withId(R.id.back_button)).perform(click());

        // Wait for navigation back
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify we're back in MainActivity by checking for a MainActivity element
        onView(withId(R.id.button_add)).check(matches(isDisplayed()));
    }
}