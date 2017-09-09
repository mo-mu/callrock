package com.momu.callrock.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.momu.callrock.R;
import com.momu.callrock.activity.SplashActivity;
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

import static com.google.android.gms.internal.zzagz.runOnUiThread;

/**
 * 위젯에서 사용하는 AppWidgetProvider
 * Created by knulps on 2017. 8. 25..
 */

public class WidgetProvider extends AppWidgetProvider {
    private static final String TAG = "WidgetProvider";
    int pm10Grade, pm25Grade;
    int mainGrade = 3;
    int appWidgetId;
    String UPDATE_WIDGET = "UPDATE CLICKED";

    /**
     * 브로드캐스트를 수신할때, Override된 콜백 메소드가 호출되기 직전에 호출됨
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        LogHelper.e(TAG, "onReceive 진입");

        if(UPDATE_WIDGET.equals(intent.getAction())){


            LogHelper.e(TAG, "onReceive 진입 DFDFDF");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            for (int i = 0; i < appWidgetIds.length; i++) {

                updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
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

        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        for (int i = 0; i < appWidgetIds.length; i++) {

            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }


    /**
     * 생성된 위젯 갱신
     *
     * @param appWidgetManager
     * @param appWidgetId      해당 위젯 ID
     */
    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        try {
            this.appWidgetId = appWidgetId;
            JSONObject jsonObject = new JSONObject(AppPreference.loadLastMeasureStation(context));
            getStationDetail(jsonObject.getString("stationName"), jsonObject.getString("measureAddr"),context,appWidgetManager);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        LogHelper.e(TAG, "updateAppWidget 진입");
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
        updateViews.setTextViewText(R.id.txt_address_widget, AppPreference.loadLastMeasureAddr(context));
        updateViews.setTextViewText(R.id.txt_status_main, Utility.getGradeStr(mainGrade));
        updateViews.setTextViewText(R.id.txt_recommend, Utility.getWholeStr(mainGrade));
        //updateViews.setTextViewText(R.id.txt_time_sync_widget, Utility.getTimeFormatFromMillis(AppPreference.loadLastMeasureTime(context)));

        /**
         * 레이아웃을 클릭하면 앱 실행
         */
        Intent intent = new Intent(context, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        updateViews.setOnClickPendingIntent(R.id.layout_widget_body, pendingIntent);

        Intent update = new Intent(context,WidgetProvider.class);
        update.setAction(UPDATE_WIDGET);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context,0,update,0);
        updateViews.setOnClickPendingIntent(R.id.btn_sync_widget,pendingIntent1);

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
    void getStationDetail(final String stationName, final String strAddress, final Context mContext, final AppWidgetManager appWidgetManager) {
        LogHelper.e(TAG, "getStationDetail 진입   "+stationName);


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
                                        setPMValueUI(mContext,appWidgetManager);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    //Toast.makeText(mContext, "미세먼지 정보가 갱신되었습니다.", Toast.LENGTH_SHORT).show();


                                } catch (JSONException e) {
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
     * 메인화면에 초미세먼지, 미세먼지 관련 UI 업데이트
     *
     * @throws JSONException
     * @throws ParseException
     */
    int  setPMValueUI(Context mContext,AppWidgetManager appWidgetManager) throws JSONException, ParseException {
        LogHelper.e(TAG, "setPMValueUI 진입");

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
        RemoteViews updateViews = new RemoteViews(mContext.getPackageName(), R.layout.layout_widget);

        switch (mainGrade) {
            case 0:
                updateViews.setImageViewResource(R.id.img_main_widget,R.drawable.ic_status_1);
                break;
            case 1:
                updateViews.setImageViewResource(R.id.img_main_widget,R.drawable.ic_status_2);
                break;
            case 2:
                updateViews.setImageViewResource(R.id.img_main_widget,R.drawable.ic_status_3);
                break;
            case 3:
                updateViews.setImageViewResource(R.id.img_main_widget,R.drawable.ic_status_4);
                break;
        }
        updateViews.setTextViewText(R.id.txt_address_widget, AppPreference.loadLastMeasureAddr(mContext));
        updateViews.setTextViewText(R.id.txt_status_main, Utility.getGradeStr(mainGrade));
        updateViews.setTextViewText(R.id.txt_recommend, Utility.getWholeStr(mainGrade));
        this.mainGrade = mainGrade;


        /**
         * 위젯 업데이트
         */
        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
        return mainGrade;
    }

}
