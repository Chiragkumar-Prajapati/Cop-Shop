package com.ctrlaltelite.copshop.objects;

public class ListingObject {
    private String id; // Only ever set by the database. Is generated when saving a new account.
    private String title;
    private String description;

    public ListingObject(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
