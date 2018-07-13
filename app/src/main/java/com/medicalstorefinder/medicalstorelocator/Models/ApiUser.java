package com.medicalstorefinder.medicalstorelocator.Models;

public class ApiUser {

    public int ID;
    public String RegMobile;
    public String Email;
    public String ProfilePicUrl;
    public String First_Name;
    public String Last_Name;
    public String Address;
    public String Passwords;

    public String Shop_Name;

    public ApiUser() {
        this.ID = -1;
        RegMobile = "";
        Email = "";
        ProfilePicUrl = "";
        First_Name="";
        Address="";
        Passwords="";
        Shop_Name="";
    }



    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Name(String larst_Name) {
        Last_Name = larst_Name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPasswords() {
        return Passwords;
    }

    public void setPasswords(String passwords) {
        Passwords = passwords;
    }

    public String getShop_Name() {
        return Shop_Name;
    }

    public void setShop_Name(String shop_Name) {
        Shop_Name = shop_Name;
    }

    public String getRegMobile() {
        return RegMobile;
    }

    public void setRegMobile(String regMobile) {
        RegMobile = regMobile;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

}
