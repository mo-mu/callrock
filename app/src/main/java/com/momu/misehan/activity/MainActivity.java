package com.momu.misehan.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.momu.misehan.BuildConfig;
import com.momu.misehan.service.FCMIdService;
import com.momu.misehan.R;
import com.momu.misehan.constant.CConstants;
import com.momu.misehan.dialog.DenyPmsDialog;
import com.momu.misehan.dialog.SearchDialog;
import com.momu.misehan.preference.AppPreference;
import com.momu.misehan.utility.LogHelper;
import com.momu.misehan.utility.Utility;
import com.momu.misehan.widget.WidgetProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;


/**
 * 메인화면
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.navView) NavigationView navigationView;
    @BindView(R.id.txt_value_pm10_degree) TextView txtValuePM10Degree;
    @BindView(R.id.txt_value_pm10) TextView txtValuePM10;
    @BindView(R.id.txt_value_pm25) TextView txtValuePM25;
    @BindView(R.id.txt_value_pm25_degree) TextView txtValuePM25Degree;
    @BindView(R.id.txt_time_sync) TextView txtSyncTime;
    @BindView(R.id.txt_status_main) TextView txtGradeMain;
    @BindView(R.id.btn_refresh) TextView btnRefresh;
    @BindView(R.id.img_cat) LottieAnimationView imgMain;
    @BindView(R.id.img_dot10) ImageView imgDot10;
    @BindView(R.id.img_dot25) ImageView imgDot25;
    @BindView(R.id.switch_who) SwitchCompat switchWho;     //WHO기준 여부 스위치
    @BindView(R.id.layout_table_who) LinearLayout layoutWho;
    @BindView(R.id.layout_table_korea) LinearLayout layoutKorea;
    @BindView(R.id.txt_recommend) TextView txtRecommend;
    @BindView(R.id.txt_mask) TextView txtMask;
    @BindView(R.id.txt_activity) TextView txtActivity;
    @BindView(R.id.txt_life) TextView txtLife;
    @BindView(R.id.txt_window) TextView txtWindow;
    @BindView(R.id.img_refresh) ImageView refreshImg;
    @BindView(R.id.view_refresh) LinearLayout refreshView;
    @BindView(R.id.txt_drawer_version) TextView txtVersion;

    private FirebaseAnalytics mFirebaseAnalytics;

    Context mContext;

    private FusedLocationProviderClient mFusedLocationClient;

    double locationX, locationY;
    String nearestStationName = null;

    int pm10Grade, pm25Grade;

    LocationManager locationManager;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    private static final int RESULT_PERMISSIONS_LOCATION = 11;      //위치정보 권한 확인
    private static final int RESULT_LOCATION_SETTINGS = 12;     //위치정보 설정 확인

//    JSONObject stationDetailObject = null;      //측정소에서 측정한 미세먼지 정보

//    public boolean isSearch = false;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fabric.with(this, new Crashlytics());
        ButterKnife.bind(this);

        mContext = this;
        initView();
        initDrawer();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        if (AppPreference.loadIsUseSearchedStation(mContext) && !AppPreference.loadLastMeasureStation(mContext).equals("")) {       //검색한 위치 이용하는 경우
            if (Utility.shouldRefreshDetail(mContext)) {         //최근 갱신한 시간을 불러와서 갱신할지 여부 확인
                LogHelper.e(TAG, "측정할 시간이 되어 서버에서 갱신, 최근 측정 시간 : " + AppPreference.loadLastMeasureTime(mContext));

                try {
                    JSONObject jsonObject = new JSONObject(AppPreference.loadLastMeasureStation(mContext));
                    getStationDetail(jsonObject.getString("stationName"), jsonObject.getString("measureAddr"));

                } catch (Exception e) {
                    LogHelper.errorStackTrace(e);
                    updateWidget();
                }

            } else {
                LogHelper.e(TAG, "측정할 시간이 되지 않아 서버에서 갱신하지 않음, 최근 측정 시간 : " + AppPreference.loadLastMeasureTime(mContext));
                updateWidget();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LogHelper.e(TAG, "위치 권한 주어지지 않음");
            // Should we show an explanation?

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(MainActivity.this
                    , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}
                    , RESULT_PERMISSIONS_LOCATION);


        }/* else if (Utility.isLocationServiceEnabled(mContext)) {
            LogHelper.e(TAG, "위치 권한 주어짐, 위치정보를 이용하여 측정함");
            buildGoogleApiClient();

            // TODO: 2017. 9. 8. 위치정보를 이용하는 측정소가 변경될 수 있으므로 시간 확인을 어떻게 할지 생각이 필요하다.
            // TODO 측정소명까지만 무조건 확인하고, 직전의 측정소랑 다를 경우에는 시간이 넘어가지 않더라도 getStationDetail 함수를 호출하는 방식이 좋을 것 같음.
            // TODO 일단은 위치정보 이용시 시간 상관없이 무조건 서버에서 값을 갱신하도록 해 놓았다.
//            getLocationData();
        } else {
            showLocationSettingDialog();
        }*/

    }

    /**
     * 레이아웃 초기화
     */
    private void initView() {
        //글꼴 초기화
        Typeface typeFace1 = Typeface.createFromAsset(getAssets(), CConstants.FONT_NANUM_MYEONGJO);
        txtGradeMain.setTypeface(typeFace1);
        btnRefresh.setTypeface(typeFace1);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //측정값 등 초기화
        try {
            if (!AppPreference.loadLastMeasureDetail(mContext).equals("")) {
                setPMValueUI();
            }

        } catch (Exception e) {
            LogHelper.errorStackTrace(e);
        }
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
                    Toast.makeText(mContext, "WHO 기준이 적용되었어요 :)", Toast.LENGTH_SHORT).show();

                } else {
                    LogHelper.e(TAG, "onCheckedChanged, false");
                    AppPreference.saveIsWhoGrade(mContext, false);
                    layoutWho.setVisibility(View.GONE);
                    layoutKorea.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, "WHO 기준이 해제되었어요 :/", Toast.LENGTH_SHORT).show();
                }

                try {
                    setPMValueUI();
                } catch (Exception e) {
                    LogHelper.errorStackTrace(e);
                }
            }
        });

        txtVersion.setText(BuildConfig.VERSION_NAME);
    }

    /**
     * 현재 위치정보를 받아옴 (권한요청도 함)
     */
    public void getLocationData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LogHelper.e(TAG, "위치 권한 주어지지 않음");
            // Should we show an explanation?

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(MainActivity.this
                    , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}
                    , RESULT_PERMISSIONS_LOCATION);
            return;

        } else if (!Utility.isLocationServiceEnabled(mContext)) {       //위치 정보 권한은 있지만 위치 설정은 꺼놓음
            showLocationSettingDialog();

        } else {    //위치 정보 권한 있음

            LogHelper.e(TAG, "위치 권한 주어짐");

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known locationManager. In some rare situations this can be null.
                            if (location != null) {
                                // ...
                                locationX = location.getLongitude();
                                locationY = location.getLatitude();
                                LogHelper.e(TAG, "위치정보 받아오기 성공 : " + locationX + ", " + locationY);
//                            locationX = 126.9004613;
//                            locationY = 37.5347978;

                                getStationList(locationX, locationY); //// TODO: 2017. 10. 29.
                            } else {
                                LogHelper.e(TAG, "위치 정보 못 받아옴, null");
                                Toast.makeText(mContext, "현재 위치 정보를 받아오지 못하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            LogHelper.errorStackTrace(e);
                            updateWidget();
                        }
                    });
        }
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
                    LogHelper.e(TAG, "getStationList : " + jsonArray.toString());
                    double tmX = jsonArray.getJSONObject(0).getDouble("x");
                    double tmY = jsonArray.getJSONObject(0).getDouble("y");

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, CConstants.URL_STATION_LIST_BY_GEO + "tmX=" + tmX + "&tmY=" + tmY, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("list");
                                LogHelper.e(TAG, "URL_STATION_LIST_BY_GEO : " + jsonArray.toString());

                                nearestStationName = jsonArray.getJSONObject(0).getString("stationName");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
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
    void getStationDetail(final String stationName, final String strAddress) {
        if (stationName != null && !stationName.equals("")) {
            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, CConstants.URL_STATION_DETAIL + stationName, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        final JSONArray jsonArray = response.getJSONArray("list");
                        LogHelper.e(TAG, "URL_STATION_DETAIL ARRAY : " + jsonArray.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject lastMeasurePlaceObject = new JSONObject();
                                    lastMeasurePlaceObject.put("stationName", stationName);
                                    lastMeasurePlaceObject.put("measureAddr", strAddress);
                                    AppPreference.saveLastMeasurePlace(mContext, lastMeasurePlaceObject.toString());      //측정소명, 측정 주소 저장

                                    LogHelper.e(TAG, "URL_STATION_DETAIL ARRAY(0) : " + jsonArray.getJSONObject(0).toString());

                                    JSONObject stationDetailObject = jsonArray.getJSONObject(0);
                                    stationDetailObject.put("stationName", stationName);    //측정소 측정값을 저장 후 나중에 불러올 때를 위해 측정소명도 넣어준다.
                                    stationDetailObject.put("measuredAddress", strAddress);     //나중에 불러올 때를 위해 측정소 측정한 사용자의 위치 혹은 검색 위치도 넣어준다.
                                    AppPreference.saveLastMeasureDetail(mContext, stationDetailObject.toString());
                                    AppPreference.saveLastMeasureTime(mContext, stationDetailObject.getString("dataTime"));  //측정한 시간 저장

                                    setPMValueUI();     //UI 업데이트
                                    Toast.makeText(mContext, "미세먼지 정보가 갱신되었습니다.", Toast.LENGTH_SHORT).show();

                                } catch (JSONException | ParseException e) {
                                    LogHelper.errorStackTrace(e);
                                    Toast.makeText(mContext, "미세먼지 정보 갱신 실패. 측정소 상세 측정값을 불러오지 못하였어요.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } catch (JSONException e) {
                        LogHelper.errorStackTrace(e);
                        Toast.makeText(mContext, "미세먼지 정보 갱신 실패. 측정소 상세 측정값을 불러오지 못하였어요.", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LogHelper.e(TAG, "ERROR : " + error.getMessage());
                    Toast.makeText(mContext, "미세먼지 정보 갱신 실패. 측정소 상세 측정값을 불러오지 못하였어요.", Toast.LENGTH_LONG).show();
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
                String strAddress;
                try {
                    JSONArray jsonArray = response.getJSONArray("documents");
                    LogHelper.e(TAG, "URL_KAKAO_GEO_COORD2ADDRESS : " + jsonArray.toString());
                    JSONObject addressObject = jsonArray.getJSONObject(0).getJSONObject("address");

                    strAddress = addressObject.getString("region_2depth_name") + " " + addressObject.getString("region_3depth_name");
                    if (addressObject.getString("region_3depth_name").contains(" "))     // 면, 리 단계의 주소 체계일 경우, 읍, 면까지만 노출시키기 위해 코드 추가
                        strAddress = addressObject.getString("region_2depth_name") + " " + addressObject.getString("region_3depth_name").split(" ")[0];

                    AppPreference.saveLastMeasureAddr(mContext, strAddress);
                } catch (Exception e) {
                    LogHelper.errorStackTrace(e);
                    strAddress = "현재 주소 알수없음";
                }
                getStationDetail(nearestStationName, strAddress);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.e(TAG, "ERROR : " + error.getMessage());
                btnRefresh.setText("주소 알수없음");
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
        if (AppPreference.loadLastMeasureDetail(mContext) == null || AppPreference.loadLastMeasureDetail(mContext).equals(""))
            return;

        JSONObject detailObject = new JSONObject(AppPreference.loadLastMeasureDetail(mContext));
        String pm10Value = detailObject.getString("pm10Value");
        String pm25Value = detailObject.getString("pm25Value");

        if (pm25Value.equals("-"))
            pm25Value = "-1";

        if (pm10Value.equals("-"))
            pm10Value = "-1";

        pm10Grade = Utility.pm10Grade(mContext, pm10Value);
        pm25Grade = Utility.pm25Grade(mContext, pm25Value);

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

        refreshView.setVisibility(View.VISIBLE);

        String dataTime = detailObject.getString("dataTime");

        final String OLD_FORMAT = "yyyy-MM-dd HH:mm";
        final String NEW_FORMAT = "HH:mm";

        // August 12, 2010
        String newDateString;

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = sdf.parse(dataTime);
        sdf.applyPattern(NEW_FORMAT);
        newDateString = sdf.format(d);
        txtSyncTime.setText(newDateString + " 업데이트 됨");

        int mainGrade;  //메인페이지에 표시할 기준 등급, 높은 등급을 기준으로 보여준다.

        if (pm10Grade > pm25Grade) {
            mainGrade = pm10Grade;

        } else {
            mainGrade = pm25Grade;
        }

        txtGradeMain.setText(Utility.getGradeStr(mainGrade));

        txtRecommend.setText(Utility.getWholeStr(mainGrade));
        txtMask.setText(Utility.getMaskStr(mainGrade));
        txtActivity.setText(Utility.getActivityStr(mainGrade));
        txtLife.setText(Utility.getLifeStr(mainGrade));
        txtWindow.setText(Utility.getWindowStr(mainGrade));


        switch (mainGrade) {
           case 0:
                imgMain.setAnimation("json/good/good.json");
                imgMain.setImageAssetsFolder("json/good/images");
                break;
            case 1:
                imgMain.setAnimation("json/normal/normal.json");
                imgMain.setImageAssetsFolder("json/normal/images");
                break;
            case 2:
                imgMain.setAnimation("json/bad/bad.json");
                imgMain.setImageAssetsFolder("json/bad/images");
                break;
            case 3:
                imgMain.setAnimation("json/verybad/verybad.json");
                imgMain.setImageAssetsFolder("json/verybad/images");
                break;
            default:
                imgMain.setAnimation("json/noinfo/noinfo.json");
                imgMain.setImageAssetsFolder("json/noinfo/images");
                break;
        }

        imgMain.playAnimation();
        imgMain.loop(true);

        btnRefresh.setText(detailObject.getString("measuredAddress"));
        updateWidget();
    }

    /**
     * 검색 버튼 클릭 이벤트
     */
    public void btnSearchClick() {
        Animation a = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        a.setRepeatCount(2);
        a.setDuration(1000);
        refreshImg.startAnimation(a);


        if (AppPreference.loadIsUseSearchedStation(mContext)) {      //검색 하여 찾을 때
            if (Utility.shouldRefreshDetail(mContext)) {         //최근 갱신한 시간을 불러와서 갱신할지 여부 확인
                LogHelper.e(TAG, "측정할 시간이 되어 서버에서 갱신, 최근 측정 시간 : " + AppPreference.loadLastMeasureTime(mContext));

                try {
                    JSONObject jsonObject = new JSONObject(AppPreference.loadLastMeasureStation(mContext));
                    getStationDetail(jsonObject.getString("stationName"), jsonObject.getString("measureAddr"));

                } catch (Exception e) {
                    LogHelper.errorStackTrace(e);
                }

            } else {
                LogHelper.e(TAG, "측정할 시간이 되지 않아 서버에서 갱신하지 않음, 최근 측정 시간 : " + AppPreference.loadLastMeasureTime(mContext));
                Toast.makeText(mContext, "미세먼지 정보가 갱신되었습니다.", Toast.LENGTH_SHORT).show();   //사용자에게는 갱신되었다고 보여줌
            }
        } else {        //메인페이지에서 현재 주소 찾을 때에는 시간 상관없이 항상 검색.
            getLocationData();

        }
    }

    /**
     * 위젯 업데이트 메소드
     */
    private void updateWidget() {
        LogHelper.e(TAG, "updateWidget 진입");
        Intent updateIntent = new Intent(mContext, WidgetProvider.class);
        updateIntent.setAction(CConstants.UPDATE_WIDGET);
        sendBroadcast(updateIntent);
    }


    /**
     * 검색 업데이트 메소드 (임시로 추가한 메소드)
     */
    @OnClick(R.id.btn_search)
    void btnSearchClick_tmp() {
        Bundle bundle = new Bundle();
        bundle.putString("where", "btnSearch");
        mFirebaseAnalytics.logEvent("update_click", bundle);

        SearchDialog mDialog = new SearchDialog(this);
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

    /**
     * 새로고침 버튼 클릭 이벤트
     */
    @OnClick(R.id.container_refresh)
    void btnRefreshClick() {
        new FCMIdService().onTokenRefresh();
        Bundle bundle = new Bundle();
        bundle.putString("where", "btnRefresh");
        mFirebaseAnalytics.logEvent("update_click", bundle);

        SearchDialog mDialog = new SearchDialog(this);
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
            case RESULT_PERMISSIONS_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    LogHelper.e(TAG, "권한 확인 3");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    buildGoogleApiClient();
//                    getLocationData();
                } else {
                    LogHelper.e(TAG, "권한 확인 4");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    boolean showRationale = false;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permissions[1]);
                    }
                    if (!showRationale) {
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting

                        DenyPmsDialog mDialog = new DenyPmsDialog(mContext);
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
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CConstants.RESULT_SEARCH_LOCATION && resultCode == CConstants.RESULT_SELECT_ITEM) {
            locationX = data.getDoubleExtra("x", -1);
            locationY = data.getDoubleExtra("y", -1);
            getStationList(locationX, locationY);

        } else if (requestCode == CConstants.RESULT_PERMISSION_SETTINGS) {
            getLocationData();

        } else if (requestCode == RESULT_LOCATION_SETTINGS) {
//            if (Utility.isLocationServiceEnabled(mContext)) {
//                buildGoogleApiClient();
//            }
        }
    }

    /**
     * 검색 창으로 이동
     */
    public void openSearchPage() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivityForResult(intent, CConstants.RESULT_SEARCH_LOCATION);
    }

    /**
     * 권한 설정으로 이동
     */
    public void openPermissionPage() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, CConstants.RESULT_PERMISSION_SETTINGS);
    }

    /**
     * 위치 설정 안내 다이얼로그 보여줌
     */
    private void showLocationSettingDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("위치 설정 안내")
                .setMessage("현재 위치의 미세먼지 정보를 받으시려면 위치 설정을 '사용'으로 변경해 주세요.")
                .setPositiveButton("설정하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        startActivityForResult(intent, RESULT_LOCATION_SETTINGS);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    protected synchronized void buildGoogleApiClient() {
        LogHelper.e(TAG, "buildGoogleApiClient 시작");
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            LogHelper.e(TAG, "googleApiClient 연결 안 되어있으므로 연결함.");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        } else {
            LogHelper.e(TAG, "googleApiClient 연결되어있으므로 추가로 연결하지 않음.");
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                LogHelper.e(TAG, "Location callBack : " + location.getLatitude() + " " + location.getLongitude());

            }

            getLocationData();
            updateWidget();
        }
    };

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            try {
                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            } catch (Exception e) {
                LogHelper.errorStackTrace(e);
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        LogHelper.e(TAG, "onConnected 진입");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000); // two minute interval
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LogHelper.e(TAG, "onConnected, 위치 권한 주어짐");
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        } else {
            LogHelper.e(TAG, "onConnected, 위치 권한 주어지지 않음");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLocation();
    }

    /**
     * 다음 조건이 모두 만족된 경우 위치정보를 업데이트 한다
     * 조건 1. 위치 검색 모드일 경우
     * 조건 2. 위치 권한이 주어진 경우
     * 조건 3. 위치 설정이 켜진 경우
     */
    public void updateLocation() {
        LogHelper.e(TAG, "updateLocation 진입 :" + AppPreference.loadIsUseSearchedStation(mContext));
        if (AppPreference.loadIsUseSearchedStation(mContext)
                && (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED))
                ) {      //검색한 위치 이용하는 경우
            if (Utility.isLocationServiceEnabled(mContext)) {
                LogHelper.e(TAG, "updateLocation, 위치 권한 주어짐, 위치정보를 이용하여 측정함");
                buildGoogleApiClient();
            } else {
                showLocationSettingDialog();
            }
        } else {
            LogHelper.e(TAG, "UPDATELOCATION 1");
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        imgMain.playAnimation();
    }

    @Override
    public void onStop() {
        super.onStop();
        imgMain.cancelAnimation();
    }
}
