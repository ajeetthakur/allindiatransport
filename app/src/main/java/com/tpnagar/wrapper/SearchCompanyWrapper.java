package com.tpnagar.wrapper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by premprajapat on 12/27/16.
 */

public class SearchCompanyWrapper implements Parcelable {

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(String company_Id) {
        Company_Id = company_Id;
    }

    public String getService_Id() {
        return Service_Id;
    }

    public void setService_Id(String service_Id) {
        Service_Id = service_Id;
    }

    public String getCompany_Name() {
        return Company_Name;
    }

    public void setCompany_Name(String company_Name) {
        Company_Name = company_Name;
    }

    public String getCompany_Desc() {
        return Company_Desc;
    }

    public void setCompany_Desc(String company_Desc) {
        Company_Desc = company_Desc;
    }

    public String getParent_Id() {
        return Parent_Id;
    }

    public void setParent_Id(String parent_Id) {
        Parent_Id = parent_Id;
    }

    public String getOwner_Name() {
        return Owner_Name;
    }

    public void setOwner_Name(String owner_Name) {
        Owner_Name = owner_Name;
    }

    public String getContact_Person() {
        return Contact_Person;
    }

    public void setContact_Person(String contact_Person) {
        Contact_Person = contact_Person;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCountry_Id() {
        return Country_Id;
    }

    public void setCountry_Id(String country_Id) {
        Country_Id = country_Id;
    }

    public String getState_Id() {
        return State_Id;
    }

    public void setState_Id(String state_Id) {
        State_Id = state_Id;
    }

    public String getState_Name() {
        return State_Name;
    }

    public void setState_Name(String state_Name) {
        State_Name = state_Name;
    }

    public String getCity_Id() {
        return City_Id;
    }

    public void setCity_Id(String city_Id) {
        City_Id = city_Id;
    }

    public String getCity_Name() {
        return City_Name;
    }

    public void setCity_Name(String city_Name) {
        City_Name = city_Name;
    }

    public String getArea_Name() {
        return Area_Name;
    }

    public void setArea_Name(String area_Name) {
        Area_Name = area_Name;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getPin_Code() {
        return Pin_Code;
    }

    public void setPin_Code(String pin_Code) {
        Pin_Code = pin_Code;
    }

    public String getTPNagar_Trusted() {
        return TPNagar_Trusted;
    }

    public void setTPNagar_Trusted(String TPNagar_Trusted) {
        this.TPNagar_Trusted = TPNagar_Trusted;
    }

    public String getService_Name() {
        return Service_Name;
    }

    public void setService_Name(String service_Name) {
        Service_Name = service_Name;
    }

    public String getPrimary_Phone() {
        return Primary_Phone;
    }

    public void setPrimary_Phone(String primary_Phone) {
        Primary_Phone = primary_Phone;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getCompany_Type_Id() {
        return Company_Type_Id;
    }

    public void setCompany_Type_Id(String company_Type_Id) {
        Company_Type_Id = company_Type_Id;
    }

    public String getCurrent_Status_Id() {
        return Current_Status_Id;
    }

    public void setCurrent_Status_Id(String current_Status_Id) {
        Current_Status_Id = current_Status_Id;
    }

    public String getStorage_Id() {
        return Storage_Id;
    }

    public void setStorage_Id(String storage_Id) {
        Storage_Id = storage_Id;
    }

    public String getDestination_Cities() {
        return Destination_Cities;
    }

    public void setDestination_Cities(String destination_Cities) {
        Destination_Cities = destination_Cities;
    }

    public String getServiceKeywords() {
        return ServiceKeywords;
    }

    public void setServiceKeywords(String serviceKeywords) {
        ServiceKeywords = serviceKeywords;
    }

    public String getDestination_States() {
        return Destination_States;
    }

    public void setDestination_States(String destination_States) {
        Destination_States = destination_States;
    }

    String _id="";
    String Company_Id="";
    String Service_Id="";
    String Company_Name="";
    String Company_Desc="";
    String Parent_Id="";
    String Owner_Name="";
    String Contact_Person="";
    String Address="";
    String Country_Id="";
    String State_Id="";
    String State_Name="";
    String City_Id="";
    String City_Name="";
    String Area_Name="";
    String Website="";
    String Pin_Code="";
    String TPNagar_Trusted="";
    String Service_Name="";
    String Primary_Phone="";
    String Rating="";
    String Company_Type_Id="";
    String Current_Status_Id="";
    String Storage_Id="";
    String Destination_Cities="";
    String ServiceKeywords="";
    String Destination_States="";

    public boolean isIsmyVender() {
        return ismyVender;
    }

    public void setIsmyVender(boolean ismyVender) {
        this.ismyVender = ismyVender;
    }

    boolean ismyVender=false;

    public String getService_Type() {
        return Service_Type;
    }

    public void setService_Type(String service_Type) {
        Service_Type = service_Type;
    }

    String Service_Type="";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
