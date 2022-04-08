package com.example.foodtopia.reminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodtopia.MainActivity;
import com.example.foodtopia.R;

import rm.com.clocks.ClockImageView;

public class ReminderRecieptActivity extends AppCompatActivity {
    ClockImageView clockImageView;
    TextView textView;
    Button btnAccept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_reciept);
        Intent intent = getIntent();
        System.out.println("medicine_name" + intent.getStringExtra("medicine_name"));
        clockImageView = findViewById(R.id.clock_alarm);
        btnAccept=findViewById(R.id.btnAccept);
        textView = findViewById(R.id.text_view_time_alarm);
        textView.setText(intent.getStringExtra("medicine_name"));

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReminderRecieptActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        clockImageView.setHours(Integer.parseInt(intent.getStringExtra("hour")));
        clockImageView.setMinutes(Integer.parseInt(intent.getStringExtra("minute")));
    }
}