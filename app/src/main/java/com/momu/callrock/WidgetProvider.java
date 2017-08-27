package com.momu.callrock;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.momu.callrock.activity.SplashActivity;
import com.momu.callrock.preference.AppPreference;
import com.momu.callrock.utility.LogHelper;
import com.momu.callrock.utility.Utility;

/**
 * Created by knulps on 2017. 8. 25..
 */

public class WidgetProvider extends AppWidgetProvider {
    private static final String TAG = "WidgetProvider";

    /**
     * 브로드캐스트를 수신할때, Override된 콜백 메소드가 호출되기 직전에 호출됨
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        LogHelper.e(TAG, "onReceive 진입");
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
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        LogHelper.e(TAG, "updateAppWidget 진입");
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
        updateViews.setTextViewText(R.id.txt_address_widget, AppPreference.loadLastMeasureAddr(context));
        updateViews.setTextViewText(R.id.txt_status_main, "보통");
        updateViews.setTextViewText(R.id.txt_recommend, "그냥~그냥 쏘쏘한 날이에요");
        //updateViews.setTextViewText(R.id.txt_time_sync_widget, Utility.getTimeFormatFromMillis(AppPreference.loadLastMeasureTime(context)));


        /**
         * 레이아웃을 클릭하면 앱 실행
         */
        Intent intent = new Intent(context, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        updateViews.setOnClickPendingIntent(R.id.layout_widget_body, pendingIntent);

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
}
