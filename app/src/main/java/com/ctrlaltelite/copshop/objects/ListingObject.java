package com.ctrlaltelite.copshop.objects;

public class ListingObject {
    private String id; // Only ever set by the database. Is generated when saving a new account.
    private String title;
    private String description;
    private String initPrice;
    private String minBid; // Minimum amount by which a bid can increment
    private String auctionStartDate; // Format: DD/MM/YEAR
    private String auctionStartTime; // Format: HR:MN (24 HR)
    private String auctionEndDate; // Format: DD/MM/YEAR
    private String auctionEndTime; //Format: HR:MN (24 HR)
    private String sellerId;

    public ListingObject(String id, String title, String description, String initPrice, String minBid, String auctionStartDate, String auctionStartTime, String auctionEndDate, String auctionEndTime, String sellerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.initPrice = initPrice;
        this.minBid = minBid;
        this.auctionStartDate = auctionStartDate;
        this.auctionStartTime = auctionStartTime;
        this.auctionEndDate = auctionEndDate;
        this.auctionEndTime = auctionEndTime;
        this.sellerId = sellerId;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getInitPrice() {
        return initPrice;
    }

    public String getMinBid() {
        return minBid;
    }

    public String getAuctionStartDate() {
        return auctionStartDate;
    }

    public String getAuctionStartTime() {
        return auctionStartTime;
    }

    public String getAuctionEndDate() {
        return auctionEndDate;
    }

    public String getAuctionEndTime() {
        return auctionEndTime;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getLocation() {
        // TODO: Get listing address from seller
        return "PLACEHOLDER";
    }

    public String getCurrentPrice() {
        // TODO: Get current price from highest bid
        return this.initPrice;
    }

    public String getNumBids() {
        // TODO: Get num bids from all bids on listing
        return "0";
    }

    public String getTimeLeft() {
        // TODO: Get the amount of time left on the listing auction
        return "0 days";
    }

    // Setters
    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInitPrice(String initPrice) {
        this.initPrice = initPrice;
    }

    public void setMinBid(String minBid) {
        this.minBid = minBid;
    }

    public void setAuctionStartDate(String auctionStartDate) {
        this.auctionStartDate = auctionStartDate;
    }

    public void setAuctionEndDate(String auctionEndDate) {
        this.auctionEndDate = auctionEndDate;
    }

    public void setAuctionStartTime(String auctionStartTime) {
        this.auctionStartTime = auctionStartTime;
    }

    public void setAuctionEndTime(String auctionEndTime) {
        this.auctionEndTime = auctionEndTime;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public String toString(){
        String listing = "";

        listing  +=  "\nid: " + id +
                    "\ntitle: " + title +
                    "\ndescription: " + description +
                    "\ninitial price: " + initPrice +
                    "\nminimum bid: " + minBid +
                    "\nauction start date: " + auctionStartDate +
                    "\nauction start time: " + auctionStartTime +
                    "\nauction end date: " + auctionEndDate +
                    "\nauction end time: " + auctionEndTime +
                    "\nseller id: " + sellerId;

        return listing;
    }
}
