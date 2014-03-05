package com.gabilheri.insuringmylife.helpers;

/**
 * Created by marcus on 3/4/14.
 */
public class Vehicle {

    private String id, brand, year, model;


    public  Vehicle(String id, String brand, String year, String model) {

        this.id = id;
        this.brand = brand;
        this.year = year;
        this.model = model;
    }

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

    @Override
    public String toString() {
        return year + " " + brand + " " + model;
    }
}
