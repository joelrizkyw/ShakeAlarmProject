package com.projek.projekshakealarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.projek.projekshakealarm.Model.DataAlarm;

public class AlarmActivity extends AppCompatActivity {
    private TimePicker alarmPicker;
    private Button btnAddAlarm;
    private Spinner spinnerRingtone;

    DataAlarm dataAlarm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        dataAlarm = new DataAlarm(AlarmActivity.this, "write");

        alarmPicker = (TimePicker) findViewById(R.id.alarmPicker);
        btnAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
        spinnerRingtone = (Spinner) findViewById(R.id.spinnerRingtone);

        btnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarmData(v);
            }
        });
    }

    private void addAlarmData(View view) {
        int hour = alarmPicker.getHour();
        int minute = alarmPicker.getMinute();

        String convert_hour = String.valueOf(set12HourSistem(hour));

        String hour_text = addZeroHour(convert_hour);
        String minute_text = addZeroMinute(minute);

        String sistem = setAlarmSistemAMPM(hour);
//
//        Toast.makeText(AlarmActivity.this,
//                hour_text  + " : "  + minute_text + " "  + sistem, Toast.LENGTH_SHORT).show();


        String ringtone = spinnerRingtone.getSelectedItem().toString();
        String song_choosed;

        switch(ringtone) {
            case "Your Name" :
                song_choosed = "Your Name";
                break;
            case "Blue Water":
                song_choosed = "Blue Water";
                break;
            case "Hikari" :
                song_choosed = "Hikari";
                break;
            case "Funk" :
                song_choosed = "Funk";
                break;
            case "Morning" :
                song_choosed = "Morning";
                break;
            default:
                song_choosed = "Choose Ringtone";
                break;
        }

        dataAlarm.setJam_alarm(hour);
        dataAlarm.setJam_text_alarm(hour_text);
        dataAlarm.setMenit_alarm(minute);
        dataAlarm.setMenit_text_alarm(minute_text);
        dataAlarm.setSistem_alarm(sistem);

        if (song_choosed != "Choose Ringtone") {
            dataAlarm.setLagu_alarm(song_choosed);

            boolean insert = dataAlarm.insertAlarmData();

            if(insert == true) {
                Toast.makeText(this, "Alarm added", Toast.LENGTH_SHORT).show();

                returnHome();
            } else {
                Toast.makeText(this, "Can't add alarm data", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "You need to choose a ringtone !", Toast.LENGTH_SHORT).show();
        }


    }

    private String addZeroHour(String hour) {
        String str;
        int hour_alarm = Integer.parseInt(hour);

        if(hour_alarm < 10) {
            str = "0" + hour;
        } else {
            str = hour;
        }

        return str;
    }

    private String addZeroMinute(int minute) {
        String str;

        if(minute < 10) {
            str = "0" + String.valueOf(minute);
        } else {
            str = String.valueOf(minute);
        }

        return str;
    }

    private int set12HourSistem(int hour) {
        int am_or_pm;

        if(hour == 0) {
            am_or_pm = hour;
        } else if(hour < 12) {
            am_or_pm = hour;
        } else if(hour == 12) {
            am_or_pm = hour;
        } else {
            am_or_pm = hour - 12;
        }

        return am_or_pm;
    }

    private String setAlarmSistemAMPM(int hour) {
        String am_or_pm;

        if(hour == 0) {
            am_or_pm = "AM";
        } else if(hour < 12) {
            am_or_pm = "AM";
        } else if(hour == 12) {
            am_or_pm = "PM";
        } else {
            am_or_pm = "PM";
        }

        return am_or_pm;
    }

    private void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }
}
