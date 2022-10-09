package com.projek.projekshakealarm.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.projek.projekshakealarm.MainActivity;
import com.projek.projekshakealarm.R;


public class RingtonePlayingService extends Service {

    MediaPlayer mediaPlayer;
    int song_playing;

    int startId;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("in the ringtone service", "you made it !");

        String extraSwitch = intent.getStringExtra("switch");
        String extraSong = intent.getStringExtra("song");

        String extraHour = intent.getStringExtra("hour");
        String extraMinute = intent.getStringExtra("minute");
        String extraSistem = intent.getStringExtra("sistem");

        Log.e("extraSwitch value", extraSwitch);
        Log.e("extraSong value", extraSong);

        Log.e("extraHour value", extraHour);
        Log.e("extraMinute value", extraMinute);
        Log.e("extraSistem value", extraSistem);

        //notification
        //set up the notification service
        NotificationManager notify_manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        //set up an intent that goes to the MainActivity
        Intent intent_main = new Intent(this.getApplicationContext(), MainActivity.class);

        //set up a pending intent
        PendingIntent pending_main = PendingIntent.getActivity(this,
                0, intent_main, 0);

        //make the notification parameters
        NotificationCompat.Builder notif_popup = new NotificationCompat.Builder(
                this.getApplicationContext(),
                "channelAlarm");

        notif_popup.setContentIntent(pending_main);
        notif_popup.setSmallIcon(R.drawable.ic_alarm);
        notif_popup.setContentTitle(extraHour + ":" + extraMinute + " " + extraSistem);
        notif_popup.setContentText("Shake your phone to stop it");
        notif_popup.setPriority(NotificationCompat.PRIORITY_MAX);

        assert  extraSwitch != null;
        switch (extraSwitch) {
            case "alarm on" :
                startId = 1;
                break;
            case "alarm off" :
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        assert  extraSong != null;
        switch (extraSong) {
            case "Your Name" :
                song_playing = R.raw.ringtone;
                break;
            case "Blue Water":
                song_playing = R.raw.blue_water;
                break;
            case "Hikari" :
                song_playing = R.raw.hikari;
                break;
            case "Funk" :
                song_playing = R.raw.funk;
                break;
            case "Morning" :
                song_playing = R.raw.morning;
                break;
            default:
                break;
        }

        //if else statements

        //if there is no music playing, and the user pressed "alarm on" button
        //music should start playing
        if(!this.isRunning && startId == 1) {
            Log.e("there is no music", "and you want start");
//
//            create an instance of the media player
            mediaPlayer = MediaPlayer.create(this, song_playing);

            //start the ringtone
            mediaPlayer.start();

            this.isRunning = true;
            this.startId = 0;

            notify_manager.notify(1,notif_popup.build());
        }

        //if there is music playing, and the user pressed "alarm off" button
        //music should stop playing
        else if(this.isRunning && startId == 0) {
            Log.e("there is music", "and you want end");

            //stop the ringtone
            mediaPlayer.stop();
            mediaPlayer.reset();

            notify_manager.cancel(1);

            this.isRunning = false;
            this.startId = 0;
        }

        //these are if the user presses random buttons
        //just to bug-proof the app
        //if there is no music playing, and the user pressed "alarm off" button
        //do nothing
        else if(!this.isRunning && startId == 0) {
            Log.e("there is no music", "and you want end");

            this.isRunning = false;
            this.startId = 0;
        }

        //if there is music playing, and the user pressed "alarm on" button
        //do nothing
        else if(this.isRunning && startId == 1) {
            Log.e("there is music", "and you want start");

            this.isRunning = true;
            this.startId = 1;
        }

        //can't think of anything else, just to catch the odd event
        else {
            Log.e("else", "somehow you reached this");
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.isRunning = false;
    }
}
