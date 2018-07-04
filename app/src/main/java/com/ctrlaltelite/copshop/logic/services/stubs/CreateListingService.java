package com.ctrlaltelite.copshop.logic.services.stubs;

import java.util.Calendar;
import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.logic.services.ICreateListingService;
import com.ctrlaltelite.copshop.objects.ListingFormValidationObject;
import com.ctrlaltelite.copshop.objects.ListingObject;
import org.apache.commons.lang3.StringUtils;

public class CreateListingService implements ICreateListingService {

    private IListingModel listingModel;

    public CreateListingService(IListingModel listingModel) {
        this.listingModel = listingModel;
    }

    /**
     * Saves a listingObject to the database
     * @param listingObject ListingObject that contains the form data
     * @return String containing the next PK of the table
     */
    public String saveNewListing(ListingObject listingObject) {
        return listingModel.createNew(listingObject);
    }

    /**
     * Create an object to store the current validity of the new listing form data
     * @param listingObject ListingObject that contains the form data
     * @return ListingFormValidationObject which contains the validity of all form fields
     */
    public ListingFormValidationObject validate(ListingObject listingObject) {
        return this.validateInputForm(listingObject);
    }

    /**
     * Ensures values in the fields of the CreateListingActivity form are valid.
     * If they aren't, the appropriate text boxes get red highlighting
     * to inform the user that they need to fix their input.
     *
     * Validates: txtTitle, initPrice, minBid, auctionStartDate, auctionStartTime,
     * auctionEndDate, auctionEndTime and description fields
     * @param listingObject The listing to validate
     * @return ListingFormValidationObject
     */
    private ListingFormValidationObject validateInputForm(ListingObject listingObject) {
        ListingFormValidationObject validationObject = new ListingFormValidationObject();

        // ListingTitle
        validationObject.validateTitle(listingObject.getTitle());

        // InitPrice
        validationObject.validateInitPrice(listingObject.getInitPrice());

        // MinBid
        validationObject.validateMinBid(listingObject.getMinBid());

        // StartDateAndTime
        validationObject.validateStartDateAndTime(listingObject.getAuctionStartDate());

        // EndDateAndTime
        validationObject.validateEndDateAndTime(listingObject.getAuctionStartDate(), listingObject.getAuctionEndDate());

        // Category
        validationObject.validateCategory(listingObject.getCategory());

        // Description
        validationObject.validateDescription(listingObject.getDescription());

        return validationObject;
    }
}
