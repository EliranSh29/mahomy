package com.example.mahomy;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mahomy.gameSeqActivity;
import com.example.mahomy.MainActivity;
import com.example.mahomy.NumberMemory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OpeningScrrActivity extends AppCompatActivity {

    Button m_buttonGame1;
    Button m_buttonGame2;
    Button m_buttonGame3;
    Button m_myRecordsButton; // Added My Records button reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_scrr);

        createNotificationChannel(); // Create the notification channel
        scheduleNotification(); // Schedule the notification

        // Write a message to the DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("My name is patrick");

        m_buttonGame1 = findViewById(R.id.button_game1);
        m_buttonGame2 = findViewById(R.id.button_game2);
        m_buttonGame3 = findViewById(R.id.button_game3);
        m_myRecordsButton = findViewById(R.id.myRecordsButton); // Initialize My Records button

        m_buttonGame1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the first mini game activity
                Intent intent = new Intent(OpeningScrrActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        m_buttonGame2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the second mini game activity
                Intent intent = new Intent(OpeningScrrActivity.this, gameSeqActivity.class);
                startActivity(intent);
            }
        });

        m_buttonGame3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the third mini game activity (for future use)
                Intent intent = new Intent(OpeningScrrActivity.this, NumberMemory.class);
                startActivity(intent);
            }
        });

        m_myRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start MyRecordsActivity
                Intent intent = new Intent(OpeningScrrActivity.this, MyRecordsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Notification Channel";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void scheduleNotification() {


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        notificationIntent.putExtra("notification_id", 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = System.currentTimeMillis() + 10000; // 10 seconds from now
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
    }
}
