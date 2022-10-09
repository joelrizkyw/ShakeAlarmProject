package com.projek.projekshakealarm.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.projek.projekshakealarm.Service.RingtonePlayingService;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("in the receiver", "you made it !");

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

        Intent intent_service = new Intent(context, RingtonePlayingService.class);

        intent_service.putExtra("switch", extraSwitch);
        intent_service.putExtra("song", extraSong);
        intent_service.putExtra("hour", extraHour);
        intent_service.putExtra("minute", extraMinute);
        intent_service.putExtra("sistem", extraSistem);

        context.startService(intent_service);
    }
}
