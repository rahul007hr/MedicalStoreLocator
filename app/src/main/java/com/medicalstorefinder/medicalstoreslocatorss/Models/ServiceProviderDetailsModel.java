package com.medicalstorefinder.medicalstoreslocatorss.Models;

import java.io.Serializable;


public class ServiceProviderDetailsModel implements Serializable {
    public int   ID;
    public String EmailId;
    public String  CustomerNo;
    public String FullName;
    public String  ServiceTypeName;
    public String Location;
    public String Status;
    public String Name,Password,DeliveryDate;
    public int  Id;
    public String Itemname1;
    public String Itemname2;
    public String Itemname3;
    public String Itemname4;
    public String Itemname5;
    public String Itemname6;
    public String Itemname7;
    public String Itemname8;
    public String Itemname9;
    public String Itemname10,image_link;

    public String orderid;
    public String orderMainId;
    public String description;
    public String imagepath;
    public String address;
    public String latitude;
    public String longitude;
    public String mobile;
    public String createddate;
    public String orderstatus;

    public String medicalId;
    public String medicalCost;
    public String medicalReply;
    public String medicalProfileUrl;
    public String customerId;
    public String customerType;
    public String isDelivered;
    public String notification;
    public String notificationTime,km;
    public Float rating;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCreateddate() {
        return createddate;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getCustomerNo() {
        return CustomerNo;
    }

    public void setCustomerNo(String customerNo) {
        CustomerNo = customerNo;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getServiceTypeName() {
        return ServiceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        ServiceTypeName = serviceTypeName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    //............................................................................

    public int getId() {
        return Id;
    }

    public void setId(int ID) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }

    public String getMedicalCost() {
        return medicalCost;
    }

    public void setMedicalCost(String medicalCost) {
        this.medicalCost = medicalCost;
    }

    public String getMedicalReply() {
        return medicalReply;
    }

    public void setMedicalReply(String medicalReply) {
        this.medicalReply = medicalReply;
    }

    public String getMedicalProfileUrl() {
        return medicalProfileUrl;
    }

    public void setMedicalProfileUrl(String medicalProfileUrl) {
        this.medicalProfileUrl = medicalProfileUrl;
    }


    public String getOrderMainId() {
        return orderMainId;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(String isDelivered) {
        this.isDelivered = isDelivered;
    }

    public void setOrderMainId(String orderMainId) {
        this.orderMainId = orderMainId;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }

    @Override
    public String toString() {
        return "ServiceProviderDetailsModel{" +
                "ID=" + ID +
                ", Retailer='" + FullName + '\'' +
                ", EmailId='" + EmailId + '\'' +
                ", Mobile_Number='" + CustomerNo + '\'' +
                ", FullName='" + FullName + '\'' +
                ", Status=" + Status +
                ", Order_Date='" + ServiceTypeName + '\'' +
                ", Location='" + Location + '\'' +
                ", Status='" + Status + '\'' +
                '}';
    }
}
