package com.ctrlaltelite.copshop.presentation.activities;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

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
import static android.support.test.espresso.action.ViewActions.scrollTo;
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


@LargeTest
@RunWith(AndroidJUnit4.class)
public class CreateListingSystemTest {

    @Rule
    public ActivityTestRule<ListingListActivity> mActivityTestRule = new ActivityTestRule<>(ListingListActivity.class);

    @Test
    public void createListingSystemTest() {
        SystemTestUtils.loginAsSeller("local@police.com", "12345");

        SystemTestUtils.deleteExtraListings();

        createListing();

        //ViewInteraction listingRowTitle = onView(withText("Bag of Broken Glass"));
        ViewInteraction listingRowPrice = onView(
                allOf(withId(R.id.listing_list_price),
                        withParent(allOf(withParent(withId(R.id.listing_list)), withChild(withText("Bag of Broken Glass"))))));
        listingRowPrice.check(matches(withText("$100.00,")));
        //listingRowTitle.check(matches(withText("Bag of Broken Glass")));

        deleteListing();
    }

    public static void createListing() {
        System.out.println("-------------------- Creating Listing");
        // We are on listing list page

        ViewInteraction drawerButton = onView(withContentDescription("Open navigation drawer"));
        drawerButton.perform(click());

        // Click on the fourth drawer item (Create Listing)
        ViewInteraction createListingButton = onView(
                allOf(SystemTestUtils.childAtPosition(allOf(withId(R.id.design_navigation_view),
                        SystemTestUtils.childAtPosition(withId(R.id.nav_view), 0)), 4),
                        isDisplayed()));
        createListingButton.perform(click());

        onView(allOf(withId(R.id.txtListingTitle), isDisplayed())).perform(replaceText("Bag of Broken Glass"), closeSoftKeyboard());
        onView(allOf(withId(R.id.txtInitialPrice), isDisplayed())).perform(replaceText("100.00"), closeSoftKeyboard());
        onView(allOf(withId(R.id.txtMinBid), isDisplayed())).perform(replaceText("10.00"), closeSoftKeyboard());
        onView(allOf(withId(R.id.txtCategory), isDisplayed())).perform(replaceText("Baggies"), closeSoftKeyboard());
        SystemTestUtils.setDate(R.id.btnStartDate, 2020, 1, 1);
        SystemTestUtils.setDate(R.id.btnEndDate, 2022, 1, 1);
        onView(allOf(withId(R.id.txtAreaDescription), isDisplayed())).perform(replaceText("Only small shards, no large pieces. They smell funny."), closeSoftKeyboard());
        onView(allOf(withId(R.id.btnCreateListing), isDisplayed())).perform(click());

    }

    public static void deleteListing() {
        SystemTestUtils.deleteListing("Bag of Broken Glass");
    }

}
