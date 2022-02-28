package com.example.foodtopia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CaloriesTrackingActivity extends AppCompatActivity {

    BarChart chart;
    FloatingActionButton back_btn;
    FirebaseAuth fAuth;
    DatabaseReference reference;
    Query allRecordFromUser;

    private final String TAG = "test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories_tracking);
        getChartData();

        // back button
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // set data
    private List<Entry> getChartData() {

        // get user id
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = fAuth.getCurrentUser();
        String userID = firebaseUser.getUid();


        reference = FirebaseDatabase.getInstance().getReference("Diets");
        allRecordFromUser = reference.orderByChild("userid").equalTo(userID);

        // get user data
        allRecordFromUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot record : snapshot.getChildren()) {
                    // iterate through all record with the same user
                    String calories = record.child("calories").getValue(String.class);
                    Log.d(TAG, calories);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        List<Entry> chartData = new ArrayList<>();
        for (int i=0; i<31; i++) {
            chartData.add(new Entry(i*2, i));
        }
        return chartData;
    }


}