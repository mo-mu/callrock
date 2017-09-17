package com.momu.callrock.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;

import com.momu.callrock.R;
import com.momu.callrock.activity.MainActivity;
import com.momu.callrock.activity.SplashActivity;
import com.momu.callrock.preference.AppPreference;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by songmho on 2017. 9. 17..
 */

public class PermissionDialog extends Dialog {
    Context mContext;

    public PermissionDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_permission);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_accept)
    void onAccepted(){
        ((SplashActivity)mContext).openMain();

    }

}
