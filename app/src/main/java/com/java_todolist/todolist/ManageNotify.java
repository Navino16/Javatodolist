package com.java_todolist.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jaunet_n on 31/01/2018.
 * Manage Notifications
 */

class ManageNotify {
    private final Context mContext;

    public ManageNotify(Context context) {
        this.mContext = context;
    }

    public void addNotification(long when, String title, String message, int id) {
        Intent intent = new Intent(mContext.getApplicationContext(), NotificationReceiver.class);
        intent.putExtra(NotificationReceiver.NOTIFICATION_TITLE, title);
        intent.putExtra(NotificationReceiver.NOTIFICATION_TEXT, message);
        intent.putExtra(NotificationReceiver.NOTIFICATION_ID, id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext.getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null)
            alarmManager.set(AlarmManager.RTC_WAKEUP, when, pendingIntent);
    }

    public void StopNotification(int id) {
        Intent myIntent = new Intent(mContext, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, id, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null)
            alarmManager.cancel(pendingIntent);
    }
}
