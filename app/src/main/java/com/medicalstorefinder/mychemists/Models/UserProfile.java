package com.medicalstorefinder.mychemists.Models;

public class UserProfile {

    public String RegMobile;
    public String Email;
    public String ProfilePicUrl;
    public String Full_Name, FirstName, LastName, ShopName;
    public String Location;
    public String cType;
    public String cStatus;
    public String Activated;
    public String Adhar_Card_Number, Created_Date, Activated_Date;

    public String getFull_Name() {
        return Full_Name;
    }

    public void setFull_Name(String full_Name) {
        Full_Name = full_Name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getcType() {
        return cType;
    }

    public void setcType(String ccType) {
        cType = ccType;
    }

    public String getcStatus() {
        return cStatus;
    }

    public void setcStatus(String ccStatus) {
        cStatus = ccStatus;
    }

    public String getActivated() {
        return Activated;
    }

    public void setActivated(String activated) {
        Activated = activated;
    }

    public String getAdhar_Card_Number() {
        return Adhar_Card_Number;
    }

    public void setAdhar_Card_Number(String adhar_Card_Number) {
        Adhar_Card_Number = adhar_Card_Number;
    }

    public String getCreated_Date() {
        return Created_Date;
    }

    public void setCreated_Date(String created_Date) {
        Created_Date = created_Date;
    }

    public String getActivated_Date() {
        return Activated_Date;
    }

    public void setActivated_Date(String activated_Date) {
        Activated_Date = activated_Date;
    }

    public String getRegMobile() {
        return RegMobile;
    }

    public void setRegMobile(String regMobile) {
        RegMobile = regMobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getProfilePicUrl() {
        return ProfilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        ProfilePicUrl = profilePicUrl;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }
}