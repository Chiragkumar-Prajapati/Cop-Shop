package com.ctrlaltelite.copshop;

import com.ctrlaltelite.copshop.presentation.activities.BiddingSystemTest;
import com.ctrlaltelite.copshop.presentation.activities.CreateBuyerAccountSystemTest;
import com.ctrlaltelite.copshop.presentation.activities.CreateListingSystemTest;
import com.ctrlaltelite.copshop.presentation.activities.CreateSellerAccountSystemTest;
import com.ctrlaltelite.copshop.presentation.activities.EditBuyerAccountSystemTest;
import com.ctrlaltelite.copshop.presentation.activities.EditListingSystemTest;
import com.ctrlaltelite.copshop.presentation.activities.EditSellerAccountSystemTest;
import com.ctrlaltelite.copshop.presentation.activities.FilterListingsSystemTest;
import com.ctrlaltelite.copshop.presentation.activities.SystemTestUtils;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CreateBuyerAccountSystemTest.class,
        CreateSellerAccountSystemTest.class,
        CreateListingSystemTest.class,

        EditListingSystemTest.class,
        EditBuyerAccountSystemTest.class,
        EditSellerAccountSystemTest.class,

        FilterListingsSystemTest.class,
        BiddingSystemTest.class,
})
public class AllAcceptanceTests
{

}