package com.momu.callrock.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.momu.callrock.R;
import com.momu.callrock.config.CConfig;
import com.momu.callrock.constant.CConstants;
import com.momu.callrock.preference.AppPreference;
import com.momu.callrock.utility.LogHelper;
import com.momu.callrock.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 메인화면
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.navView) NavigationView navigationView;
    @BindView(R.id.txt_value_pm10_degree) TextView txtValuePM10Degree;
    @BindView(R.id.txt_value_pm10) TextView txtValuePM10;
    @BindView(R.id.txt_value_pm25) TextView txtValuePM25;
    @BindView(R.id.txt_value_pm25_degree) TextView txtValuePM25Degree;
    @BindView(R.id.txt_time_sync) TextView txtSyncTime;
    @BindView(R.id.txt_status_main) TextView txtGradeMain;
    @BindView(R.id.btn_search) TextView btnSearch;
    @BindView(R.id.img_cat) ImageView imgMain;
    @BindView(R.id.img_dot10) ImageView imgDot10;
    @BindView(R.id.img_dot25) ImageView imgDot25;
    @BindView(R.id.switch_who) SwitchCompat switchWho;     //WHO기준 여부 스위치
    @BindView(R.id.layout_table_who) LinearLayout layoutWho;
    @BindView(R.id.layout_table_korea) LinearLayout layoutKorea;

    Context mContext;

    private FusedLocationProviderClient mFusedLocationClient;

    double locationX, locationY;
    String nearestStationName = null;

    int pm10Grade, pm25Grade;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 11;  //위치정보 권한 확인

    JSONObject stationDetailObject = null;      //측정소에서 측정한 미세먼지 정보

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mContext = this;
        initDrawer();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocationData();
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

        if (AppPreference.loadIsWhoGrade(mContext)) {
            switchWho.setChecked(true);
            layoutKorea.setVisibility(View.GONE);
            layoutWho.setVisibility(View.VISIBLE);

        } else {
            switchWho.setChecked(false);
            layoutWho.setVisibility(View.GONE);
            layoutKorea.setVisibility(View.VISIBLE);
        }

        switchWho.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    LogHelper.e(TAG, "onCheckedChanged, true");
                    AppPreference.saveIsWhoGrade(mContext, true);
                    layoutKorea.setVisibility(View.GONE);
                    layoutWho.setVisibility(View.VISIBLE);

                } else {
                    LogHelper.e(TAG, "onCheckedChanged, false");
                    AppPreference.saveIsWhoGrade(mContext, false);
                    layoutWho.setVisibility(View.GONE);
                    layoutKorea.setVisibility(View.VISIBLE);
                }

                try {
                    setPMValueUI();
                } catch (Exception e) {
                    LogHelper.errorStackTrace(e);
                }
            }
        });
    }

    /**
     * 현재 위치정보를 받아옴 (권한요청도 함)
     */
    void getLocationData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LogHelper.e(TAG, "위치 권한 주어지지 않음");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS)) {

                LogHelper.e(TAG, "권한 확인 1");

            } else {
                LogHelper.e(TAG, "권한 확인 2");

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this
                        , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}
                        , MY_PERMISSIONS_REQUEST_LOCATION);
                return;
            }

        } else {    //위치 정보 권한 있음
            LogHelper.e(TAG, "위치 권한 주어짐");
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // ...
                                locationX = location.getLongitude();
                                locationY = location.getLatitude();
                                LogHelper.e(TAG, "위치정보 : " + locationX + ", " + locationY);
//                            locationX = 126.9004613;
//                            locationY = 37.5347978;

                                getStationList(locationX, locationY);
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            LogHelper.errorStackTrace(e);
                        }
                    });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    /**
     * 위치정보(longitude, latitude)를 이용하여 서버에서 측정소 정보를 가져온다.
     *
     * @param geoX 위치정보(x좌표, longitude)
     * @param geoY 위치정보(y좌표, latitude)
     */

    private void getStationList(final double geoX, final double geoY) {
        final RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, String.format(CConstants.URL_KAKAO_GEO_TRANSCOORD, String.valueOf(geoX), String.valueOf(geoY)), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("documents");
                    LogHelper.e(TAG, jsonArray.toString());
                    double tmX = jsonArray.getJSONObject(0).getDouble("x");
                    double tmY = jsonArray.getJSONObject(0).getDouble("y");

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, CConstants.URL_STATION_LIST_BY_GEO + "tmX=" + tmX + "&tmY=" + tmY, null, new Response.Listener<JSONObject>() {
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
                                        getStationDetail(nearestStationName);
                                        getAddressFromCoord(geoX, geoY);
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


                } catch (Exception e) {
                    LogHelper.errorStackTrace(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.e(TAG, "ERROR : " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "KakaoAK e8695fade4aae0be4d1609c102af6a8b");
                return params;
            }
        };
        queue.add(request);
    }

    /**
     * 측정소 이름으로 환경공단 공공 API에서 측정소에서 측정한 정보를 가져옴.
     *
     * @param stationName 측정소 이름
     */
    void getStationDetail(String stationName) {
        if (stationName != null && !stationName.equals("")) {
            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, CConstants.URL_STATION_DETAIL + stationName, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        final JSONArray jsonArray = response.getJSONArray("list");
                        LogHelper.e(TAG, jsonArray.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    LogHelper.e(TAG, jsonArray.getJSONObject(0).toString());
                                    stationDetailObject = jsonArray.getJSONObject(0);
                                    setPMValueUI();
                                } catch (JSONException | ParseException e) {
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
     * 카카오 REST_API에서 좌표정보를 지도정보로 변환 후 메인 상단에 보여줌.
     *
     * @param geoX longitude
     * @param geoY latitude
     */
    void getAddressFromCoord(double geoX, double geoY) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, String.format(CConstants.URL_KAKAO_GEO_COORD2ADDRESS, String.valueOf(geoX), String.valueOf(geoY)), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("documents");
                    LogHelper.e(TAG, jsonArray.toString());
                    JSONObject addressObject = jsonArray.getJSONObject(0).getJSONObject("address");
                    btnSearch.setText(Html.fromHtml("<font color=\"#323232\"><u>" + addressObject.getString("region_1depth_name") + " " + addressObject.getString("region_2depth_name") + "</font></u>"));

                } catch (Exception e) {
                    LogHelper.errorStackTrace(e);
                    btnSearch.setText(Html.fromHtml("<u>" + "현재 주소 알수없음" + "</u>"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.e(TAG, "ERROR : " + error.getMessage());
                btnSearch.setText(Html.fromHtml("<u>" + "현재 주소 알수없음" + "</u>"));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "KakaoAK e8695fade4aae0be4d1609c102af6a8b");
                return params;
            }
        };
        queue.add(request);
    }

    /**
     * 메인화면에 초미세먼지, 미세먼지 관련 UI 업데이트
     *
     * @throws JSONException
     * @throws ParseException
     */
    void setPMValueUI() throws JSONException, ParseException {
        if (stationDetailObject == null) return;

        String pm10Value = stationDetailObject.getString("pm10Value");
        String pm25Value = stationDetailObject.getString("pm25Value");

        if (pm25Value.equals("-"))
            pm25Value = "-1";

        if (pm10Value.equals("-"))
            pm10Value = "-1";

        pm10Grade = Utility.pm10Grade(Integer.parseInt(pm10Value), AppPreference.loadIsWhoGrade(mContext));
        pm25Grade = Utility.pm25Grade(Integer.parseInt(pm25Value), AppPreference.loadIsWhoGrade(mContext));

        if (pm10Grade == -1) {        //측정값 없을 경우
            txtValuePM10Degree.setText("");
            imgDot10.setVisibility(View.GONE);
        } else {
            txtValuePM10Degree.setText(pm10Value);
            imgDot10.setVisibility(View.VISIBLE);
        }

        txtValuePM10.setText(Utility.getGradeStr(pm10Grade));

        if (pm25Grade == -1) {         //측정값 없을 경우
            txtValuePM25Degree.setText("");
            imgDot25.setVisibility(View.GONE);
        } else {
            txtValuePM25Degree.setText(pm25Value);
            imgDot25.setVisibility(View.VISIBLE);
        }

        txtValuePM25.setText(Utility.getGradeStr(pm25Grade));


        String dataTime = stationDetailObject.getString("dataTime");

        final String OLD_FORMAT = "yyyy-MM-dd HH:mm";
        final String NEW_FORMAT = "HH:mm";

        // August 12, 2010
        String newDateString;

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = sdf.parse(dataTime);
        sdf.applyPattern(NEW_FORMAT);
        newDateString = sdf.format(d);
        txtSyncTime.setText(newDateString + " 기준 측정값");

        int mainGrade;  //메인페이지에 표시할 기준 등급, 높은 등급을 기준으로 보여준다.
        if (pm10Grade > pm25Grade) {
            mainGrade = pm10Grade;

        } else {
            mainGrade = pm25Grade;
        }

        txtGradeMain.setText(Utility.getGradeStr(mainGrade));

        switch (mainGrade) {
            case 0:
                imgMain.setImageResource(R.drawable.status_icon_1);
                break;
            case 1:
                imgMain.setImageResource(R.drawable.status_icon_2);
                break;
            case 2:
                imgMain.setImageResource(R.drawable.status_icon_3);
                break;
            case 3:
                imgMain.setImageResource(R.drawable.status_icon_4);
                break;
        }

    }

    /**
     * 검색 버튼 클릭 이벤트
     */
    @OnClick(R.id.btn_search)
    void btnSearchClick() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivityForResult(intent, CConfig.SEARCH_LOCATION);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    LogHelper.e(TAG, "권한 확인 3");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getLocationData();
                } else {
                    LogHelper.e(TAG, "권한 확인 4");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CConfig.SEARCH_LOCATION && resultCode == CConfig.SELECT_ITEM){
            getStationList(data.getDoubleExtra("x",-1),data.getDoubleExtra("y",-1));
        }
    }
}
