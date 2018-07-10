package com.medicalstorefinder.medicalstorelocator.Models;

public class ApiUser {

    public int ID;
    public String RegMobile,Permission;
    public String Email;
    public String ProfilePicUrl;
    public String Full_Name;
    public String Location;
    public String Passwords;
    public String cType;
    public String Type_of_Service;
    public String cStatus;
    public String Activated;
    public String Adhar_Card_Number,Created_Date,Activated_Date;

    public ApiUser() {
        this.ID = -1;
        RegMobile = "";
        Email = "";
        ProfilePicUrl = "";
        Full_Name="";
        Location="";
        Passwords="";
        cType="";
        Type_of_Service="";
        cStatus="";
        Activated="";
        Adhar_Card_Number="";

    }

    public String getPermission() {
        return Permission;
    }

    public void setPermission(String permission) {
        Permission = permission;
    }

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

    public String getPasswords() {
        return Passwords;
    }

    public void setPasswords(String passwords) {
        Passwords = passwords;
    }

    public String getcType() {
        return cType;
    }

    public void setcType(String ccType) {
        cType = ccType;
    }

    public String getType_of_Service() {
        return Type_of_Service;
    }

    public void setType_of_Service(String type_of_Service) {
        Type_of_Service = type_of_Service;
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
