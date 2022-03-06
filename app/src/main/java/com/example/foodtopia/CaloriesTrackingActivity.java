package com.example.foodtopia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.foodtopia.Model.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class CaloriesTrackingActivity extends AppCompatActivity {

    BarChart chart;
    FloatingActionButton back_btn;
    FirebaseAuth fAuth;
    DatabaseReference reference;
    Query allRecordFromUser;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private final String TAG = "test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories_tracking);

        // back button
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // chart
        chart = findViewById(R.id.chart);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = FirebaseDatabase.getInstance().getReference("Diets")
                .orderByChild("userid").equalTo(user.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HashMap<String, List<String>> map = new HashMap<>();
                for (DataSnapshot record : snapshot.getChildren()) {
//                    System.out.println(record.child("date").getValue().toString());
                    List<String> alternateList = map.get(record.child("date").getValue().toString());
                    if (alternateList == null) {
                        alternateList = new ArrayList<>();
                        map.put(record.child("date").getValue().toString(), alternateList);
                    }
                    alternateList.add(record.child("calories").getValue().toString());
                }

                for (Map.Entry<String, List<String>> alternateEntry : map.entrySet()) {
                    System.out.println(alternateEntry.getKey() + ": " +
                            alternateEntry.getValue().toString());
                }

                HashMap<String, Float> calories_added = new HashMap<>();
//                String a = "568.5";
//                String b = "456.545";
//                String c = "116.545";
//                String d = "416.545";
//                String e = "126.545";
//                String f = "456.545";
//                String g = "116.545";
//                String h = "416.545";
//                String i = "416.545";
//                String j = "456.545";
//                String k = "116.545";
//                String l = "416.545";
//                calories_added.put("20220208", Float.parseFloat(a));
//                calories_added.put("20220209", Float.parseFloat(b));
//                calories_added.put("202202010", Float.parseFloat(c));
//                calories_added.put("202202011", Float.parseFloat(d));
//                calories_added.put("202202211", Float.parseFloat(e));
//                calories_added.put("202202012", Float.parseFloat(f));
//                calories_added.put("202202013", Float.parseFloat(g));
//                calories_added.put("202202014", Float.parseFloat(h));
//                calories_added.put("2022020113", Float.parseFloat(i));
//                calories_added.put("2022020121", Float.parseFloat(j));
//                calories_added.put("2022020134", Float.parseFloat(k));
//                calories_added.put("2022020154", Float.parseFloat(l));

//                int ass = 20220208;

//                HashMap<String, Float> calories_added2 = new HashMap<>();
//                int a = 0;
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    float td_calories_tt = 0;
                    for (String aString : entry.getValue()) {
                        System.out.println("key : " + entry.getKey() + " value : " + aString);
                        td_calories_tt += Float.parseFloat(aString);
                    }
                    calories_added.put(entry.getKey(), td_calories_tt);
//                    a++;
//                    if (a == 8) {
//                        break;
//                    }
                }


                Map<String, Float> sorted_calories_added = new TreeMap(calories_added);


//                for (Map.Entry<String, Float> entry : sorted_calories_added.entrySet()) {
//                    System.out.println(entry.getKey() + "++++" + entry.getValue());
//                }

                //                <------------------------------------------------------------------------>
                ArrayList<String> myList = new ArrayList<>();
                for (Map.Entry<String, Float> entry : sorted_calories_added.entrySet()) {
                myList.add(entry.getKey());
                }

//                myList.add("1");
//                myList.add("2");
//                myList.add("3");
//                myList.add("4");
//                myList.add("5");
//                myList.add("6");
//                myList.add("7");
//                myList.add("8");
//                myList.add("9");
//                myList.add("10");
//                myList.add("11");
//                myList.add("12");
//                myList.add("13");

                XAxis xAxis = chart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setValueFormatter(new BarChartXAxisValueFormatter());
                xAxis.setValueFormatter(new IndexAxisValueFormatter(myList));
                Map<String, Float> test = new TreeMap();

//                for (Map.Entry<String, Float> entry : test.entrySet()) {
//                    System.out.println(entry.getKey() + "/" + entry.getValue());
//                }

                ////
                ArrayList<BarEntry> entries = new ArrayList<>();
                String title = "Calories";
                int ac = 0;
                for (Map.Entry<String, Float> entry : sorted_calories_added.entrySet()) {
                    BarEntry barEntry = new BarEntry(ac++, entry.getValue());
                    entries.add(barEntry);
                }

//
//                for (int i = 0; i < 6; ++i) {
//                    BarEntry entry = new BarEntry(i, i + 1);
//                    entries.add(entry);
//                }

                BarDataSet barDataSet = new BarDataSet(entries, title);

                barDataSet.setColors(ColorTemplate.getHoloBlue());
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);
//
                BarData data = new BarData(barDataSet);
                chart.setFitBars(true);
                chart.setData(data);
                chart.invalidate();
                chart.animateY(2000);
                chart.getDescription().setEnabled(false);

