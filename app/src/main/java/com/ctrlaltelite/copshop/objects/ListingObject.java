package com.ctrlaltelite.copshop.objects;

public class ListingObject {
    private String id; // Only ever set by the database. Is generated when saving a new account.
    private String title;
    private String description;
    private String initPrice;
    private String minBid; // Minimum amount by which a bid can increment
    private String auctionStartDate; // Format: DD/MM/YEAR Format: HR:MN (24 HR)
    private String auctionEndDate; // Format: DD/MM/YEAR HR:MN (24 HR)
    private String sellerId;

    public ListingObject(String id, String title, String description, String initPrice, String minBid, String auctionStartDate, String auctionEndDate, String sellerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.initPrice = initPrice;
        this.minBid = minBid;
        this.auctionStartDate = auctionStartDate;
        this.auctionEndDate = auctionEndDate;
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

    public String getAuctionEndDate() {
        return auctionEndDate;
    }

    public String getSellerId() {
        return sellerId;
    }

    // Implement once bids are implemented
    public String getLocation() {
        return "PLACEHOLDER";
    }
    public String getCurrentPrice() {
        return "00.00";
    }
    public String getNumBids() {
        return "0";
    }
    public String getTimeLeft() {
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
                    "\nauction end date: " + auctionEndDate +
                    "\nseller id: " + sellerId;

        return listing;
    }
}
