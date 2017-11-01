package com.momu.misehan.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.momu.misehan.R;
import com.momu.misehan.activity.MainActivity;
import com.momu.misehan.activity.SplashActivity;
import com.momu.misehan.constant.CConstants;
import com.momu.misehan.utility.LogHelper;

/**
 * Created by songm on 2017-09-30.
 */

public class FCMservice extends FirebaseMessagingService {

    private static final String TAG = "FCMservice";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        LogHelper.e(TAG,remoteMessage.getNotification().getBody()+"  "+remoteMessage.getData().get("title")+"  "+ remoteMessage.getData().get("message"));
        sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
    }



    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageTitle, String messageBody) {

        /*      Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CConstants.default_notification_channel_id)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.noti_icon)
                .setContentTitle(messageTitle)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentText(messageBody)
                .setContentIntent(pendingIntent);

        notificationManager.notify(0, builder.build());


        */
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CConstants.default_notification_channel_id);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("hi", "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationBuilder.setChannelId("hi");
        }

        notificationBuilder.setSmallIcon(R.drawable.noti_icon)
                .setTicker("미세한")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_MAX)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent);


        notificationManager.notify(0, notificationBuilder.build());
    }
}
