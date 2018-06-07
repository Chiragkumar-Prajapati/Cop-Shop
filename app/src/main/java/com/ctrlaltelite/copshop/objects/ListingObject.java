package com.ctrlaltelite.copshop.objects;

public class ListingObject {
    private String id; // Only ever set by the database. Is generated when saving a new account.
    private String title;
    private String description;
    private String initPrice;
    private String minBid; // Minimum amount by which a bid can increment
    private String auctionStartDate; // Format:
    private String auctionStartTime; // Format:
    private String auctionEndDate; // Format:
    private String auctionEndTime; //Format:

    public ListingObject(String id, String title, String description, String initPrice, String minBid, String auctionStartDate, String auctionStartTime, String auctionEndDate, String auctionEndTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.initPrice = initPrice;
        this.minBid = minBid;
        this.auctionStartDate = auctionStartDate;
        this.auctionStartTime = auctionStartTime;
        this.auctionEndDate = auctionEndDate;
        this.auctionEndTime = auctionEndTime;
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

}
