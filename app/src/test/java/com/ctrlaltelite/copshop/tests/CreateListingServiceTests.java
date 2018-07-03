package com.ctrlaltelite.copshop.tests;

import com.ctrlaltelite.copshop.logic.services.ICreateListingService;
import com.ctrlaltelite.copshop.logic.services.stubs.CreateListingService;
import com.ctrlaltelite.copshop.objects.ListingFormValidationObject;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;
import com.ctrlaltelite.copshop.persistence.database.stubs.MockDatabaseStub;
import com.ctrlaltelite.copshop.persistence.stubs.ListingModel;

import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CreateListingServiceTests {

    @Test
    public void saveNewListing_addsListingAndReturnsId() {
        IDatabase database = new MockDatabaseStub();
        IListingModel listingModel = new ListingModel(database);
        ICreateListingService createListingService = new CreateListingService(listingModel);

        ListingObject listing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/02/2020 12:00", "category", "sellerId");

        // Save the new listings
        String id1 = createListingService.saveNewListing(listing);
        String id2 = createListingService.saveNewListing(listing);
        String id3 = createListingService.saveNewListing(listing);

        // Verify they were created
        assertTrue("Row was not created", database.rowExists("Listings", id1));
        assertTrue("Row was not created", database.rowExists("Listings", id2));
        assertTrue("Row was not created", database.rowExists("Listings", id3));
    }

    @Test
    public void create_verifiesValidationObjectCreation() {
        IDatabase database = new MockDatabaseStub();
        IListingModel listingModel = new ListingModel(database);
        ICreateListingService createListingService = new CreateListingService(listingModel);
        ListingFormValidationObject validationObject;
        ListingObject validListing;
        ListingObject invalidListing;

        //--------------------------------------Valid listing objects for testing------------------------------------------------//
        validListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/02/2020 12:00", "category", "sellerId");
        validationObject = createListingService.validate(validListing);
        assertTrue("Form was incorrectly validated", validationObject.isAllValid());

        validListing = new ListingObject("ignored","title", "description", "2.0", "2.0",
                "02/02/2019 10:00", "02/02/2020 12:00", "category", "sellerId");
        validationObject = createListingService.validate(validListing);
        assertTrue("Form was incorrectly validated", validationObject.isAllValid());

        validListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/02/2019 11:00", "category", "sellerId");
        validationObject = createListingService.validate(validListing);
        assertTrue("Form was incorrectly validated", validationObject.isAllValid());


        //--------------------------------------Invalid listing objects for testing-----------------------------------------------//

        //================================================Invalid due to null=====================================================//
        invalidListing = new ListingObject("ignored", null, "description", "2", "2",
                "02/02/2019 10:00", "02/02/2020 10:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getTitleValid());

        invalidListing = new ListingObject("ignored","title", null, "2", "2",
                "02/02/2019 10:00", "02/02/2020 10:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getDescriptionValid());

        invalidListing = new ListingObject("ignored","title", "description", null, "2",
                "02/02/2019 10:00", "02/02/2020 10:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getInitPriceValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", null,
                "02/02/2019 10:00", "02/02/2020 10:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getMinBidValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                null, "02/02/2020 10:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getStartDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", null, "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        //==============================================Invalid due to empty String==============================================//
        invalidListing = new ListingObject("ignored", "", "description", "2", "2",
                "02/02/2019 10:00", "02/02/2020 10:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getTitleValid());

        invalidListing = new ListingObject("ignored","title", "", "2", "2",
                "02/02/2019 10:00", "02/02/2020 10:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getDescriptionValid());

        invalidListing = new ListingObject("ignored","title", "description", "", "2",
                "02/02/2019 10:00", "02/02/2020 10:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getInitPriceValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "",
                "02/02/2019 10:00", "02/02/2020 10:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getMinBidValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "",
                "02/02/2019 10:00", "02/02/2020 10:00", "", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getMinBidValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                " 10:00", "02/02/2020 10:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getStartDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 ", "02/02/2020 10:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getStartDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", " 10:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/02/2020 ", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());


        //=============================Invalid due to time/date: e.g: in the past, improper format, end = start times=============//
        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2017 10:00", "02/02/2020 12:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getStartDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:59", "02/02/2019 10:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/02/2019 9:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "01/02/2019 12:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/01/2019 12:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2.0", "2.0",
                "02/02/2019 10:00", "02/02/2019 10:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/02/2018 10:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "2019/02/02 10:00", "02/02/2020 12:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getStartDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/2020/02 12:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 1000", "02/02/2020 12:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getStartDateAndTimeValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2",
                "02/02/2019 10:00", "02/02/2020 1200", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getEndDateAndTimeValid());


        //=============================Invalid due to currency: e.g decimal too large, invalid format================================//
        invalidListing = new ListingObject("ignored","title", "description", "2.1234", "2",
                "02/02/2019 10:00", "02/02/2020 12:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getInitPriceValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", "2.1234",
                "02/02/2019 10:00", "02/02/2020 12:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getMinBidValid());

        invalidListing = new ListingObject("ignored","title", "description", ".", "2",
                "02/02/2019 10:00", "02/02/2020 12:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getInitPriceValid());

        invalidListing = new ListingObject("ignored","title", "description", "2", ".",
                "02/02/2019 10:00", "02/02/2020 12:00", "category", "sellerId");
        validationObject = createListingService.validate(invalidListing);
        assertFalse("Form was incorrectly validated", validationObject.isAllValid());
        assertFalse("Expected invalid field was valid", validationObject.getMinBidValid());
    }
}
