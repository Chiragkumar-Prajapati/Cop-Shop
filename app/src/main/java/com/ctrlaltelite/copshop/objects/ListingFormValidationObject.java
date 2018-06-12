package com.ctrlaltelite.copshop.objects;

/**
 * Data object that stores (Boolean) values for
 * each field in the CreateNewListing form indicating
 * whether or not data in it is valid
 */
public class ListingFormValidationObject {

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
        this.endDateAndTimeValid = true;
    }

    private Boolean titleValid;
    private Boolean descriptionValid;
    private Boolean initPriceValid;
    private Boolean minBidValid; // Minimum amount by which a bid can increment
    private Boolean startDateAndTimeValid; // Format - Date: DD/MM/YEAR Time: HR:MN (24 HR)
    private Boolean endDateAndTimeValid; // Format - Date: DD/MM/YEAR Time: HR:MN (24 HR)

    public Boolean getTitleValid() {
        return titleValid;
    }

    public void setTitleValid(Boolean titleValid) {
        this.titleValid = titleValid;
    }

    public Boolean getDescriptionValid() {
        return descriptionValid;
    }

    public void setDescriptionValid(Boolean descriptionValid) {
        this.descriptionValid = descriptionValid;
    }

    public Boolean getInitPriceValid() {
        return initPriceValid;
    }

    public void setInitPriceValid(Boolean initPriceValid) {
        this.initPriceValid = initPriceValid;
    }

    public Boolean getMinBidValid() {
        return minBidValid;
    }

    public void setMinBidValid(Boolean minBidValid) {
        this.minBidValid = minBidValid;
    }

    public Boolean getStartDateAndTimeValid() {
        return startDateAndTimeValid;
    }

    public void setStartDateAndTimeValid(Boolean auctionStartDateValid) {
        this.startDateAndTimeValid = auctionStartDateValid;
    }

    public Boolean getEndDateAndTimeValid() {
        return endDateAndTimeValid;
    }

    public void setEndDateAndTimeValid(Boolean auctionEndDateValid) {
        this.endDateAndTimeValid = auctionEndDateValid;
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
}
