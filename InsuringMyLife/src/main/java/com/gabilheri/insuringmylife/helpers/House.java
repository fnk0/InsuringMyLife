package com.gabilheri.insuringmylife.helpers;

/**
 * Created by marcus on 3/5/14.
 */
public class House {

    public static final String TAG_ADDRESS = "address";
    public static final String TAG_CITY = "city";
    public static final String TAG_STATE = "zip_code";
    public static final String TAG_ID = "id";
    public static final String TAG_POLICENUMBER = "police_number";
    public static final String TAG_PAYED = "payed";
    public static final String TAG_USERID = "user_id";

    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String id;
    private String policeNumber;
    private String payed;
    private String userId;

    public House() {


    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPoliceNumber(String policeNumber) {
        this.id = policeNumber;
    }

    public void setPayed(String payed) {
        this.payed = payed;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return  city;
    }

    public String getId() {
        return id;
    }

    public String getPoliceNumber() {
        return policeNumber;
    }

    public String getPayed() {
        return payed;
    }

    public String getUserId() {
        return userId;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }
}
