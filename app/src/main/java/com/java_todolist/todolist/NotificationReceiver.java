package com.java_todolist.todolist;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by jaunet_n on 31/01/2018.
 * Notification receiver
 */

public class NotificationReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_TITLE = "Title";
    public static final String NOTIFICATION_TEXT = "Text";
    public static final String NOTIFICATION_ID = "id";
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(NOTIFICATION_TITLE);
        String text = intent.getStringExtra(NOTIFICATION_TEXT);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);

        Intent repeating_intent = new Intent(context, MainActivity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, String.valueOf(id))
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null)
            notificationManager.notify(id, builder.build());
    }
}
