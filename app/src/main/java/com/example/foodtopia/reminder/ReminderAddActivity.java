package com.example.foodtopia.reminder;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodtopia.R;
import com.example.foodtopia.model.Remind;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ReminderAddActivity extends AppCompatActivity {
    DatabaseReference reference;
    FirebaseAuth auth;
    MaterialTimePicker picker;
    Button post, editTime;
    Calendar calendar;
    EditText medicine, time, hint;
    ImageButton back;
    int reminderCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_add);
//        createNotificationChannel();
        post = findViewById(R.id.post);
        back = findViewById(R.id.back);
        time = findViewById(R.id.time);
        medicine = findViewById(R.id.medicineName);
        hint = findViewById(R.id.hint);
        editTime = findViewById(R.id.btnTime);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NotificationCompat.Builder builder  =new NotificationCompat.Builder(ReminderAddActivity.this,"My Notification");
//                builder.setContentTitle("My Title");
//                builder.setContentText("Hello world");
//                builder.setSmallIcon(R.drawable.notification_icon);
//                builder.setAutoCancel(true);
//                NotificationManagerCompat managerCompat=NotificationManagerCompat.from(ReminderAddActivity.this);
//                managerCompat.notify(1,builder.build());
//                setAlarm();
                addMyRemind(medicine.getText().toString(), time.getText().toString(), hint.getText().toString());
            }
        });

    }

    public void addMyRemind(String medicine, String time, String hint) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Remind");
        String remindID = reference.push().getKey();
        Remind remind = new Remind(medicine, time, userID, hint, remindID);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ReminderAddActivity.this, ReminderReceiver.class);
        intent.putExtra("msg", medicine);
        intent.putExtra("ren", reminderCount + "");
        System.out.println((int) (System.currentTimeMillis() % 1000000000));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                ReminderAddActivity.this, (int) (System.currentTimeMillis() % 1000000000)
                , intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        reference.child(remindID).setValue(remind).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ReminderAddActivity.this, "提醒設定成功!!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });


    }

    private void setAlarm() {

//            AlarmManager mgrAlarm = (AlarmManager) this.getSystemService(ALARM_SERVICE);
//            Intent intent = new Intent(this, AlarmReceiver.class);
//            intent.putExtra("ccc","msg");
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//            mgrAlarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                    SystemClock.elapsedRealtime(),
//                    pendingIntent);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        intent.putExtra("msg", "456");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, reminderCount++, intent, 0);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_SHORT).show();
    }

    private void showTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .build();

        picker.show(getSupportFragmentManager(), "android");
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (picker.getHour() > 12) {
                    time.setText(
                            String.format("%02d", (picker.getHour() - 12)) + " : " + String.format("%02d", picker.getMinute()) + " PM"
                    );
                } else {

                    time.setText(picker.getHour() + " : " + picker.getMinute() + " AM");

                }
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            }
        });


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            System.out.println("cc12312");
            CharSequence name = "ReminderChannel";
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("android", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}