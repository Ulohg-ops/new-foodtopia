package com.example.foodtopia;

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

import com.example.foodtopia.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class ReminderEditActivity extends AppCompatActivity {
    DatabaseReference reference;
    FirebaseAuth auth;
    MaterialTimePicker picker;
    Button post, editTime;
    Calendar calendar;
    EditText medicine, time, hint;
    ImageButton back, delete;
    ActivityMainBinding binding;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_edit);
        createNotificationChannel();
        Intent intent = getIntent();
        final String remindID = intent.getStringExtra("remindID");
        post = findViewById(R.id.post);
        back = findViewById(R.id.back);
        time = findViewById(R.id.time);
        medicine = findViewById(R.id.medicineName);
        hint = findViewById(R.id.hint);
        editTime = findViewById(R.id.btnTime);
        delete = findViewById(R.id.delete);
        getRemind(remindID);


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Remind")
                        .child(remindID).removeValue();
                finish();
            }
        });

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
                    String TxtTime = time.getText().toString();
                    String Txtmedicine = medicine.getText().toString();
                    String TxtDes = hint.getText().toString();
                    reference = FirebaseDatabase.getInstance().getReference().child("Remind").child(remindID);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("time", TxtTime);
                    map.put("medicine", Txtmedicine);
                    map.put("des", TxtDes);
                    reference.updateChildren(map);
                    Intent intent = new Intent(ReminderEditActivity.this, ReminderActivity.class);
                    startActivity(intent);
            }
        });
    }

    public void getRemind(String remindId) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Remind").child(remindId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             try{
                 Remind remind = snapshot.getValue(Remind.class);
                 hint.setText(remind.getDes());
                 time.setText(remind.getTime());
                 medicine.setText(remind.getMedicine());
             }catch (Exception e){
             }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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


        reference.child(remindID).setValue(remind).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });


    }

    private void setAlarm() {

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_SHORT).show();
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
            CharSequence name = "foxandroidReminderChannel";
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("foxandroid", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }


    }
}