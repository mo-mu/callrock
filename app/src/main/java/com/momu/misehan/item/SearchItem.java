package com.momu.misehan.item;

/**
 * Created by songm on 2017-08-13.
 */

public class SearchItem {
    String address;
    double latitude, longitude;
    int id;

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public SearchItem(int id, String address, double longitude, double latitude){
        this.id = id;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
