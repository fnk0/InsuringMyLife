package com.gabilheri.insuringmylife.helpers;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marcus on 3/4/14.
 */
public class Vehicle implements Parcelable {

    private String id, brand, year, model, policeNumber, color, licensePlate, mainDriver, driverBirthday, driverLicense, licenseState, driverGender;
    public static final String TAG_ID = "id";
    public static final String TAG_USERID = "user_id";
    public static final String TAG_VEHICLES = "vehicles";
    public static final String TAG_YEAR = "year";
    public static final String TAG_MODEL = "model";
    public static final String TAG_BRAND = "brand";
    public static final String TAG_COLOR = "color";
    public static final String TAG_LICENSE = "license_plate";
    public static final String TAG_DRIVER = "main_driver";
    public static final String TAG_POLICENUMBER = "police_number";
    public static final String TAG_DRIVER_LICENSE = "drivers_license";
    public static final String TAG_LICENSE_STATE = "license_state";
    public static final String TAG_BIRTHDAY_YEAR = "driver_birthday_year";
    public static final String TAG_BIRTHDAY_MONTH = "driver_birthday_month";
    public static final String TAG_BIRTHDAY_DAY = "driver_birthday_day";
    public static final String TAG_DRIVER_GENDER = "driver_gender";

    // PHP script address

    public static final String VEHICLES_URL = "http://162.243.225.173/InsuringMyLife/view_vehicles.php";

    // JSON ID's
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_MESSAGE = "message";

    public Vehicle() {

    }

    public static final Parcelable.Creator<Vehicle> CREATOR
            = new Parcelable.Creator<Vehicle>() {
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

    public Vehicle(Parcel source) {

        // Reconstruct from the parcel
        id = source.readString();
        brand = source.readString();
        year = source.readString();
        model = source.readString();
        policeNumber = source.readString();
        color = source.readString();
        licensePlate = source.readString();
        mainDriver = source.readString();
        driverBirthday = source.readString();
        driverLicense = source.readString();
        licenseState = source.readString();
        driverGender = source.readString();

    }

    // Constructor for displaying vehicle information.
    public  Vehicle(String id, String brand, String year, String model) {
        this.id = id;
        this.brand = brand;
        this.year = year;
        this.model = model;
    }


    // Setters for the Vehicle class
    public void setId(String id) {
        this.id = id;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPoliceNumber(String policeNumber) {
        this.policeNumber = policeNumber;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setMainDriver(String mainDriver) {
        this.mainDriver = mainDriver;
    }

    public void setDriverBirthday(String driverBirthday) {
        this.driverBirthday = driverBirthday;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public void setLicenseState(String licenseState) {
        this.licenseState = licenseState;
    }

    public void setDriverGender(String driverGender) {
        this.driverGender = driverGender;
    }


    // GETTERS FOR THE VEHICLE CLASS

    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getPoliceNumber() {
        return policeNumber;
    }

    public String getMainDriver() {
        return mainDriver;
    }

    public String getDriverBirthday() {
        return driverBirthday;
    }

    public String getLicenseState() {
        return licenseState;
    }

    public String getDriverGender() {
        return driverGender;
    }

    public String getDriverLicense() {
        return driverLicense;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(getId());
        parcel.writeString(getBrand());
        parcel.writeString(getYear());
        parcel.writeString(getModel());
        parcel.writeString(getPoliceNumber());
        parcel.writeString(getColor());
        parcel.writeString(getLicensePlate());
        parcel.writeString(getMainDriver());
        parcel.writeString(getDriverBirthday());
        parcel.writeString(getDriverLicense());
        parcel.writeString(getLicenseState());
        parcel.writeString(getDriverGender());

    }


    @Override
    public String toString() {
        return year + " " + brand + " " + model;
    }
}
