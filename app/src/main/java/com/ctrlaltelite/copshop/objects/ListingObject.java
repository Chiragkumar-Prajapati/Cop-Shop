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
    private String category;

    public ListingObject(String id, String title, String description, String initPrice, String minBid, String auctionStartDate, String auctionEndDate, String category, String sellerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.initPrice = initPrice;
        this.minBid = minBid;
        this.auctionStartDate = auctionStartDate;
        this.auctionEndDate = auctionEndDate;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public String getAuctionEndDate() {
        return auctionEndDate;
    }

    public String getSellerId() {
        return sellerId;
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

    public void setCategory(String category) {
        this.category = category;
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
                    "\ncategory: " + category +
                    "\nseller id: " + sellerId;

        return listing;
    }
}
