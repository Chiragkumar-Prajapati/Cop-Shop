package com.ctrlaltelite.copshop.logic.services.stubs;

import android.util.Log;
import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.logic.services.ICreateListingService;
import com.ctrlaltelite.copshop.objects.ListingFormValidationObject;
import com.ctrlaltelite.copshop.objects.ListingObject;
import org.apache.commons.lang3.StringUtils;

public class CreateListingService implements ICreateListingService {
    private static final int TIME_LENGTH = 5;
    private static final int DATE_LENGTH = 10;
    private static final int JAN = 1, FEB = 2, MAR = 3, APR = 4, MAY = 5, JUN = 6,
                             JUL = 7, AUG = 8, SEP = 9, OCT = 10, NOV = 11, DEC = 12;
    private static final int APP_SUPPORT_TILL_YEAR = 2050;
    private static final String TAG = "CreateNewListing";
    private static ListingFormValidationObject validationObject = new ListingFormValidationObject();

    private IListingModel listingModel;

    public CreateListingService(IListingModel listingModel) {
        this.listingModel = listingModel;
    }

    public ListingFormValidationObject create(ListingObject listingObject) {
        Boolean successful = this.validateInputForm(listingObject);

        if (successful) {
            listingModel.createNew(listingObject);
        }

        return validationObject;
    }

    /**
     * Ensures values in the fields of the CreateListingActivity form are valid.
     * If they aren't, the appropriate text boxes get red highlighting
     * to inform the user that they need to fix their input.
     *
     * Validates: initPrice, minBid, auctionStartDate, auctionStartTime,
     * auctionEndDate and auctionEndTime fields
     */
    private boolean validateInputForm(ListingObject listingObject) {

        String current;

        // ListingTitle
        current = listingObject.getTitle();
        validationObject.setTitleValid(validateTitle(current));

        // InitPrice
        current = listingObject.getInitPrice();
        validationObject.setInitPriceValid(validateBidPrice(current));

        // MinBid
        current = listingObject.getMinBid();
        validationObject.setMinBidValid(validateBidPrice(current));

        // AuctionStartDate
        current = listingObject.getAuctionStartDate();
        validationObject.setAuctionStartDateValid(validateDate(current));

        // AuctionStartTime
        current = listingObject.getAuctionStartTime();
        validationObject.setAuctionStartTimeValid(validateTime(current));

        // AuctionEndDate
        current = listingObject.getAuctionEndDate();
        validationObject.setAuctionEndDateValid(validateDate(current));

        // AuctionEndTime
        current = listingObject.getAuctionEndTime();
        validationObject.setAuctionEndTimeValid(validateTime(current));

        current = listingObject.getDescription();
        validationObject.setDescriptionValid(validateDescription(current));

        return validationObject.isAllValid();
    }

    private boolean validateTitle(String title){
        return title != null && !title.isEmpty();
    }

    private boolean validateBidPrice(String value){
        boolean isValid = true;

        if (value.contains(".")){
            String[] priceParts = value.split("\\.");
            if(priceParts[1].length() > 2){
                isValid = false;
            }
        }

        try{
            Float valueFloat = Float.valueOf(value);
        }
        catch (NumberFormatException e){
            isValid = false;
            Log.i(TAG, "Error parsing: " + value + " to Float." );
        }

        return isValid;
    }

    // Format: DD/MM/YEAR
    private boolean validateDate(String date){
        boolean isValid = true;

        if (date == null || (StringUtils.countMatches(date, "/") != 2) || !(date.length() == DATE_LENGTH)){
            isValid = false;
        }

        if (isValid) {
            String[] dateParts = date.split("/");

            // Month
            int month = Integer.parseInt(dateParts[1]);
            if (month < 1 || month > 12) {
                isValid = false;
            }

            // Day

            // 31 Days
            if (isValid && (month == JAN || month == MAR || month == MAY || month == JUL || month == AUG || month == OCT || month == DEC)) {
                int day = Integer.parseInt(dateParts[0]);
                if (day < 0 || day > 31) {
                    isValid = false;
                }
            }
            // 30 Days
            if (isValid && (month == APR || month == JUN || month == SEP || month == NOV)) {
                int day = Integer.parseInt(dateParts[0]);
                if (day < 0 || day > 30) {
                    isValid = false;
                }
            }

            // Year
            int year = Integer.parseInt(dateParts[2]);
            if (year < 0 || year > APP_SUPPORT_TILL_YEAR) {
                isValid = false;
            }

            // February - 28 days
            if (isValid && (month == FEB)) {
                int day = Integer.parseInt(dateParts[0]);

                if (isLeapYear(year)){
                    if (day < 0 || day > 29) {
                        isValid = false;
                    }
                }
                else {
                    if (day < 0 || day > 28) {
                        isValid = false;
                    }
                }
            }
        }
        return isValid;
    }

    // Format: HR:MN (24 HR)
    private boolean validateTime(String time){
        boolean isValid = true;

        if (time == null || time.isEmpty() || !(time.contains(":")) || (StringUtils.countMatches(time, ":") != 1) || !(time.length() == TIME_LENGTH)){
            isValid = false;
        }

        if (isValid) {
            String[] timeParts = time.split(":");

            // Hour
            int hour = Integer.parseInt(timeParts[0]);
            if (hour < 0 || hour > 23) {
                isValid = false;
            }

            // Minutes
            int minutes = Integer.parseInt(timeParts[1]);
            if (minutes < 0 || minutes > 59) {
                isValid = false;
            }
        }
        return isValid;
    }

    /**
     * Determine if year passed as a parameter is a leap year
     *
     * @param year int to determine leapness
     * @return Boolean indicating if year is a leap year
     */
    private boolean isLeapYear (int year){
        boolean isLeap = false;

        if(year%4 == 0){
            if(year%100 == 0){
                if(year%400 == 0){
                    isLeap = true;
                }
            }
            else{
                isLeap = true;
            }
        }

        return isLeap;
    }

    private boolean validateDescription (String description){
        return description != null && !description.isEmpty();
    }
}
