package com.momu.misehan.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.momu.misehan.R;
import com.momu.misehan.activity.SplashActivity;
import com.momu.misehan.constant.CConstants;
import com.momu.misehan.preference.AppPreference;
import com.momu.misehan.utility.LogHelper;
import com.momu.misehan.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.android.gms.internal.zzagz.runOnUiThread;

/**
 * 위젯에서 사용하는 AppWidgetProvider
 * Created by knulps on 2017. 8. 25..
 */

public class WidgetProvider extends AppWidgetProvider {
    private static final String TAG = "WidgetProvider";
    int pm10Grade, pm25Grade;
    int mainGrade = -1;
    int appWidgetId;
    Handler handler = new Handler();

    AppWidgetManager appWidgetManager;
    private FirebaseAnalytics mFirebaseAnalytics;

    /**
     * 브로드캐스트를 수신할때, override된 콜백 메소드가 호출되기 직전에 호출됨
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        LogHelper.e(TAG, "onReceive 진입");

        if (CConstants.UPDATE_WIDGET.equals(intent.getAction())) {    //위젯의 업데이트 버튼 클릭 시 동작하는 부분

            appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            for (int i = 0; i < appWidgetIds.length; i++) {
                updateAppWidget(context, appWidgetManager, appWidgetIds[i], false);
            }
        }
    }

    /**
     * 위젯을 갱신할때 호출됨
     * <p>
     * 주의 : Configure Activity를 정의했을때는 위젯 등록시 처음 한번은 호출이 되지 않습니다
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        this.appWidgetManager = appWidgetManager;
        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        for (int i = 0; i < appWidgetIds.length; i++) {

            updateAppWidget(context, appWidgetManager, appWidgetIds[i], true);
        }
    }


    /**
     * 생성된 위젯 갱신
     *
     * @param appWidgetManager
     * @param appWidgetId      해당 위젯 ID
     */
    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, boolean isFromOnUpdate) {
        LogHelper.e(TAG, "updateAppWidget 진입");

        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);

        if (!isFromOnUpdate) {       //수동 업데이트인 경우에만 프로그레스 다이얼로그를 보여준다.
            updateViews.setViewVisibility(R.id.progressBar, View.VISIBLE);
            updateViews.setViewVisibility(R.id.sync_default, View.GONE);
        }
        this.appWidgetId = appWidgetId;

        try {
            if (Utility.shouldRefreshDetail(context)) {
                LogHelper.e(TAG, "위젯 업데이트 할 시간이 됨, 서버 통신까지함");
                if (AppPreference.loadIsUseSearchedStation(context)) {      //검색 하여 찾을 때는 측정소 고정
                    JSONObject jsonObject = new JSONObject(AppPreference.loadLastMeasureStation(context));
                    getStationDetail(jsonObject.getString("stationName"), jsonObject.getString("measureAddr"), context);

                } else {    //위치기반일 경우 위치까지 검색해야됨.  지금은 그냥 검색하자.// TODO: 2017. 9. 23.
                    JSONObject jsonObject = new JSONObject(AppPreference.loadLastMeasureStation(context));
                    getStationDetail(jsonObject.getString("stationName"), jsonObject.getString("measureAddr"), context);
                }

            } else {
                LogHelper.e(TAG, "위젯 업데이트 할 시간이 아직 안됨 ui만 반영함");
                setPMValueUI(context);
            }

        } catch (JSONException | ParseException e) {
            LogHelper.errorStackTrace(e);
        }


        //updateViews.setTextViewText(R.id.txt_time_sync_widget, Utility.getTimeFormatFromMillis(AppPreference.loadLastMeasureTime(context)));

        /**
         * 레이아웃을 클릭하면 앱 실행
         */
        Intent intent = new Intent(context, SplashActivity.class);
        intent.putExtra("where","widget");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        updateViews.setOnClickPendingIntent(R.id.layout_widget_body, pendingIntent);

        /**
         * 새로고침 클릭 시 새로고침 코드 실행(onReceive 콜 함)
         */
        Intent update = new Intent(context, WidgetProvider.class);
        update.setAction(CConstants.UPDATE_WIDGET);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, update, 0);
        updateViews.setOnClickPendingIntent(R.id.btn_sync, pendingIntent1);

        /**
         * 위젯 업데이트
         */
        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }

    /**
     * 위젯이 처음 생성될때 호출됨
     * <p>
     * 동일한 위젯이 생성되도 최초 생성때만 호출됨
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        LogHelper.e(TAG, "onEnabled 진입");
    }

    /**
     * 위젯의 마지막 인스턴스가 제거될때 호출됨
     * <p>
     * onEnabled()에서 정의한 리소스 정리할때
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        LogHelper.e(TAG, "onDisabled 진입");
    }

    /**
     * 위젯이 사용자에 의해 제거될때 호출됨
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        LogHelper.e(TAG, "onDeleted 진입");
    }

    /**
     * 측정소 이름으로 환경공단 공공 API에서 측정소에서 측정한 정보를 가져옴.
     *
     * @param stationName 측정소 이름
     */
    void getStationDetail(final String stationName, final String strAddress, final Context mContext) {
        LogHelper.e(TAG, "getStationDetail 진입   " + stationName);


        if (stationName != null && !stationName.equals("")) {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, CConstants.URL_STATION_DETAIL + stationName, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        final JSONArray jsonArray = response.getJSONArray("list");
                        LogHelper.e(TAG, "URL_STATION_DETAIL ARRAY : " + jsonArray.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                boolean isError = false;
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


                                    try {
                                        setPMValueUI(mContext);
                                    } catch (ParseException e) {
                                        LogHelper.errorStackTrace(e);
                                        isError = true;
                                    }
                                    //Toast.makeText(mContext, "미세먼지 정보가 갱신되었습니다.", Toast.LENGTH_SHORT).show();


                                } catch (JSONException e) {
                                    LogHelper.errorStackTrace(e);
                                    isError = true;
                                }
                                if (isError) {
                                    setFailedUi(mContext);
                                }
                            }
                        });

                    } catch (JSONException e) {
                        LogHelper.errorStackTrace(e);
                        setFailedUi(mContext);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    LogHelper.e(TAG, "ERROR : " + error.getMessage());
                    setFailedUi(mContext);
                }
            });

            queue.add(jsonRequest);
        }
    }

    /**
     * 메인화면에 초미세먼지, 미세먼지 관련 UI 업데이트
     *
     * @param mContext
     * @throws JSONException
     * @throws ParseException
     */
    int setPMValueUI(Context mContext) throws JSONException, ParseException {
        LogHelper.e(TAG, "setPMValueUI 진입");

        final RemoteViews updateViews = new RemoteViews(mContext.getPackageName(), R.layout.layout_widget);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateViews.setViewVisibility(R.id.progressBar, View.GONE);
                updateViews.setViewVisibility(R.id.sync_default, View.VISIBLE);

                appWidgetManager.updateAppWidget(appWidgetId, updateViews);
            }
        }, 1500);


        if (AppPreference.loadLastMeasureDetail(mContext) == null || AppPreference.loadLastMeasureDetail(mContext).equals(""))
            return -1;

        JSONObject detailObject = new JSONObject(AppPreference.loadLastMeasureDetail(mContext));
        String pm10Value = detailObject.getString("pm10Value");
        String pm25Value = detailObject.getString("pm25Value");

        if (pm25Value.equals("-"))
            pm25Value = "-1";

        if (pm10Value.equals("-"))
            pm10Value = "-1";

        pm10Grade = Utility.pm10Grade(mContext, pm10Value);
        pm25Grade = Utility.pm25Grade(mContext, pm25Value);

        String dataTime = detailObject.getString("dataTime");

        final String OLD_FORMAT = "yyyy-MM-dd HH:mm";
        final String NEW_FORMAT = "HH:mm";

        // August 12, 2010
        String newDateString;

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = sdf.parse(dataTime);
        sdf.applyPattern(NEW_FORMAT);
        newDateString = sdf.format(d);

        int mainGrade;  //메인페이지에 표시할 기준 등급, 높은 등급을 기준으로 보여준다.

        if (pm10Grade > pm25Grade) {
            mainGrade = pm10Grade;

        } else {
            mainGrade = pm25Grade;
        }

        /**
         * 위젯 ui 수정하는 부분
         */


        switch (mainGrade) {
            case 0:
                updateViews.setImageViewResource(R.id.img_main_widget, R.drawable.ic_status_1);
                break;
            case 1:
                updateViews.setImageViewResource(R.id.img_main_widget, R.drawable.ic_status_2);
                break;
            case 2:
                updateViews.setImageViewResource(R.id.img_main_widget, R.drawable.ic_status_3);
                break;
            case 3:
                updateViews.setImageViewResource(R.id.img_main_widget, R.drawable.ic_status_4);
                break;
            default:
                updateViews.setImageViewResource(R.id.img_main_widget, R.drawable.ic_status_5);
                break;
        }
        updateViews.setTextViewText(R.id.txt_address_widget, AppPreference.loadLastMeasureAddr(mContext));
        updateViews.setTextViewText(R.id.txt_status_main, Utility.getGradeStr(mainGrade));
        updateViews.setTextViewText(R.id.txt_recommend, Utility.getWholeStr(mainGrade));
        this.mainGrade = mainGrade;

        appWidgetManager.updateAppWidget(appWidgetId, updateViews);

        return mainGrade;
    }

    /**
     * 정보를 제대로 가져오지 못했을 때 보여주는 ui
     */
    void setFailedUi(Context mContext) {
        RemoteViews updateViews = new RemoteViews(mContext.getPackageName(), R.layout.layout_widget);

        updateViews.setViewVisibility(R.id.progressBar, View.GONE);
        updateViews.setViewVisibility(R.id.sync_default, View.VISIBLE);

        updateViews.setImageViewResource(R.id.img_main_widget, R.drawable.ic_status_1);
        updateViews.setTextViewText(R.id.txt_status_main, "정보를 불러오지 못했어요.");
        updateViews.setTextViewText(R.id.txt_recommend, "");


        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }
}
