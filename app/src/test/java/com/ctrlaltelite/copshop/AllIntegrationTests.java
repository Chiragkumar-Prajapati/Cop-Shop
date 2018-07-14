package com.ctrlaltelite.copshop;

import com.ctrlaltelite.copshop.tests.integration.AccountServiceIntegrationTests;
import com.ctrlaltelite.copshop.tests.integration.BidServiceIntegrationTests;
import com.ctrlaltelite.copshop.tests.integration.CreateListingServiceIntegrationTests;
import com.ctrlaltelite.copshop.tests.integration.ListingServiceIntegrationTests;
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
        CreateListingServiceIntegrationTests.class,
        AccountServiceIntegrationTests.class,
        ListingServiceIntegrationTests.class,
        BidServiceIntegrationTests.class,
})
public class AllIntegrationTests
{

}
