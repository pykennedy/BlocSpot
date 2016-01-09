package com.example.peter.blocspot.api.model;

public class PoiItem {
    private String titleID;
    private String name;
    private String category;
    private String notes;
    private long id = -1;
    private double longitude;
    private double latitude;
    private boolean viewed;

    public PoiItem(String titleID, String name, String category, String notes,
                   long id, double longitude, double latitude, boolean viewed) {
        this.titleID = titleID;
        this.name = name;
        this.category = category;
        this.notes = notes;
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.viewed = viewed;
    }

    public String getTitleID() {
        return titleID;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getNotes() {
        return notes;
    }

    public long getId() {
        return id;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setTitleID(String titleID) {
        this.titleID = titleID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setViewed (boolean isViewed) {
        this.viewed = isViewed;
    }
}
