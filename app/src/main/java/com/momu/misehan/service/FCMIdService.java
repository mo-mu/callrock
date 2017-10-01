package com.momu.misehan.service;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.momu.misehan.utility.LogHelper;

/**
 * Created by songm on 2017-09-30.
 */

public class FCMIdService extends FirebaseInstanceIdService  {

    private static final String TAG = "FCMIdService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();


        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        LogHelper.e(TAG, refreshToken);
    }
}
