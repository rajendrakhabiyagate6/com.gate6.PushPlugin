package com.plugin.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.ionicframework.benefit187810.MainActivity;

import static android.content.Intent.*;


/**
 * Created by kundan on 10/22/2015.
 */
public class PushNotificationService extends GcmListenerService{
    private static final String TAG = "PushNotificationService";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d("Push Notification Service", "onMessageReceived - from: " + from);
        String message = data.getString("message");
        //createNotification(mTitle, push_msg);
        /*Intent dialogIntent = new Intent(this, MainActivity.class);
        dialogIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        dialogIntent.putExtra("message", message);
        startActivity(dialogIntent);*/


       //Notify(message,message);
        createNotification(from, data);

    }

    public void createNotification(String notificationTitle, Bundle extras)
    {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String appName = getAppName(this);

        Intent notificationIntent = new Intent(this, PushHandlerActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra("pushBundle", extras);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int defaults = Notification.DEFAULT_ALL;

        if (extras.getString("defaults") != null) {
            try {
                defaults = Integer.parseInt(extras.getString("defaults"));
            } catch (NumberFormatException e) {}
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setDefaults(defaults)
                        .setSmallIcon(this.getApplicationInfo().icon)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle(extras.getString("title"))
                        .setTicker(extras.getString("title"))
                        .setContentIntent(contentIntent)
                        .setAutoCancel(true);

        String message = extras.getString("message");
        if (message != null) {
            mBuilder.setContentText(message);
        } else {
            mBuilder.setContentText("<missing message content>");
        }

        String msgcnt = extras.getString("msgcnt");
        if (msgcnt != null) {
            mBuilder.setNumber(Integer.parseInt(msgcnt));
        }

        int notId = 0;

        try {
            notId = Integer.parseInt(extras.getString("notId"));
        }
        catch(NumberFormatException e) {
            Log.e(TAG, "Number format exception - Error parsing Notification ID: " + e.getMessage());
        }
        catch(Exception e) {
            Log.e(TAG, "Number format exception - Error parsing Notification ID" + e.getMessage());
        }

        mNotificationManager.notify((String) appName, notId, mBuilder.build());
    }

    private static String getAppName(Context context)
    {
        CharSequence appName =
                context
                        .getPackageManager()
                        .getApplicationLabel(context.getApplicationInfo());

        return (String)appName;
    }
}
