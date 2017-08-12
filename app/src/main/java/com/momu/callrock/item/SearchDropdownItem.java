package com.momu.callrock.item;

/**
 * Created by songm on 2017-07-15.
 */

public class SearchDropdownItem {
    String addr;
    int id;
    public String getAddr() {
        return addr;
    }


    public int getId() {
        return id;
    }

    public SearchDropdownItem(String a, int i){
        this.addr = a;
        this.id = i;
    }
}

