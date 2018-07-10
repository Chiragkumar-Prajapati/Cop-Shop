package com.ctrlaltelite.copshop;

import com.ctrlaltelite.copshop.tests.unit.AccountServiceTests;
import com.ctrlaltelite.copshop.tests.unit.BidModelTests;
import com.ctrlaltelite.copshop.tests.unit.BidServiceTests;
import com.ctrlaltelite.copshop.tests.unit.BuyerModelTests;
import com.ctrlaltelite.copshop.tests.unit.CreateListingServiceTests;
import com.ctrlaltelite.copshop.tests.unit.DatabaseTests;
import com.ctrlaltelite.copshop.tests.unit.ListingModelTests;
import com.ctrlaltelite.copshop.tests.unit.ListingServiceTests;
import com.ctrlaltelite.copshop.tests.unit.SellerModelTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DatabaseTests.class,

        BuyerModelTests.class,
        ListingModelTests.class,
        SellerModelTests.class,
        BidModelTests.class,

        CreateListingServiceTests.class,
        AccountServiceTests.class,
        ListingServiceTests.class,
        BidServiceTests.class,
})
public class AllUnitTests
{

}
