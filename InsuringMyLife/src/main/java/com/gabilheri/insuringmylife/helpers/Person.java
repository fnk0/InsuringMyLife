package com.gabilheri.insuringmylife.helpers;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marcus on 3/5/14.
 */
public class Person implements Parcelable {

    public static final String TAG_ID = "id";
    public static final String TAG_PERSONS = "person";
    public static final String TAG_POLICENUMBER = "police_number";
    public static final String TAG_USERID = "user_id";
    public static final String TAG_NAME = "name";
    public static final String TAG_LAST_NAME = "last_name";
    public static final String TAG_BIRTHDAY_YEAR = "birthday_year";
    public static final String TAG_BIRTHDAY_MONTH = "birthday_month";
    public static final String TAG_BIRTHDAY_DAY = "birthday_day";
    public static final String TAG_AGE = "person_age";
    public static final String TAG_RELATIONSHIP = "relationship";
    public static final String TAG_MIDDLE_I = "middle_initial";
    public static final String TAG_INSURANCE = "has_insurance";
    public static final String NEW_PERSON_URL = "http://162.243.225.173/InsuringMyLife/new_person.php";
    public static final String VIEW_PERSON_URL = "http://162.243.225.173/InsuringMyLife/view_person.php";

    private String id;
    private String policeNumber;
    private String userId;
    private String name;
    private String lastName;
    private String birthDay;
    private String age;
    private String hasInsurance;
    private String relationship;
    private String middleI;

    public Person() {

    }

    public static final Parcelable.Creator<Person> CREATOR
            = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public Person(String id, String policeNumber, String userId, String name, String lastName, String birthDay, String age, String hasInsurance) {
        this.id = id;
        this.policeNumber = policeNumber;
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.age = age;
        this.hasInsurance = hasInsurance;
    }

    @Override
    public String toString() {
        return name + " " + lastName;
    }

    public Person(Parcel in) {

        id = in.readString();
        userId = in.readString();
        policeNumber = in.readString();
        name = in.readString();
        middleI = in.readString();
        lastName = in.readString();
        birthDay = in.readString();
        age = in.readString();
        relationship = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoliceNumber() {
        return policeNumber;
    }

    public void setPoliceNumber(String policeNumber) {
        this.policeNumber = policeNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHasInsurance() {
        return hasInsurance;
    }

    public void setHasInsurance(String hasInsurance) {
        this.hasInsurance = hasInsurance;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getMiddleI() {
        return middleI;
    }

    public void setMiddleI(String middleI) {
        this.middleI = middleI;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(getId());
        parcel.writeString(getUserId());
        parcel.writeString(getPoliceNumber());
        parcel.writeString(getName());
        parcel.writeString(getMiddleI());
        parcel.writeString(getLastName());
        parcel.writeString(getBirthDay());
        parcel.writeString(getAge());
        parcel.writeString(getRelationship());

    }
}
