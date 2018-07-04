package com.ctrlaltelite.copshop.objects;

import com.ctrlaltelite.copshop.logic.services.utilities.DateUtility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Data object that stores (Boolean) values for
 * each field in the CreateNewListing form indicating
 * whether or not data in it is valid
 */
public class ListingFormValidationObject {

    private static final int MIN_DATE_LEN = 12;

    /**
     * Initially assume fields are valid,
     * then we test if they are not valid.
     */
    public ListingFormValidationObject() {
        this.titleValid = true;
        this.descriptionValid = true;
        this.initPriceValid = true;
        this.minBidValid = true;
        this.startDateAndTimeValid = true;
        this.categoryValid = true;
        this.endDateAndTimeValid = true;
    }

    private Boolean titleValid;
    private Boolean descriptionValid;
    private Boolean initPriceValid;
    private Boolean minBidValid; // Minimum amount by which a bid can increment
    private Boolean startDateAndTimeValid; // Format - Date: DD/MM/YEAR Time: HR:MN (24 HR)
    private Boolean endDateAndTimeValid; // Format - Date: DD/MM/YEAR Time: HR:MN (24 HR)
    private Boolean categoryValid;

    public Boolean getTitleValid() {
        return titleValid;
    }

    private void setTitleValid(Boolean titleValid) {
        this.titleValid = titleValid;
    }

    public Boolean getDescriptionValid() {
        return descriptionValid;
    }

    private void setDescriptionValid(Boolean descriptionValid) {
        this.descriptionValid = descriptionValid;
    }

    public Boolean getInitPriceValid() {
        return initPriceValid;
    }

    private void setInitPriceValid(Boolean initPriceValid) {
        this.initPriceValid = initPriceValid;
    }

    public Boolean getMinBidValid() {
        return minBidValid;
    }

    private void setMinBidValid(Boolean minBidValid) {
        this.minBidValid = minBidValid;
    }

    public Boolean getStartDateAndTimeValid() {
        return startDateAndTimeValid;
    }

    private void setStartDateAndTimeValid(Boolean auctionStartDateValid) {
        this.startDateAndTimeValid = auctionStartDateValid;
    }

    public Boolean getEndDateAndTimeValid() {
        return endDateAndTimeValid;
    }

    private void setEndDateAndTimeValid(Boolean auctionEndDateValid) {
        this.endDateAndTimeValid = auctionEndDateValid;
    }

    public Boolean getCategoryValid() {
        return categoryValid;
    }

    private void setCategoryValid(Boolean categoryValid) {
        this.categoryValid = categoryValid;
    }

    /**
     *
     * @return Boolean indicating whether all field values are valid
     */
    public Boolean isAllValid(){
        return (titleValid &&
                descriptionValid &&
                initPriceValid &&
                minBidValid &&
                startDateAndTimeValid &&
                endDateAndTimeValid);
    }


    /**
     * Determine if title form field is valid(non-empty)
     * Sets the boolean value indicating validity once done
     * @param title String containing the form field text
     */
    public void validateTitle(String title){
        boolean isValid =  title != null && !title.isEmpty();
        setTitleValid(isValid);
    }

    /**
     * Determine if value in the initPrice field is valid,
     * calls validateInitPrice() to do so
     * Sets the boolean value indicating validity once done
     * @param value String containing the form field text
     */
    public void validateInitPrice(String value){
        boolean isValid = validateBidPrice(value);
        setInitPriceValid(isValid);
    }

    /**
     * Determine if value in the minBid field is valid,
     * calls validateInitPrice() to do so
     * Sets the boolean value indicating validity once done
     * @param value String containing the form field text
     */
    public void validateMinBid(String value){
        boolean isValid = validateBidPrice(value);
        setMinBidValid(isValid);
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
     * Determine if start date and time form field is valid,
     * calls validateDateAndTime() to do so
     * Sets the boolean value indicating validity once done
     * @param date String containing the date form fields text data (Format: DD/MM/YEAR HH:MM)
     */
    public void validateStartDateAndTime(String date){
        setStartDateAndTimeValid(validateDateAndTime(date));
    }

    /**
     * Determine if a date and time form field is valid
     * @param date String containing the date form fields text data (Format: DD/MM/YEAR HH:MM)
     * @return Boolean indicating if valid
     */
    private boolean validateDateAndTime(String date) {
        boolean isValid;
        Calendar prevDay;

        if(date != null && !date.isEmpty() && date.length() < MIN_DATE_LEN) {
            isValid = false;
        } else {
            prevDay = Calendar.getInstance(Locale.CANADA);
            prevDay.add(Calendar.DATE, -1); // All days valid from one day in the past
            Calendar cal = DateUtility.convertToDateObj(date);
            isValid = cal != null && cal.after(prevDay);
        }

        return isValid;
    }

    /**
     * Determine if the given start date and time occurs before the end date and time given
     * @param startDate String containing a given start date (Format: DD/MM/YEAR HH:MM)
     * @param endDate String containing a given end date (Format: DD/MM/YEAR HH:MM)
     * @return Boolean indicating if valid(end time occurs after start time)
     */
    public void validateEndDateAndTime(String startDate, String endDate) {
        boolean isValid = false;
        if (!validateDateAndTime(startDate)) {
            isValid = validateDateAndTime(endDate); //End date can still be valid if start date isn't
        } else {
            if (validateDateAndTime(endDate)) {
                // If startDate is valid, then End date must be valid and occur after start date
                Calendar startCal = DateUtility.convertToDateObj(startDate);
                Calendar endCal = DateUtility.convertToDateObj(endDate);
                isValid = startCal.before(endCal);
            }
        }
        setEndDateAndTimeValid(isValid);
    }

    /**
     * Determine if category form field is valid(non-empty)
     * Sets the boolean value indicating validity once done
     * @param category String containing the form field text
     */
    public void validateCategory (String category){
        boolean isValid = category != null && !category.isEmpty();
        setDescriptionValid(isValid);
    }

    /**
     * Determine if description form field is valid(non-empty)
     * Sets the boolean value indicating validity once done
     * @param description String containing the form field text
     */
    public void validateDescription (String description){
        boolean isValid = description != null && !description.isEmpty();
        setDescriptionValid(isValid);
    }
}
