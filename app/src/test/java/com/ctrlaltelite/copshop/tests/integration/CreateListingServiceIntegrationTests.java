package com.ctrlaltelite.copshop.tests.integration;

import com.ctrlaltelite.copshop.logic.services.ICreateListingService;
import com.ctrlaltelite.copshop.logic.services.stubs.CreateListingService;
import com.ctrlaltelite.copshop.objects.ListingFormValidationObject;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.persistence.ISellerModel;
import com.ctrlaltelite.copshop.persistence.hsqldb.ListingModelHSQLDB;
import com.ctrlaltelite.copshop.persistence.hsqldb.SellerModelHSQLDB;
import com.ctrlaltelite.copshop.tests.db.HSQLDBTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CreateListingServiceIntegrationTests {
    private HSQLDBTestUtil dbUtil =  new HSQLDBTestUtil();

    @Before
    public void setup() {
        dbUtil.setup();
    }

    @After
    public void teardown() {
        dbUtil.reset();
    }

    @Test
    public void saveNewListing_addsListingAndReturnsId() {
        IListingModel listingModel = new ListingModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        ICreateListingService createListingService = new CreateListingService(listingModel);

        SellerAccountObject sAccount1 = new SellerAccountObject("ignored","name1",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        String sId1 = sellerModel.createNew(sAccount1);
        assertNotNull("Seller was not created", sId1);

        ListingObject listing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/02/2020 12:00", "category", sId1);

        // Save the new listings
        String id1 = createListingService.saveNewListing(listing);
        String id2 = createListingService.saveNewListing(listing);
        String id3 = createListingService.saveNewListing(listing);

        // Verify they were created
        assertNotNull("Listing was not created", id1);
        assertNotNull("Listing was not created", id2);
        assertNotNull("Listing was not created", id3);

        assertTrue("Listing was not created", null != listingModel.fetch(id1));
        assertTrue("Listing was not created", null != listingModel.fetch(id1));
        assertTrue("Listing was not created", null != listingModel.fetch(id1));
    }

    @Test
    public void create_verifiesValidationObjectCreation() {
        IListingModel listingModel = new ListingModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        ICreateListingService createListingService = new CreateListingService(listingModel);

        SellerAccountObject sAccount1 = new SellerAccountObject("ignored","name1",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        String sId1 = sellerModel.createNew(sAccount1);
        assertNotNull("Seller was not created", sId1);

        ListingFormValidationObject validationObject;
        ListingObject validListing;
        ListingObject invalidListing;

        //--------------------------------------Valid listing objects for testing------------------------------------------------//
        validListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/02/2020 12:00", "category", sId1);
        validationObject = createListingService.validate(validListing);
        assertTrue("Form was incorrectly validated", validationObject.isAllValid());

        validListing = new ListingObject("ignored","title", "description", "2.0", "2.0",
                "02/02/2019 10:00", "02/02/2020 12:00", "category", sId1);
        validationObject = createListingService.validate(validListing);
        assertTrue("Form was incorrectly validated", validationObject.isAllValid());

        validListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/02/2019 11:00", "category", sId1);
        validationObject = createListingService.validate(validListing);
        assertTrue("Form was incorrectly validated", validationObject.isAllValid());


        //--------------------------------------Invalid listing objects for testing-----------------------------------------------//

        //================================================Invalid due to null=====================================================//
        invalidListing = new ListingObject("ignored", null, "description", "2", "2",
                "02/02/2019 10:00", "02/02/2020 10:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getTitleValid());

        invalidListing = new ListingObject("ignored","title", null, "2", "2",
                "02/02/2019 10:00", "02/02/2020 10:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getDescriptionValid());

        invalidListing = new ListingObject("ignored","title", "description", null, "2",
                "02/02/2019 10:00", "02/02/2020 10:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getInitPriceValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", null,
                "02/02/2019 10:00", "02/02/2020 10:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getMinBidValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                null, "02/02/2020 10:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getStartDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", null, "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        //==============================================Invalid due to empty String==============================================//
        invalidListing = new ListingObject("ignored", "", "description", "2", "2",
                "02/02/2019 10:00", "02/02/2020 10:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getTitleValid());

        invalidListing = new ListingObject("ignored","title", "", "2", "2",
                "02/02/2019 10:00", "02/02/2020 10:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getDescriptionValid());

        invalidListing = new ListingObject("ignored","title", "description", "", "2",
                "02/02/2019 10:00", "02/02/2020 10:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getInitPriceValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "",
                "02/02/2019 10:00", "02/02/2020 10:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getMinBidValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "",
                "02/02/2019 10:00", "02/02/2020 10:00", "", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getMinBidValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                " 10:00", "02/02/2020 10:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getStartDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 ", "02/02/2020 10:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getStartDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", " 10:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/02/2020 ", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());


        //=============================Invalid due to time/date: e.g: in the past, improper format, end = start times=============//
        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2017 10:00", "02/02/2020 12:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getStartDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:59", "02/02/2019 10:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/02/2019 9:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "01/02/2019 12:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/01/2019 12:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2.0", "2.0",
                "02/02/2019 10:00", "02/02/2019 10:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/02/2018 10:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "2019/02/02 10:00", "02/02/2020 12:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getStartDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/2020/02 12:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 1000", "02/02/2020 12:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getStartDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/02/2020 1200", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());


        //=============================Invalid due to currency: e.g decimal too large, invalid format================================//
        invalidListing = new ListingObject("ignored","title", "description", "2.1234", "2",
                "02/02/2019 10:00", "02/02/2020 12:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getInitPriceValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2.1234",
                "02/02/2019 10:00", "02/02/2020 12:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getMinBidValid());

        invalidListing = new ListingObject("ignored","title", "description", ".", "2",
                "02/02/2019 10:00", "02/02/2020 12:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getInitPriceValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", ".",
                "02/02/2019 10:00", "02/02/2020 12:00", "category", sId1);
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getMinBidValid());
    }
}
