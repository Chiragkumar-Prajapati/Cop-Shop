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
        this.auctionStartDateValid = true;
        this.auctionStartTimeValid = true;
        this.auctionEndDateValid = true;
        this.auctionEndTimeValid = true;
    }

    private Boolean titleValid;
    private Boolean descriptionValid;
    private Boolean initPriceValid;
    private Boolean minBidValid; // Minimum amount by which a bid can increment
    private Boolean auctionStartDateValid; // Format: DD/MM/YEAR
    private Boolean auctionStartTimeValid; // Format: HR:MN (24 HR)
    private Boolean auctionEndDateValid; // Format: DD/MM/YEAR
    private Boolean auctionEndTimeValid; //Format: HR:MN (24 HR)

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

    public Boolean getAuctionStartDateValid() {
        return auctionStartDateValid;
    }

    public void setAuctionStartDateValid(Boolean auctionStartDateValid) {
        this.auctionStartDateValid = auctionStartDateValid;
    }

    public Boolean getAuctionStartTimeValid() {
        return auctionStartTimeValid;
    }

    public void setAuctionStartTimeValid(Boolean auctionStartTimeValid) {
        this.auctionStartTimeValid = auctionStartTimeValid;
    }

    public Boolean getAuctionEndDateValid() {
        return auctionEndDateValid;
    }

    public void setAuctionEndDateValid(Boolean auctionEndDateValid) {
        this.auctionEndDateValid = auctionEndDateValid;
    }

    public Boolean getAuctionEndTimeValid() {
        return auctionEndTimeValid;
    }

    public void setAuctionEndTimeValid(Boolean auctionEndTimeValid) {
        this.auctionEndTimeValid = auctionEndTimeValid;
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
                auctionStartDateValid &&
                auctionStartTimeValid &&
                auctionEndDateValid &&
                auctionEndTimeValid);
    }
}
