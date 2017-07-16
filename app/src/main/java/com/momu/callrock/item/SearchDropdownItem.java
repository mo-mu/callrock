package com.momu.callrock.item;

/**
 * Created by songm on 2017-07-15.
 */

public class SearchDropdownItem {
    String addr, stationName;

    public String getAddr() {
        return addr;
    }

    public String getStationName() {
        return stationName;
    }

    public SearchDropdownItem(String a, String s){
        this.addr = a;
        this.stationName = s;
    }
}

