package com.gabilheri.insuringmylife.helpers;

/**
 * Created by marcus on 3/4/14.
 */
public class Vehicle {

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

    public Vehicle() {

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

    public String policeNumber() {
        return policeNumber;
    }

    public String getColor() {
        return color;
    }

    public String getLicensePlate() {
        return licensePlate;
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

    @Override
    public String toString() {
        return year + " " + brand + " " + model;
    }
}
