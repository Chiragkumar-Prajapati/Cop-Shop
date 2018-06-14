package com.ctrlaltelite.copshop.logic.services;

import com.ctrlaltelite.copshop.objects.ListingFormValidationObject;
import com.ctrlaltelite.copshop.objects.ListingObject;

public interface ICreateListingService {

    /**
     * Creates a temp object that is used to determine the current validity of the form.
     * @param listingObject The object containing the field values to be validated
     * @return ListingFormValidationObject containing validity of fields in form
     */
    ListingFormValidationObject validate(ListingObject listingObject);

    /**
     * Creates a new listing object and inserts it into the database
     * @param listingObject The Listing object to be inserted into the DB
     * @return String containing the highest order PK on success
     */
    String saveNewListing(ListingObject listingObject);

}
