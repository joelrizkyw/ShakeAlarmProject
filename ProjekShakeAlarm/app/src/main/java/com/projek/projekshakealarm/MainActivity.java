package com.projek.projekshakealarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.projek.projekshakealarm.Database.DatabaseHelper;
import com.projek.projekshakealarm.Model.DataAlarm;
import com.projek.projekshakealarm.Receiver.AlarmReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.seismic.ShakeDetector;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements ShakeDetector.Listener{

    private SimpleCursorAdapter adapter;
    private ListView listAlarm;
    private FloatingActionButton fabSetAlarm;

    private String[] from = new String[]{DatabaseHelper.COL_ID_ALARM, DatabaseHelper.COL_JAM_TEXT,
            DatabaseHelper.COL_MENIT_TEXT, DatabaseHelper.COL_SISTEM_AM_PM};
    private int[] to = new int[]{R.id.txtIdAlarm, R.id.txtJamAlarm,
            R.id.txtMenitAlarm, R.id.txtSistemAlarm};

    DataAlarm dataAlarm;
    AlarmManager alarmManager;
    SensorManager sensorManager;
    private int idSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        ShakeDetector shakeDetector = new ShakeDetector(this);
        shakeDetector.start(sensorManager);
        shakeDetector.setSensitivity(ShakeDetector.SENSITIVITY_MEDIUM);

        //Data Alarm
        dataAlarm = new DataAlarm(MainActivity.this, "read");

        listAlarm = (ListView) findViewById(R.id.listAlarm);
        fabSetAlarm = (FloatingActionButton) findViewById(R.id.fabSetAlarm);

        listAlarm.setEmptyView(findViewById(R.id.txtEmpty));

        showAlarmData();

        listAlarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView id_alarm = (TextView) view.findViewById(R.id.txtIdAlarm);

                String idAlarm = id_alarm.getText().toString();

                Intent intent = new Intent(MainActivity.this, UpdateAlarmActivity.class);
                intent.putExtra("id alarm", idAlarm);

                startActivity(intent);
            }
        });

        fabSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewAlarm(v);
            }
        });

    }

    @Override
    public void hearShake() {

        Toast.makeText(MainActivity.this,
                "Alarm turned off", Toast.LENGTH_SHORT).show();

        setAlarmTime(getIdSelected(), "Off");
    }

    private void setIdSelected(int id) {
        this.idSelected = id;
    }

    private int getIdSelected() {
        return this.idSelected;
    }

    private void setNewAlarm(View view) {
        Intent new_alarm = new Intent(MainActivity.this, AlarmActivity.class);
        startActivity(new_alarm);
    }

    public void setAlarmButton(View view) {
        RelativeLayout rv = (RelativeLayout) view.getParent().getParent().getParent();

        TextView id = (TextView) rv.findViewById(R.id.txtIdAlarm);
        TextView jam = (TextView) rv.findViewById(R.id.txtJamAlarm);
        TextView menit = (TextView) rv.findViewById(R.id.txtMenitAlarm);
        TextView sistem = (TextView) rv.findViewById(R.id.txtSistemAlarm);

        String idAlarm = id.getText().toString();
        String hour = jam.getText().toString();
        String minute = menit.getText().toString();
        String system = sistem.getText().toString();

        setIdSelected(Integer.parseInt(idAlarm));

        Toast.makeText(MainActivity.this,
                "Alarm set to " + hour + ":"
                        + minute + " " + system, Toast.LENGTH_SHORT).show();

        setAlarmTime(Integer.parseInt(idAlarm), "On");
    }

    private void showAlarmData() {
        try {
            Cursor cursor = dataAlarm.readAlarmData();

            adapter = new SimpleCursorAdapter(MainActivity.this, R.layout.alarm_list_style,
                    cursor, from, to, 0);
            adapter.notifyDataSetChanged();

            listAlarm.setAdapter(adapter);
        } catch (Exception e) {

        }
    }

    public void setAlarmTime(int id, String purpose) {
        long alarm_milisecond;

        Intent my_intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent;

        try {
            Cursor cursor = dataAlarm.getAlarmDataById(id);

            if(cursor.getCount() > 0) {
                while(cursor.moveToNext()) {
                    String hour = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_JAM_ALARM));
                    String minute = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_MENIT_ALARM));
                    String sistem = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_SISTEM_AM_PM));
                    String ringtone = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_RINGTONE_ALARM));

                    String jam = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_JAM_TEXT));
                    String menit = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_MENIT_TEXT));

                    int hour_alarm = Integer.parseInt(hour);
                    int minute_alarm = Integer.parseInt(minute);
                    int sistem_alarm = setSistemAM_PM(hour_alarm);

                    Log.e("hour:minute sistem", hour_alarm + ":" + minute_alarm + " " + sistem_alarm);

                    Calendar calendar = Calendar.getInstance();

                    calendar.set(Calendar.MINUTE, minute_alarm);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.AM_PM, sistem_alarm);

                    if(calendar.getTimeInMillis() < System.currentTimeMillis()) {
                        alarm_milisecond = calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY;
                    } else {
                        alarm_milisecond = calendar.getTimeInMillis();
                    }

                    switch (ringtone) {
                        case "Your Name" :
                            my_intent.putExtra("song","Your Name");
                            break;
                        case "Blue Water":
                            my_intent.putExtra("song","Blue Water");
                            break;
                        case "Hikari" :
                            my_intent.putExtra("song","Hikari");
                            break;
                        case "Funk" :
                            my_intent.putExtra("song" ,"Funk");
                            break;
                        case "Morning" :
                            my_intent.putExtra("song", "Morning");
                            break;
                        default:
                            //nothing
                            break;
                    }

                    if(purpose == "On") {
                        my_intent.putExtra("switch", "alarm on");
                        my_intent.putExtra("hour", jam);
                        my_intent.putExtra("minute", menit);
                        my_intent.putExtra("sistem", sistem);

//                        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
//                                my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        pendingIntent = getDistinctPendingIntent(my_intent, id);

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm_milisecond, pendingIntent);
                        } else {
                            alarmManager.set(AlarmManager.RTC_WAKEUP, alarm_milisecond, pendingIntent);
                        }
                    } else if(purpose == "Off") {
                        my_intent.putExtra("switch", "alarm off");
                        my_intent.putExtra("hour", jam);
                        my_intent.putExtra("minute", menit);
                        my_intent.putExtra("sistem", sistem);

//                        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,
//                                my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        pendingIntent = getDistinctPendingIntent(my_intent, id);

                        alarmManager.cancel(pendingIntent);
                        //stop the ringtone
                        sendBroadcast(my_intent);
                    }

                }
            }
        } catch(Exception e) {
            Toast.makeText(MainActivity.this, "Set Alarm Error", Toast.LENGTH_SHORT).show();
        }
    }

    private int setSistemAM_PM(int hour) {
        int am_or_pm;

        if(hour == 0) {
            am_or_pm = Calendar.AM;
        } else if(hour < 12) {
            am_or_pm = Calendar.AM;
        } else if(hour == 12) {
            am_or_pm = Calendar.PM;
        } else {
            am_or_pm = Calendar.PM;
        }

        return am_or_pm;
    }

    private PendingIntent getDistinctPendingIntent(Intent intent, int requestId) {
        PendingIntent pi = PendingIntent.getBroadcast(
                MainActivity.this,
                requestId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pi;
    }


}
