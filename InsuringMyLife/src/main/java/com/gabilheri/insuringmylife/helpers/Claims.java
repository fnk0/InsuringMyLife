package com.gabilheri.insuringmylife.helpers;

/**
 * Created by marcus on 3/6/14.
 */

public class Claims {

    public static final String TAG_ID = "id";
    public static final String TAG_USERID = "user_id";
    public static final String TAG_VEHICLES = "claim_vehicle";
    public static final String TAG_CLAIM_NUMBER = "claim_number";
    public static final String TAG_CLAIM_YEAR = "claim_year";
    public static final String TAG_CLAIM_MONTH = "claim_month";
    public static final String TAG_CLAIM_DAY = "claim_day";
    public static final String TAG_CLAIM_HOUR = "claim_hour";
    public static final String TAG_CLAIM_MINUTE = "claim_minute";
    public static final String TAG_CAUSE = "claim_cause";

    private String id, userId, claimNumber, claimDate, claimTime, claimCause;

    public Claims() {

    }

    public Claims(String id, String userId, String claimNumber, String claimDate, String claimTime, String claimCause) {
        this.id = id;
        this.userId = userId;
        this.claimNumber = claimNumber;
        this.claimDate = claimDate;
        this.claimTime = claimTime;
        this.claimCause = claimCause;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    public String getClaimTime() {
        return claimTime;
    }

    public void setClaimTime(String claimTime) {
        this.claimTime = claimTime;
    }

    public String getClaimCause() {
        return claimCause;
    }

    public void setClaimCause(String claimCause) {
        this.claimCause = claimCause;
    }
}
