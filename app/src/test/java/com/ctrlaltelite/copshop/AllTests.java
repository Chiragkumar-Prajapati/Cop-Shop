package com.ctrlaltelite.copshop;

import com.ctrlaltelite.copshop.tests.AccountServiceTests;
import com.ctrlaltelite.copshop.tests.BuyerModelTests;
import com.ctrlaltelite.copshop.tests.CreateListingServiceTests;
import com.ctrlaltelite.copshop.tests.DatabaseTests;
import com.ctrlaltelite.copshop.tests.ListingModelTests;
import com.ctrlaltelite.copshop.tests.ListingServiceTests;
import com.ctrlaltelite.copshop.tests.SellerModelTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DatabaseTests.class,

        BuyerModelTests.class,
        ListingModelTests.class,
        SellerModelTests.class,

        CreateListingServiceTests.class,
        AccountServiceTests.class,
        ListingServiceTests.class,
})
public class AllTests
{

}
