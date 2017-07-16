package com.momu.callrock.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.momu.callrock.R;
import com.momu.callrock.constant.CConstants;
import com.momu.callrock.utility.GeoPoint;
import com.momu.callrock.utility.LogHelper;
import com.momu.callrock.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.navView) NavigationView navigationView;
    @BindView(R.id.txt_main) TextView txtMain;

    Context mContext;
    double locationX, locationY;
    String nearestStationName = null;


    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mContext = this;
        initDrawer();
    }

    /**
     * Navigation drawer 초기화
     */
    void initDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    /**
     * 위치정보를 받아옴
     */
    void getLocationData() {
        locationX = 126.9004613;
        locationY = 37.5347978;
        GeoPoint geoPoint = Utility.convertToTM(locationX, locationY);
        getStationList(geoPoint);
    }


    @Override
    public void onResume() {
        super.onResume();
        getLocationData();
    }


    private void getStationList(GeoPoint geoPoint) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, CConstants.URL_STATION_LIST_BY_GEO + "tmX=" + geoPoint.getX() + "&tmY=" + geoPoint.getY(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("list");
                    LogHelper.e(TAG, jsonArray.toString());

                    nearestStationName = jsonArray.getJSONObject(0).getString("stationName");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "가장 가까운 측정소는 " + nearestStationName + " 측정소 입니다.", Toast.LENGTH_LONG).show();
                            getStationDetail();
                        }
                    });

                } catch (JSONException e) {
                    LogHelper.errorStackTrace(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.e(TAG, "ERROR : " + error.getMessage());
            }
        });

        queue.add(jsonRequest);
    }

    void getStationDetail() {
        if (nearestStationName != null) {
            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, CConstants.URL_STATION_DETAIL + nearestStationName, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        final JSONArray jsonArray = response.getJSONArray("list");
                        LogHelper.e(TAG, jsonArray.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    txtMain.setText(jsonArray.getJSONObject(0).toString());
                                    Toast.makeText(mContext, "가장 가까운 측정소는 " + nearestStationName + " 측정소 입니다.", Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    LogHelper.errorStackTrace(e);
                                }
                            }
                        });

                    } catch (JSONException e) {
                        LogHelper.errorStackTrace(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LogHelper.e(TAG, "ERROR : " + error.getMessage());
                }
            });

            queue.add(jsonRequest);
        }
    }

    /**
     * 검색 버튼 클릭 이벤트
     */
    @OnClick(R.id.btn_search)
    void btnSearchClick() {
        startActivity(new Intent(MainActivity.this, SearchActivity.class));
    }

    /**
     * 새로고침 버튼 클릭 이벤트
     */
    @OnClick(R.id.btn_refresh)
    void btnRefreshClick() {
        getLocationData();
    }

    /**
     * 설정 버튼 클릭 이벤트
     */
    @OnClick(R.id.btn_setting)
    void btnSettingClick() {
        drawerLayout.openDrawer(navigationView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawer(navigationView);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
