package com.projek.projekshakealarm;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {

            NotificationChannel channelAlarm = new NotificationChannel("channelAlarm",
                    "Alarm",
                    NotificationManager.IMPORTANCE_HIGH);

            channelAlarm.setDescription("This is an alarm channel");
            channelAlarm.setVibrationPattern(new long[] { 1000, 1000, 1000, 1000, 1000 });

            try {
                NotificationManager manager = getSystemService(NotificationManager.class);

                manager.createNotificationChannel(channelAlarm);
            } catch (Exception e) {
                Log.e("NotificationManager value", null);
            }
        }
    }
}
