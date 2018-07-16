package com.ctrlaltelite.copshop.presentation.activities;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.ctrlaltelite.copshop.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class BiddingSystemTest {

    @Rule
    public ActivityTestRule<ListingListActivity> mActivityTestRule = new ActivityTestRule<>(ListingListActivity.class);

    @Test
    public void biddingSystemTest() {
        SystemTestUtils.loginAsSeller("local@police.com", "12345");

        SystemTestUtils.deleteExtraListings();

        CreateListingSystemTest.createListing();

        SystemTestUtils.loginAsBuyer("john@email.com", "john");

        ViewInteraction listingRowPrice = onView(
                allOf(withId(R.id.listing_list_price),
                        withParent(allOf(withParent(withId(R.id.listing_list)), withChild(withText("Bag of Broken Glass"))))));
        listingRowPrice.perform(click());

        onView(allOf(withId(R.id.view_listing_bid_button), withText("Bid"))).perform(click());

        onView(withId(R.id.listing_view_bid_amount)).check(matches(withText("$100.00")));
        onView(withId(R.id.view_listing_bid_input)).check(matches(withText("110.00")));
        onView(withId(R.id.view_listing_bid_input)).check(matches(withText("110.00")));
        onView(withId(R.id.view_listing_min_bid)).check(matches(withText("$110.00")));
        onView(allOf(withId(R.id.view_listing_bid_input), withText("110.00"))).perform(replaceText("400.00"));
        onView(allOf(withId(R.id.view_listing_bid_input), withText("400.00"))).perform(closeSoftKeyboard());
        onView(allOf(withId(R.id.view_listing_bid_button), withText("Bid"))).perform(click());

        pressBack();

        listingRowPrice.check(matches(withText("$410.00,")));

        SystemTestUtils.loginAsSeller("local@police.com", "12345");
        CreateListingSystemTest.deleteListing();
    }

}
