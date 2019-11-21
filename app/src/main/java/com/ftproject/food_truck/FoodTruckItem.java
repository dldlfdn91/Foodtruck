package com.ftproject.food_truck;

import android.media.Image;

/**
 * Created by JeongHwan on 2017-05-13.
 */

public class FoodTruckItem {
    private String mId;
    private String mLat;
    private String mLon;
    private String mHp;
    private Image truck;
    private FoodTruckMenu menu;

    public void setmId(String mId) {
        this.mId = mId;
    }

    public void setmLat(String mLat) {
        this.mLat = mLat;
    }

    public void setmLon(String mLon) {
        this.mLon = mLon;
    }

    public String getmId() {
        return mId;
    }

    public String getmLat() {
        return mLat;
    }

    public String getmLon() {
        return mLon;
    }
}
