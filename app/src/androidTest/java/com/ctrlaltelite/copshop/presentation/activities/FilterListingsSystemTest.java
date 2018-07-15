package com.ctrlaltelite.copshop.presentation.activities;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.ctrlaltelite.copshop.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class FilterListingsSystemTest {

    @Rule
    public ActivityTestRule<ListingListActivity> mActivityTestRule = new ActivityTestRule<>(ListingListActivity.class);

    @Test
    public void editListingSystemTest() {
        SystemTestUtils.loginAsSeller("local@police.com", "12345");

        SystemTestUtils.deleteExtraListings();

        CreateListingSystemTest.createListing();

        // We are on listing list, go to the filter listings page

        ViewInteraction drawerButton = onView(withContentDescription("Open navigation drawer"));
        drawerButton.perform(click());

        // Click on the third drawer item (Filter Listings)
        ViewInteraction createListingButton = onView(
                allOf(SystemTestUtils.childAtPosition(allOf(withId(R.id.design_navigation_view),
                        SystemTestUtils.childAtPosition(withId(R.id.nav_view), 0)), 3),
                        isDisplayed()));
        createListingButton.perform(click());

        // Choose a category

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.category_spinner),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(SystemTestUtils.childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView.perform(click());

        onView(withId(R.id.btnSearch)).perform(click());

        // Back on listings list

        onView(withText("Bag of Broken Glass")).check(matches(isDisplayed()));
        onView(withText("Bicycle 1")).check(doesNotExist());
        //onView(withText("Bicycle 1")).check(matches(not(isDisplayed())));
        onView(withText("Toothbrush 4")).check(doesNotExist());

        // Now filter by Listing Title

        // We are on listing list, go to the filter listings page

        drawerButton = onView(withContentDescription("Open navigation drawer"));
        drawerButton.perform(click());

        // Click on the third drawer item (Filter Listings)
        createListingButton = onView(
                allOf(SystemTestUtils.childAtPosition(allOf(withId(R.id.design_navigation_view),
                        SystemTestUtils.childAtPosition(withId(R.id.nav_view), 0)), 3),
                        isDisplayed()));
        createListingButton.perform(click());

        // Write a phrase to sort by
        onView(withId(R.id.txtFilterName)).perform(replaceText("Broken Glass"), closeSoftKeyboard());
        onView(withId(R.id.btnSearch)).perform(click());

        // Back on listings list

        onView(withText("Bag of Broken Glass")).check(matches(isDisplayed()));
        onView(withText("Bicycle 1")).check(doesNotExist());
        //onView(withText("Bicycle 1")).check(matches(not(isDisplayed())));
        onView(withText("Toothbrush 4")).check(doesNotExist());

        CreateListingSystemTest.deleteListing();
    }

}
