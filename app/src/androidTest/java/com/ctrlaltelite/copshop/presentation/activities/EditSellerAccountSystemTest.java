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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EditSellerAccountSystemTest {

    @Rule
    public ActivityTestRule<ListingListActivity> mActivityTestRule = new ActivityTestRule<>(ListingListActivity.class);

    @Test
    public void editSellerAccountSystemTest() {

        String newEmail = CreateSellerAccountSystemTest.createSellerAccount();

        // We are on listing list page

        ViewInteraction drawerButton = onView(withContentDescription("Open navigation drawer"));
        drawerButton.perform(click());

        // Click on the first drawer item (Account Details)
        ViewInteraction button = onView(
                allOf(SystemTestUtils.childAtPosition(allOf(withId(R.id.design_navigation_view),
                        SystemTestUtils.childAtPosition(withId(R.id.nav_view), 0)), 1),
                        isDisplayed()));
        button.perform(click());

        onView(withId(R.id.btnEditSellerAccount)).perform(click());

        onView(withId(R.id.editTextOrganizationName)).perform(replaceText("The Doghouse"), closeSoftKeyboard());
        onView(withId(R.id.editTextEmail)).perform(replaceText(newEmail + "2"), closeSoftKeyboard());

        onView(withId(R.id.btnSaveSellerAccount)).perform(click());

        // Ensure the page reloaded the same (it is reloaded from DB)
        onView(withId(R.id.editTextOrganizationName)).check(matches(withText("The Doghouse")));
        onView(withId(R.id.editTextEmail)).check(matches(withText(newEmail + "2")));

        pressBack();
    }


}
