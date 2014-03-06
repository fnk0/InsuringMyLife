package com.gabilheri.insuringmylife.helpers;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marcus on 3/5/14.
 */
public class House implements  Parcelable {


    public static final String TAG_ADDRESS = "address";
    public static final String TAG_CITY = "city";
    public static final String TAG_STATE = "zip_code";
    public static final String TAG_ID = "id";
    public static final String TAG_POLICENUMBER = "police_number";
    public static final String TAG_PAYED = "payed";
    public static final String TAG_USERID = "user_id";

    private String address, city, state, zipCode, id, policeNumber, payed, userId;

    public House() {


    }

    public House(String address, String city, String state, String zipCode, String id, String policeNumber, String payed, String userId) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.id = id;
        this.policeNumber = policeNumber;
        this.payed = payed;
        this.userId = userId;
    }

    public House(Parcel in) {
        address = in.readString();
        city = in.readString();
        state = in.readString();
        zipCode = in.readString();
        id = in.readString();
        policeNumber = in.readString();
        payed = in.readString();
        userId = in.readString();
    }

    public static final Parcelable.Creator<House> CREATOR
            = new Parcelable.Creator<House>() {
        public House createFromParcel(Parcel in) {
            return new House(in);
        }

        public House[] newArray(int size) {
            return new House[size];
        }
    };


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
        this.policeNumber = policeNumber;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(getId());
        parcel.writeString(getZipCode());
        parcel.writeString(getUserId());
        parcel.writeString(getAddress());
        parcel.writeString(getCity());
        parcel.writeString(getPayed());
        parcel.writeString(getPoliceNumber());
        parcel.writeString(getState());


    }
}
