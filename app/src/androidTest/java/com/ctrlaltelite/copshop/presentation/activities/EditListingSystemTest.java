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
public class EditListingSystemTest {

    @Rule
    public ActivityTestRule<ListingListActivity> mActivityTestRule = new ActivityTestRule<>(ListingListActivity.class);

    @Test
    public void editListingSystemTest() {
        SystemTestUtils.loginAsSeller("local@police.com", "12345");

        SystemTestUtils.deleteExtraListings();

        CreateListingSystemTest.createListing();

        // We are on listing list, go to the listing

        ViewInteraction listingRowPrice = onView(
                allOf(withId(R.id.listing_list_price),
                        withParent(allOf(withParent(withId(R.id.listing_list)), withChild(withText("Bag of Broken Glass"))))));
        listingRowPrice.perform(click());

        // Go to the listing edit screen
        onView(withId(R.id.view_listing_edit_button)).perform(click());

        onView(withId(R.id.txtInitialPrice)).perform(replaceText("800.00"), closeSoftKeyboard());
        onView(withId(R.id.txtAreaDescription)).perform(replaceText("Only small shards, no large pieces. Price increased."), closeSoftKeyboard());


        onView(withId(R.id.btnSaveListing)).perform(click());

        ViewInteraction listingRowPriceNew = onView(
                allOf(withId(R.id.listing_list_price),
                        withParent(allOf(withParent(withId(R.id.listing_list)), withChild(withText("Bag of Broken Glass"))))));
        listingRowPriceNew.check(matches(withText("$800.00,")));

        ViewInteraction listingRowDescNew = onView(
                allOf(withId(R.id.listing_list_description),
                        withParent(allOf(withParent(withId(R.id.listing_list)), withChild(withText("Bag of Broken Glass"))))));
        listingRowDescNew.check(matches(withText("Only small shards, no large pieces. Price increased.")));

        CreateListingSystemTest.deleteListing();
    }

}
