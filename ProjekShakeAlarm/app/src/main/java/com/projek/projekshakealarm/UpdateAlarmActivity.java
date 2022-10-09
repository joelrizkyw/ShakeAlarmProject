package com.projek.projekshakealarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.projek.projekshakealarm.Database.DatabaseHelper;
import com.projek.projekshakealarm.Model.DataAlarm;

public class UpdateAlarmActivity extends AppCompatActivity {
    private TimePicker alarmUpdatePicker;
    private Button btnEditAlarm, btnDeleteAlarm;
    private Spinner spinnerUpdateRingtone;

    DataAlarm dataAlarm;
    AlarmManager alarmManager;

    int idAlarm;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_alarm);

        intent = getIntent();
        String id = intent.getStringExtra("id alarm");

        idAlarm = Integer.parseInt(id);

        dataAlarm = new DataAlarm(UpdateAlarmActivity.this, "write");
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmUpdatePicker = (TimePicker) findViewById(R.id.alarmUpdatePicker);
        btnEditAlarm = (Button) findViewById(R.id.btnEditAlarm);
        btnDeleteAlarm = (Button) findViewById(R.id.btnDeleteAlarm);
        spinnerUpdateRingtone = (Spinner) findViewById(R.id.spinnerUpdateRingtone);

        try {
            Cursor cursor = dataAlarm.getAlarmDataById(idAlarm);

            if(cursor.getCount() > 0) {
                while(cursor.moveToNext()) {
                    String hour = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_JAM_ALARM));
                    String minute = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_MENIT_ALARM));
                    String ringtone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_RINGTONE_ALARM));

                    int hour_alarm = Integer.parseInt(hour);
                    int minute_alarm = Integer.parseInt(minute);

                    alarmUpdatePicker.setHour(hour_alarm);
                    alarmUpdatePicker.setMinute(minute_alarm);

                    spinnerUpdateRingtone.setSelection(getIndex(spinnerUpdateRingtone,
                            cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_RINGTONE_ALARM))));
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong in update activity", Toast.LENGTH_SHORT).show();
        }


        btnEditAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAlarmData(v);
            }
        });

        btnDeleteAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlarmData(v);
            }
        });
    }

    private void editAlarmData(View view) {
        int hour = alarmUpdatePicker.getHour();
        int minute = alarmUpdatePicker.getMinute();

        String convert_hour = String.valueOf(set12HourSistem(hour));

        String hour_text = addZeroHour(convert_hour);
        String minute_text = addZeroMinute(minute);

        String sistem = setAlarmSistemAMPM(hour);

        String ringtone = spinnerUpdateRingtone.getSelectedItem().toString();
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

        //set new alarm data
        dataAlarm.setJam_alarm(hour);
        dataAlarm.setJam_text_alarm(hour_text);
        dataAlarm.setMenit_alarm(minute);
        dataAlarm.setMenit_text_alarm(minute_text);
        dataAlarm.setSistem_alarm(sistem);

        if (song_choosed != "Choose Ringtone") {
            dataAlarm.setLagu_alarm(song_choosed);

            int update = dataAlarm.updateAlarmData(idAlarm);

            if(update > 0) {
                Toast.makeText(this, "Alarm updated", Toast.LENGTH_SHORT).show();

                returnHome();

                finish();
            } else {
                Toast.makeText(this, "Can't update alarm data", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "You need to choose a ringtone !", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteAlarmData(View view) {
        int delete = dataAlarm.deleteAlarmData(idAlarm);

        if(delete > 0) {
            Toast.makeText(UpdateAlarmActivity.this, "Alarm deleted ", Toast.LENGTH_LONG).show();

            returnHome();

            finish();
        } else {
            Toast.makeText(UpdateAlarmActivity.this, "Can't delete alarm data" , Toast.LENGTH_LONG).show();
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

    private PendingIntent getDistinctPendingIntent(Intent intent, int requestId) {
        PendingIntent pi = PendingIntent.getBroadcast(
                UpdateAlarmActivity.this,
                requestId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pi;
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    private void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}
