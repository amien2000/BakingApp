package com.example.android.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    private IdlingResource mIdlingResource;
    private static final String recipeNameAtPosition = "Cheesecake";
    private static final int positionToClick = 3;
    private static final int numberOfIngredients = 9;
    private static final int numberOfSteps = 13;

    @Rule
    final public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void numberOfIngredients() {
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(positionToClick, click()));
        matchToolbarTitle(recipeNameAtPosition)
                .check(matches(isDisplayed()));
        onView(
                allOf(childAtPosition(allOf(withId(android.R.id.tabs),
                        childAtPosition(withClassName(is("android.widget.LinearLayout")),
                        0)),
                        0),
                        isDisplayed())).perform(click());
        onView(withId(R.id.rv_ingredients)).check(new RecyclerViewItemCountAssertion(
                numberOfIngredients));
    }

    @Test
    public void numberOfSteps() {
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(positionToClick, click()));
        onView(
                allOf(childAtPosition(allOf(withId(android.R.id.tabs),
                        childAtPosition(withClassName(is("android.widget.LinearLayout")),
                        0)),
                        1),
                        isDisplayed())).perform(click());
        onView(withId(R.id.rv_steps)).check(new RecyclerViewItemCountAssertion(numberOfSteps));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

    private static ViewInteraction matchToolbarTitle(
            CharSequence title) {
        return onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(is(title))));
    }

    public static Matcher withToolbarTitle (final Matcher<CharSequence> textMatcher){
        return new TypeSafeMatcher<Toolbar>(){
            @Override
            public boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }
            @Override
            public void describeTo(Description description) {
            }
        };
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
