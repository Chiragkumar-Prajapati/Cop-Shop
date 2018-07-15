package com.ctrlaltelite.copshop.presentation.activities;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.PickerActions;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;

import com.ctrlaltelite.copshop.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

public class SystemTestUtils {
    public static void setDate(int datePickerLaunchViewId, int year, int monthOfYear, int dayOfMonth) {
        onView(withId(datePickerLaunchViewId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
    }

    public static void logout() {
        // Open drawer
        ViewInteraction drawerButton = onView(withContentDescription("Open navigation drawer"));
        drawerButton.perform(click());

        // Click on the first drawer item
        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)), 1),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        // If logged in, log out
        try {
            ViewInteraction unused = onView(allOf(withId(R.id.email), isDisplayed()));
            unused.perform(click());
        }
        catch (NoMatchingViewException e) {
            // If 'login' was not found, we must be on the account creation page
            pressBack();

            // Open drawer
            drawerButton.perform(click());

            // Click on the second drawer item (Logout)
            ViewInteraction logoutButton = onView(
                    allOf(childAtPosition(
                            allOf(withId(R.id.design_navigation_view),
                                    childAtPosition(
                                            withId(R.id.nav_view),
                                            0)), 2),
                            isDisplayed()));
            logoutButton.perform(click());

            // Open drawer
            drawerButton.perform(click());

            // Click on the first drawer item (Login)
            ViewInteraction loginButton = onView(
                    allOf(childAtPosition(allOf(withId(R.id.design_navigation_view),
                            childAtPosition(withId(R.id.nav_view), 0)), 1),
                            isDisplayed()));
            loginButton.perform(click());
        }
    }

    public static void loginAsBuyer() {
        logout();

        // On login page

        // Log in as buyer
        onView(allOf(withId(R.id.email), isDisplayed())).perform(replaceText("john@email.com"), closeSoftKeyboard());
        onView(allOf(withId(R.id.password), isDisplayed())).perform(replaceText("john"), closeSoftKeyboard());
        onView(allOf(withId(R.id.btnLogin), isDisplayed())).perform(click());
    }

    public static void loginAsSeller() {
        logout();

        // On login page

        // Log in as seller
        ViewInteraction email = onView(allOf(withId(R.id.email), isDisplayed()));
        email.perform(replaceText("local@police.com"), closeSoftKeyboard());

        ViewInteraction password = onView(allOf(withId(R.id.password), isDisplayed()));
        password.perform(replaceText("12345"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btnLogin), isDisplayed()));
        appCompatButton.perform(click());
    }

    public static Matcher<View> childAtPosition(
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
