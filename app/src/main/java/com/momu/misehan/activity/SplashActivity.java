package com.momu.misehan.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.momu.misehan.R;
import com.momu.misehan.constant.CConstants;
import com.momu.misehan.dialog.PermissionDialog;
import com.momu.misehan.preference.AppPreference;
import com.momu.misehan.utility.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

/**
 * 스플레시 페이지
 * Created by songm on 2017-07-09.
 */
public class SplashActivity extends AppCompatActivity {

    private Context mContext;

    @BindView(R.id.txtTitle) TextView txtTitle;
    @BindView(R.id.animation_view) LottieAnimationView animationView;

    private FirebaseAnalytics mFirebaseAnalytics;

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mContext = this;

        Typeface typeFace1 = Typeface.createFromAsset(getAssets(), CConstants.FONT_NANUM_MYEONGJO);
        txtTitle.setTypeface(typeFace1);

        getObserve();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppPreference.loadIsFirst(mContext)) {  // 앱 설치 후 퍼미션 설정 페이지 봤을 때

                    openMain();
                } else { // 페이지 안봤을 때

                    PermissionDialog mDialog = new PermissionDialog(mContext);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setCancelable(false);
                    mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {

                        }
                    });
                    mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });
                    mDialog.show();

                }
            }
        }, 1500);
    }


    private void getObserve() {
        RequestQueue queue = Volley.newRequestQueue(this);

        final SharedPreferences preferences = getSharedPreferences("Pref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, CConstants.URL_STATION_INFO, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("list");
                    LogHelper.e(TAG, jsonArray.toString());
                    JSONArray result = new JSONArray();

                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject o = jsonArray.getJSONObject(i);

                            JSONObject curObs = new JSONObject();
                            curObs.put("addr", o.getString("addr"));
                            curObs.put("dmX", o.getDouble("dmX"));
                            curObs.put("dmY", o.getDouble("dmY"));
                            curObs.put("oper", o.getString("oper"));
                            curObs.put("stationName", o.getString("stationName"));

                            result.put(curObs);
                        }

                        editor.putString("Observe", result.toString()).apply();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.e(TAG, "ERROR : " + error.getMessage());
                if (AppPreference.loadIsFirst(mContext)) {  // 앱 설치 후 퍼미션 설정 페이지 봤을 때
                    openMain();

                } else { // 페이지 안봤을 때
                    PermissionDialog mDialog = new PermissionDialog(mContext);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setCancelable(false);
                    mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {

                        }
                    });
                    mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });
                    mDialog.show();
                }
            }
        });

        queue.add(jsonRequest);
    }

    /**
     * MainActivity 진입
     */
    public void openMain() {
        Bundle bundle = new Bundle();
        try {
            if (getIntent().getStringExtra("where") == null) {
                bundle.putString("gotoMain", "inSplash");
                LogHelper.e(TAG, "inSplash");
            } else {
                bundle.putString("gotoMain", "inWidget");
                LogHelper.e(TAG, "inWidget");
            }
            mFirebaseAnalytics.logEvent("gotoMain", bundle);
        } catch (Exception e) {
            LogHelper.e(TAG, e.toString());
        }
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        AppPreference.saveIsFirst(mContext, true);
        finish();
    }
}
