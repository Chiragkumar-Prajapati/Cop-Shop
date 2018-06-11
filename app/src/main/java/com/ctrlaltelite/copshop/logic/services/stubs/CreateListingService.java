package com.ctrlaltelite.copshop.logic.services.stubs;

import android.util.Log;
import java.util.Calendar;
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

    public String saveNewListing(ListingObject listingObject) {
        return listingModel.createNew(listingObject);
    }

    public ListingFormValidationObject create(ListingObject listingObject) {
        this.validateInputForm(listingObject);

        return validationObject;
    }

    /**
     * Ensures values in the fields of the CreateListingActivity form are valid.
     * If they aren't, the appropriate text boxes get red highlighting
     * to inform the user that they need to fix their input.
     *
     * Validates: txtTitle, initPrice, minBid, auctionStartDate, auctionStartTime,
     * auctionEndDate, auctionEndTime and description fields
     */
    private void validateInputForm(ListingObject listingObject) {

        // ListingTitle
        validationObject.setTitleValid(validateTitle(listingObject.getTitle()));

        // InitPrice
        validationObject.setInitPriceValid(validateBidPrice(listingObject.getInitPrice()));

        // MinBid
        validationObject.setMinBidValid(validateBidPrice(listingObject.getMinBid()));

        // StartDateAndTime
        validationObject.setStartDateAndTimeValid(validateDateAndTime(listingObject.getAuctionStartDate(),
                                                                      listingObject.getAuctionStartTime()));

        // EndDateAndTime
        validationObject.setEndDateAndTimeValid(validateDateAndTime(listingObject.getAuctionEndDate(),
                                                                    listingObject.getAuctionEndTime()) &&
                                                                    isEndAfterStart(listingObject.getAuctionStartDate(),
                                                                    listingObject.getAuctionStartTime(),
                                                                    listingObject.getAuctionEndDate(),
                                                                    listingObject.getAuctionEndTime()));

        // Description
        validationObject.setDescriptionValid(validateDescription(listingObject.getDescription()));

    }

    private boolean validateTitle(String title){
        return title != null && !title.isEmpty();
    }

    private boolean validateBidPrice(String value){
        boolean isValid = true;

        if (value == null || value.isEmpty()) {
            isValid = false;
        } else {

            if (value.contains(".")) {
                String[] priceParts = value.split("\\.");
                if (priceParts[1].length() > 2) {
                    isValid = false;
                }
            }

            try {
                Float valueFloat = Float.valueOf(value);
            } catch (NumberFormatException e) {
                isValid = false;
                Log.i(TAG, "Error parsing: " + value + " to Float.");
            }
        }

        return isValid;
    }

    // Format: DD/MM/YEAR
    private boolean validateDateAndTime(String date, String time){
        boolean isValid = true;

        if (date == null || (StringUtils.countMatches(date, "/") != 2) || !(date.length() == DATE_LENGTH)||
                time == null || (StringUtils.countMatches(time, ":") != 1) || !(time.length() == TIME_LENGTH)){
            isValid = false;
        } else {

            //split the date and time strings into managable parts and store in array
            String[] dateParts = date.split("/");
            String[] timeParts = time.split(":");

            //convert the form date parts into int values
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            //get the current date in int values
            int currDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            int currMonth = Calendar.getInstance().get(Calendar.MONTH) +1; //plus one to have first month int value be 1
            int currYear = Calendar.getInstance().get(Calendar.YEAR);

            //convert the form time parts into int values
            int hour = Integer.parseInt(timeParts[0]);
            int minutes = Integer.parseInt(timeParts[1]);

            //get the current time in int values
            int currHour = Calendar.getInstance().get(Calendar.HOUR);
            int currMinute = Calendar.getInstance().get(Calendar.MINUTE);

            // Month
            if (month < 1 || month > 12) {
                isValid = false;
            }

            // Days //
            // 31 Days
            if (isValid && (month == JAN || month == MAR || month == MAY || month == JUL || month == AUG || month == OCT || month == DEC)) {
                if (day < 0 || day > 31) {
                    isValid = false;
                }
            }
            // 30 Days
            if (isValid && (month == APR || month == JUN || month == SEP || month == NOV)) {
                if (day < 0 || day > 30) {
                    isValid = false;
                }
            }

            // Year
            if (year < currYear || year > APP_SUPPORT_TILL_YEAR) {
                isValid = false;
            }

            // February - 28 days
            if (isValid && (month == FEB)) {

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

            // Hours
            if (hour < 0 || hour > 23) {
                isValid = false;
            }

            // Minutes
            if (minutes < 0 || minutes > 59) {
                isValid = false;
            }

            //after validating all form dates are valid, confirm they are future dates
            if (year == currYear) {
                if (month == currMonth) {
                    if (day == currDay) {
                        if (hour == currHour) {
                            if (minutes < currMinute) {
                                isValid = false;
                            } //current or future minute with current hour, day, month, and year, therefore valid
                        } else if (hour < currHour) {
                            isValid = false;
                        } //future hour, with current day, month and year, therefore valid
                    } else if (day < currDay) {
                        isValid = false;
                    } //future day with current month and year, therefore valid
                } else if (month < currMonth) {
                    isValid = false;
                } //future month with current year, therefore valid
            } //future year, therefore valid
        }
        return isValid;
    }

    private boolean isEndAfterStart(String startDate, String startTime, String endDate, String endTime){
        boolean isValid = true;

        if (startDate == null || (StringUtils.countMatches(startDate, "/") != 2) || !(startDate.length() == DATE_LENGTH)||
                endDate == null || (StringUtils.countMatches(endDate, "/") != 2) || !(endDate.length() == DATE_LENGTH)||
                startTime == null || (StringUtils.countMatches(startTime, ":") != 1) || !(startTime.length() == TIME_LENGTH) ||
                endTime == null || (StringUtils.countMatches(endTime, ":") != 1) || !(endTime.length() == TIME_LENGTH)){

            isValid = false;
        } else {

            //split the date and time strings into managable parts and store in array
            String[] startDateParts = startDate.split("/");
            String[] startTimeParts = startTime.split(":");
            String[] endDateParts = endDate.split("/");
            String[] endTimeParts = endTime.split(":");

            //convert the form date and time parts into int values
            int startDay = Integer.parseInt(startDateParts[0]);
            int startMonth = Integer.parseInt(startDateParts[1]);
            int startYear = Integer.parseInt(startDateParts[2]);
            int startHour = Integer.parseInt(startTimeParts[0]);
            int startMinutes = Integer.parseInt(startTimeParts[1]);

            int endDay = Integer.parseInt(endDateParts[0]);
            int endMonth = Integer.parseInt(endDateParts[1]);
            int endYear = Integer.parseInt(endDateParts[2]);
            int endHour = Integer.parseInt(endTimeParts[0]);
            int endMinutes = Integer.parseInt(endTimeParts[1]);

            //after validating all form dates are valid, confirm they are future dates
            if (endYear == startYear) {
                if (endMonth == startMonth) {
                    if (endDay == startDay) {
                        if (endHour == startHour) {
                            if (endMinutes <= startMinutes) {
                                isValid = false;
                            } //current or future minute with current hour, day, month, and year, therefore valid
                        } else if (endHour < startHour) {
                            isValid = false;
                        } //future hour, with current day, month and year, therefore valid
                    } else if (endDay < startDay) {
                        isValid = false;
                    } //future day with current month and year, therefore valid
                } else if (endMonth < startMonth) {
                    isValid = false;
                } //future month with current year, therefore valid
            } //future year, therefore valid
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
