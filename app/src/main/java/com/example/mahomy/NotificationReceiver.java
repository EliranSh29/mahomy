// NotificationReceiver.java

package com.example.mahomy;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notification_id", 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Build your notification
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context, "channel_id")
                    .setContentTitle("mahomey")
                    .setContentText("Great to see you again!")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .build();
        }

        // Show the notification
        notificationManager.notify(notificationId, notification);
    }
}
