package com.ctrlaltelite.copshop.objects;

public class ListingObject {
    private String id; // Only ever set by the database. Is generated when saving a new account.
    private String title;
    private String description;
    private String price;
    private String location;
    private String numBids;
    private String timeLeft;

    public ListingObject(String id, String title, String description, String location, String price, String numBids, String timeLeft) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.location = location;
        this.numBids = numBids;
        this.timeLeft = timeLeft;
    }

    public String getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public String getTitle() {
        return title;
    }
    public String getTimeLeft() {
        return timeLeft;
    }
    public String getLocation() {
        return location;
    }
    public String getNumBids() {
        return numBids;
    }
    public String getPrice() {
        return price;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setNumBids(String numBids) {
        this.numBids = numBids;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }
}
