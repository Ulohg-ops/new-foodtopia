package com.example.foodtopia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CaloriesTrackingActivity extends AppCompatActivity {

    BarChart chart;
    FloatingActionButton back_btn;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private final String TAG = "test";

//    1.create a map which store <String(date) ,List<String>(calories)>  =>map
//    2.create another map which store <String(date),Float> =>   calories_added
//        and put diet data into this
//    3.using treemap to sort the map
//    4.set XAxis label to date
//    5.put map into entry

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories_tracking);

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        chart = findViewById(R.id.chart);

        Query query = FirebaseDatabase.getInstance().getReference("Diets")
                .orderByChild("userid").equalTo(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HashMap<String, List<String>> map = new HashMap<>();
                for (DataSnapshot record : snapshot.getChildren()) {
                    List<String> alternateList = map.get(record.child("date").getValue().toString());
                    if (alternateList == null) {
                        alternateList = new ArrayList<>();
                        map.put(record.child("date").getValue().toString(), alternateList);
                    }
                    alternateList.add(record.child("calories").getValue().toString());
                }

//                for (Map.Entry<String, List<String>> alternateEntry : map.entrySet()) {
//                    System.out.println(alternateEntry.getKey() + ": " +
//                            alternateEntry.getValue().toString());
//                }

                HashMap<String, Float> calories_added = new HashMap<>();
//-------------------------Test Data---------------------------
                String a = "568.5";
                String b = "456.545";
                String c = "116.545";
                String d = "1423";
//                String d = "416.545";
//                String e = "126.545";
//                String f = "456.545";
//                String g = "116.545";
//                String h = "416.545";
//                String i = "416.545";
//                String j = "456.545";
//                String k = "116.545";
//                String l = "416.545";
                calories_added.put("20220406", Float.parseFloat(a));
                calories_added.put("20220407", Float.parseFloat(b));
                calories_added.put("20220408", Float.parseFloat(c));
//                calories_added.put("20220331", Float.parseFloat(c));
                calories_added.put("20220209", Float.parseFloat(d));
//                calories_added.put("20220205", Float.parseFloat(e));
//                calories_added.put("20220206", Float.parseFloat(f));
//                calories_added.put("20220207", Float.parseFloat(g));
//                calories_added.put("20220208", Float.parseFloat(h));
//                calories_added.put("20220209", Float.parseFloat(i));
//                calories_added.put("20220210", Float.parseFloat(j));
//                calories_added.put("20220211", Float.parseFloat(k));
//                calories_added.put("20220212", Float.parseFloat(l));
//                calories_added.put("20220213", Float.parseFloat(d));
//                calories_added.put("20220214", Float.parseFloat(e));
//                calories_added.put("20220215", Float.parseFloat(f));
//                calories_added.put("20220216", Float.parseFloat(g));
//                calories_added.put("20220217", Float.parseFloat(h));
//                calories_added.put("20220218", Float.parseFloat(i));
//                calories_added.put("20220219", Float.parseFloat(j));
//                calories_added.put("20220220", Float.parseFloat(k));
//                calories_added.put("20220221", Float.parseFloat(l));
//                calories_added.put("20220222", Float.parseFloat(l));
//                calories_added.put("20220223", Float.parseFloat(d));
//                calories_added.put("20220224", Float.parseFloat(e));
//                calories_added.put("20220225", Float.parseFloat(f));
//                calories_added.put("20220226", Float.parseFloat(g));
//                calories_added.put("20220227", Float.parseFloat(h));
//                calories_added.put("20220228", Float.parseFloat(i));
//-------------------------Test Data---------------------------
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    float td_calories_tt = 0;
                    for (String aString : entry.getValue()) {
                        td_calories_tt += Float.parseFloat(aString);
                    }
                    calories_added.put(entry.getKey(), td_calories_tt);
                }

                Map<String, Float> sorted_calories_added = new TreeMap(calories_added);

                ArrayList<String> myList = new ArrayList<>();
                for (Map.Entry<String, Float> entry : sorted_calories_added.entrySet()) {
                    String newString=entry.getKey().substring(4,6)+"/"+entry.getKey().substring(6,8);
                    myList.add(newString);

                }
                XAxis xAxis = chart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(myList));

                ArrayList<BarEntry> entries = new ArrayList<>();
                String title = "Calories";
                int ac = 0;
                for (Map.Entry<String, Float> entry : sorted_calories_added.entrySet()) {
                    BarEntry barEntry = new BarEntry(ac++, entry.getValue());
                    entries.add(barEntry);
                }
                BarDataSet barDataSet = new BarDataSet(entries, title);
                barDataSet.setColors(ColorTemplate.getHoloBlue());
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(10);
                BarData data = new BarData(barDataSet);
                chart.setFitBars(true);
                chart.setData(data);
                chart.invalidate();
                chart.animateY(2000);
                chart.setDragEnabled(true);
                chart.getDescription().setEnabled(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
