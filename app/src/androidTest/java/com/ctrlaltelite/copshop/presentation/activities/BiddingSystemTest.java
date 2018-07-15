package com.ctrlaltelite.copshop.presentation.activities;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ctrlaltelite.copshop.R;
import com.ctrlaltelite.copshop.db.HSQLDBSystemTestUtil;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class BiddingSystemTest {
//    private HSQLDBSystemTestUtil dbUtil =  new HSQLDBSystemTestUtil();
//
//    @Before
//    public void setup() {
//        dbUtil.setup();
//    }
//
//    @After
//    public void teardown() {
//        dbUtil.reset();
//    }


    @Rule
    public ActivityTestRule<ListingListActivity> mActivityTestRule = new ActivityTestRule<>(ListingListActivity.class);

    @Test
    public void biddingSystemTest() {
        ViewInteraction appCompatImageButton = onView(withContentDescription("Open navigation drawer"));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        1),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        try {
            ViewInteraction appCompatEditText = onView(
                    allOf(withId(R.id.email),
                            isDisplayed()));
            appCompatEditText.perform(replaceText("john@email.com"), closeSoftKeyboard());

            ViewInteraction appCompatEditText2 = onView(
                    allOf(withId(R.id.password),
                            isDisplayed()));
            appCompatEditText2.perform(replaceText("john"), closeSoftKeyboard());

            ViewInteraction appCompatButton = onView(
                    allOf(withId(R.id.btnLogin), withText("Login"),
                            isDisplayed()));
            appCompatButton.perform(click());
        }
        catch (NoMatchingViewException e) {
            pressBack();
        }

        // Logged in as Buyer

        DataInteraction constraintLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.listing_list),
                        childAtPosition(
                                withId(R.id.listing_scroller),
                                0)))
                .atPosition(2);
        constraintLayout.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.view_listing_bid_button), withText("Bid"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                9),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.listing_view_bid_amount), withText("$10.00"),
                        isDisplayed()));
        textView.check(matches(withText("$10.00")));

        ViewInteraction editText = onView(
                allOf(withId(R.id.view_listing_bid_input), withText("11.00"),
                        isDisplayed()));
        editText.check(matches(withText("11.00")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.view_listing_min_bid), withText("$11.00"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        1),
                                2),
                        isDisplayed()));
        textView2.check(matches(withText("$11.00")));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.view_listing_bid_input), withText("11.00"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                8),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("40.00"));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.view_listing_bid_input), withText("40.00"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                8),
                        isDisplayed()));
        appCompatEditText4.perform(closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.view_listing_bid_button), withText("Bid"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.design.widget.CoordinatorLayout")),
                                        1),
                                9),
                        isDisplayed()));
        appCompatButton3.perform(click());

        pressBack();

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.listing_list_price), withText("$41.00,"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.listing_list),
                                        2),
                                4),
                        isDisplayed()));
        textView3.check(matches(withText("$41.00,")));

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