//      <------------------------------------------------------------------------>


                //                for (String str : treeMap.keySet()) {
//                    System.out.println(str);
//                }
////
//                for (Map.Entry<String, Float> entry : map2.entrySet()) {
//                    System.out.println(entry.getKey() + "/" + entry.getValue());
//                }


//                String[] months = {"20220501", "20220601", "20220602", "20220603", "20220604", "20220605"};
//                ArrayList<String> mm = new ArrayList<>();
//                mm.add("ccc");
//                mm.add("ddd");
//                mm.add("eee");
////
//                chart.setDrawBarShadow(false);
//                chart.setDrawValueAboveBar(false);
//                chart.getDescription().setEnabled(false);
//                chart.setDrawGridBackground(false);
//
//                XAxis xaxis = chart.getXAxis();
//                xaxis.setDrawGridLines(false);
//                xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                xaxis.setGranularity(1f);
//                xaxis.setDrawLabels(true);
//                xaxis.setDrawAxisLine(false);
//                xaxis.setValueFormatter(new IndexAxisValueFormatter(mm));
//
//
//                chart.getAxisRight().setEnabled(false);
//
//                Legend legend = chart.getLegend();
//                legend.setEnabled(false);
//
//                ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
//
//                for (int i = 0; i < 6; ++i) {
//                    BarEntry entry = new BarEntry(i, (i + 1) * 10);
//                    valueSet1.add(entry);
//                }
//                for (Map.Entry<String, Float> entry : sorted_calories_added.entrySet()) {
//                    BarEntry barEntry = new BarEntry(TimeUnit.MILLISECONDS.toDays((long)a++), entry.getValue());
//                    entries.add(barEntry);
////                }
//                List<IBarDataSet> dataSets = new ArrayList<>();
//                BarDataSet barDataSet = new BarDataSet(valueSet1, " ");
//                barDataSet.setColor(Color.CYAN);
//                barDataSet.setDrawValues(false);
//                dataSets.add(barDataSet);
//
//                BarData data = new BarData(dataSets);
//                chart.setData(data);
//                chart.invalidate();


                ////---------------ver2
//                HashMap<String,List<String>> map=new HashMap<>();
//                for (DataSnapshot record : snapshot.getChildren()) {
////                    System.out.println(record.child("date").getValue().toString());
//                    List<String> alternateList = map.get(record.child("date").getValue().toString());
//                    if(alternateList==null) {
//                        alternateList=new ArrayList<>();
//                        map.put(record.child("date").getValue().toString(),alternateList);
//                    }
//                    alternateList.add(record.child("calories").getValue().toString());
//                }
//                for(Map.Entry<String, List<String>> alternateEntry : map.entrySet()) {
//                    System.out.println(alternateEntry.getKey() + ": " +
//                            alternateEntry.getValue().toString());
//                }
////---------------ver1
                //                for (DataSnapshot record : snapshot.getChildren()) {
//                    System.out.println(record.child("date").getValue().toString());
//                    List<String> name=new ArrayList<>();
//                    if(!map.containsKey(record.child("date").getValue().toString())){
//                        ArrayList<String> name=new ArrayList<>();
//                        name.add(record.child("calories").getValue().toString());
//                        map.put(record.child("date").getValue().toString(),name);
//                    }else{
//                        map.put(record.child("date").getValue().toString(),
//                                map.get(record.child("date").getValue().
//                                toString()));
//                    }
//                    //System.out.println(record.child("calories")+" "+record.child("userid")+" "+record.child("userid_date"));;
//                }
//                for (Map.Entry<String, ArrayList> entry : map.entrySet()) {
//                    System.out.println("555");
//                    System.out.println(entry.getKey() + "/" + entry.getValue());
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
