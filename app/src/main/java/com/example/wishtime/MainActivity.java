package com.example.wishtime;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary notification_channel";
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent notifyIntent = new Intent(this,AlarmReceiver.class);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast(this,
                NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        ToggleButton alarmToggle = findViewById(R.id.alarmToggle);

        boolean alarmUp = (PendingIntent.getBroadcast(this,NOTIFICATION_ID, notifyIntent,
                PendingIntent.FLAG_NO_CREATE) !=null);

        alarmToggle.setChecked(alarmUp);

        alarmToggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String toastMessage = "Wish Alarm On!";
                        if(isChecked){
                            if(alarmManager !=null){
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR,11);
                                calendar.set(Calendar.MINUTE,11);
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                        notifyPendingIntent);
                                Toast.makeText(MainActivity.this,toastMessage,Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if(alarmManager !=null){
                                alarmManager.cancel(notifyPendingIntent);
                                mNotificationManager.cancelAll();
                            }
                        }
                    }
                }
        );
        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel(){
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, "Make a Wish Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifies when it is 11:11");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
