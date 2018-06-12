package com.ctrlaltelite.copshop.logic.services.stubs;

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
        validationObject.setEndDateAndTimeValid(validateEndDateAndTime(listingObject.getAuctionStartDate(),
                                                                listingObject.getAuctionStartTime(),
                                                                listingObject.getAuctionEndDate(),
                                                                listingObject.getAuctionEndTime()));

        // Description
        validationObject.setDescriptionValid(validateDescription(listingObject.getDescription()));

    }

    /**
     * Determine if title form field is valid(non-empty)
     * @param title String containing the form field text
     * @return Boolean indicating if valid
     */
    private boolean validateTitle(String title){
        return title != null && !title.isEmpty();
    }

    /**
     * Determine if a currency form field is valid
     * @param value String containing the form field text
     * @return Boolean indicating if valid
     */
    private boolean validateBidPrice(String value){
        boolean isValid = true;

        if (value == null || value.isEmpty()) {
            isValid = false;
        } else {

            try {
                Float valueFloat = Float.valueOf(value);
            } catch (NumberFormatException e) {
                isValid = false;
            }

            if (isValid && value.contains(".")) {
                String[] priceParts = value.split("\\.");
                if (priceParts[1].length() > 2) {
                    isValid = false;
                }
            }
        }

        return isValid;
    }

    /**
     * Determine if a date and time form field is valid
     * @param date String containing the date form fields combined text data (Format: DD/MM/YEAR)
     * @param time String containing the time form fields combined text data (Format: HH:MM)
     * @return Boolean indicating if valid
     */
    private boolean validateDateAndTime(String date, String time){
        boolean isValid = true;

        if (date == null || (StringUtils.countMatches(date, "/") != 2) || !(date.length() == DATE_LENGTH)||
                time == null || (StringUtils.countMatches(time, ":") != 1) || !(time.length() == TIME_LENGTH)){
            isValid = false;
        } else {

            // Split the date and time strings into managable parts and store in array
            String[] dateParts = date.split("/");
            String[] timeParts = time.split(":");

            // Convert the form date parts into int values
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            // Get the current date in int values
            int currDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            int currMonth = Calendar.getInstance().get(Calendar.MONTH) + 1; // Unlike Day and Year, Month returns as -1, i.e. Jan is 00 and not 01
            int currYear = Calendar.getInstance().get(Calendar.YEAR);

            // Convert the form time parts into int values
            int hour = Integer.parseInt(timeParts[0]);
            int minutes = Integer.parseInt(timeParts[1]);

            // Get the current time in int values
            int currHour = Calendar.getInstance().get(Calendar.HOUR);
            int currMinutes = Calendar.getInstance().get(Calendar.MINUTE);

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

                if (isLeapYear(year)) {
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

            // ensure listing start date/time is after curr date/time
            isValid = isValid && validateDateInFuture(currYear, year, currMonth, month, currDay, day, currHour, hour, currMinutes, minutes);
        }
        return isValid;
    }

    /**
     * Determine if the given start date and time occurs before the end date and time given
     * @param startDate String containing a given start date (Format: DD/MM/YEAR)
     * @param startTime String containing a given start time (Format: HH:MM)
     * @param endDate String containing a given end date (Format: DD/MM/YEAR)
     * @param endTime String containing a given end time (Format: HH:MM)
     * @return Boolean indicating if valid(end time occurs after start time)
     */
    private boolean validateEndDateAndTime(String startDate, String startTime, String endDate, String endTime){
        boolean isValid = true;

        if (!validateDateAndTime(endDate, endTime)) {
            isValid = false;
        } else if (validateDateAndTime(startDate, startTime)) {
            // Split the date and time strings into managable parts and store in array
            String[] startDateParts = startDate.split("/");
            String[] startTimeParts = startTime.split(":");
            String[] endDateParts = endDate.split("/");
            String[] endTimeParts = endTime.split(":");

            // Convert the form date and time parts into int values
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

            // ensure end date/time is after start date/time
            isValid = validateDateInFuture(startYear, endYear, startMonth, endMonth, startDay, endDay, startHour, endHour, startMinutes, endMinutes);
        }// If start time isn't valid we can't check to see if end date is before it
        return isValid;
    }

    /**
     *
     * Validates the a given date range to ensure that start Date/Time doesn't occur before end Date/Time
     * @param startYear beginning year in the given start-end date range
     * @param endYear ending year in the given start-end date range
     * @param startMonth beginning month in the given start-end date range
     * @param endMonth ending month in the given start-end date range
     * @param startDay beginning day in the given start-end date range
     * @param endDay ending day in the given start-end date range
     * @param startHour beginning hour in the given start-end date range
     * @param endHour ending hour in the given start-end date range
     * @param startMinutes beginning min in the given start-end date range
     * @param endMinutes ending min in the given start-end date range
     * @return
     */
    private boolean validateDateInFuture(int startYear, int endYear, int startMonth, int endMonth, int startDay, int endDay, int startHour, int endHour, int startMinutes, int endMinutes) {
        boolean isValid = true;

        // After validating all form dates are valid, confirm they are future dates
        if (endYear == startYear) {
            if (endMonth == startMonth) {
                if (endDay == startDay) {
                    if (endHour == startHour) {
                        if (endMinutes <= startMinutes) {
                            isValid = false;
                        } // Current or future minute with current hour, day, month, and year, therefore valid
                    } else if (endHour < startHour) {
                        isValid = false;
                    } // Future hour, with current day, month and year, therefore valid
                } else if (endDay < startDay) {
                    isValid = false;
                } // Future day with current month and year, therefore valid
            } else if (endMonth < startMonth) {
                isValid = false;
            } // Future month with current year, therefore valid
        } // Future year, therefore valid

        return isValid;
    }

    /**
     * Determine if year passed as a parameter is a leap year
     * @param year int representing year to check
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

    /**
     * Determine if description form field is valid(non-empty)
     * @param description String containing the form field text
     * @return Boolean indicating if valid
     */
    private boolean validateDescription (String description){
        return description != null && !description.isEmpty();
    }
}
