package com.medicalstorefinder.mychemists.Models;

public class PostOrders {

    public String PicUrl;
    public String Description;
    public String Latitude;
    public String Longitude;
    public String Address;

    public PostOrders() {
        PicUrl = "";
        Description = "";
        Address = "";
        Latitude = "";
        Longitude = "";
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}