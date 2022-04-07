package com.example.foodtopia.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.foodtopia.R;
import com.example.foodtopia.reminder.ReminderRecieptActivity;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, ReminderRecieptActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //        PendingIntent pendingIntent =
//                PendingIntent.getActivity(context,2123,i,0);//
//        System.out.println(intent.getStringExtra("msg"));
//        System.out.println(intent.getStringExtra("ren"));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"android")
                .setSmallIcon(R.drawable.reminder_icon_drugs)
                .setContentTitle("用藥提醒")
                .setContentText(intent.getStringExtra("msg"))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,builder.build());


    }
}
