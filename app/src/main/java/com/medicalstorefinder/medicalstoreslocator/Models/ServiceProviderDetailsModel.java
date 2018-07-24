package com.medicalstorefinder.medicalstoreslocator.Models;

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

    public String getItemname1() {
        return Itemname1;
    }

    public void setItemname1(String itemname1) {
        Itemname1 = itemname1;
    }

    public String getItemname2() {
        return Itemname2;
    }

    public void setItemname2(String itemname2) {
        Itemname2 = itemname2;
    }

    public String getItemname3() {
        return Itemname3;
    }

    public void setItemname3(String itemname3) {
        Itemname3 = itemname3;
    }

    public String getItemname4() {
        return Itemname4;
    }

    public void setItemname4(String itemname4) {
        Itemname4 = itemname4;
    }

    public String getItemname5() {
        return Itemname5;
    }

    public void setItemname5(String itemname5) {
        Itemname5 = itemname5;
    }

    public String getItemname6() {
        return Itemname6;
    }

    public void setItemname6(String itemname6) {
        Itemname6 = itemname6;
    }

    public String getItemname7() {
        return Itemname7;
    }

    public void setItemname7(String itemname7) {
        Itemname7 = itemname7;
    }

    public String getItemname8() {
        return Itemname8;
    }

    public void setItemname8(String itemname8) {
        Itemname8 = itemname8;
    }

    public String getItemname9() {
        return Itemname9;
    }

    public void setItemname9(String itemname9) {
        Itemname9 = itemname9;
    }

    public String getItemname10() {
        return Itemname10;
    }

    public void setItemname10(String itemname10) {
        Itemname10 = itemname10;
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
