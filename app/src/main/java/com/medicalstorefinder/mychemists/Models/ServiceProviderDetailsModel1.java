package com.medicalstorefinder.mychemists.Models;

import java.io.Serializable;


public class ServiceProviderDetailsModel1 implements Serializable {

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
    public String Itemname10;
    public String Cost;

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }


//............................................................................

    public int getId() {
        return Id;
    }

    public void setId(int ID) {
        this.Id = Id;
    }

}
