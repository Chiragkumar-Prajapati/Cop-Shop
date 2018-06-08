package com.ctrlaltelite.copshop.logic.services;

import com.ctrlaltelite.copshop.objects.ListingFormValidationObject;
import com.ctrlaltelite.copshop.objects.ListingObject;

public interface ICreateListingService {

    /**
     * Creates a new listing and inserts it into the database.
     * First validates input fields and if all goes well,
     * adds the listing to the database using the ListingModel.
     *
     * @return ListingFormValidationObject containing validity of fields in form
     */
    ListingFormValidationObject create(ListingObject listingObject);

}
