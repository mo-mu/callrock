package com.momu.callrock.item;

/**
 * Created by songm on 2017-08-12.
 */

public class WholeCity {
    int id;
    String address;
    double longitude, latitude;

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public WholeCity(int id, String address, double longitude, double latitude){
        this.id = id;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
