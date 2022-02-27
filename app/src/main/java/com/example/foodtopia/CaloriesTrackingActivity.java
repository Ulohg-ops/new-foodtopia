package com.example.foodtopia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

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

import java.util.ArrayList;
import java.util.List;

public class CaloriesTrackingActivity extends AppCompatActivity {

    BarChart mBarChart;
    String TAG = "fefe";

    ArrayList<BarEntry> barValues = new ArrayList<>();
    List<String> axisLabel = new ArrayList<>();
    XAxis xAxis;
    YAxis leftAxis;
    YAxis rightAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories_tracking);

        mBarChart = findViewById(R.id.chart);

        loadChart();


    }

    private void loadChart() {
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.getDescription().setEnabled(false);
        mBarChart.setPinchZoom(false);
        mBarChart.setDrawGridBackground(false);
        mBarChart.setDragEnabled(true);
        mBarChart.setScaleEnabled(false);
        mBarChart.setPinchZoom(false);
        mBarChart.setDoubleTapToZoomEnabled(false);
        mBarChart.setDrawBorders(false);
        mBarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                Log.e(TAG, "onValueSelected: "+e);
                Log.e(TAG, "data" + axisLabel.get((int) e.getX()));
                mBarChart.centerViewToAnimated(e.getX(), e.getY(),
                        mBarChart.getData().getDataSetByIndex(h.getDataSetIndex()).getAxisDependency(), 200);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        mBarChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                MPPointD pointD = MPPointD.getInstance(0, 0);
                Transformer transformer = mBarChart.getTransformer(YAxis.AxisDependency.LEFT);
                transformer.getValuesByTouchPoint(me.getX(), me.getY(), pointD);

                int roundX = round((float) pointD.x);
                int roundY = round((float) pointD.y);
                mBarChart.centerViewToAnimated(roundX, roundY, YAxis.AxisDependency.LEFT, 2000);

                Utils.selectedIndex = roundX;
                showSelectedInfo(roundX);
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });

        ValueFormatter xAxisFormatter = new IndexAxisValueFormatter(axisLabel);

        xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(16f);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);


        leftAxis = mBarChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(1f); // this replaces setStartAtZero(true)
        leftAxis.setEnabled(false);

        rightAxis = mBarChart.getAxisRight();
        rightAxis.setDrawGridLines(true);
        rightAxis.setLabelCount(2, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(1f); // this replaces setStartAtZero(true)
        rightAxis.setCenterAxisLabels(true);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        rightAxis.enableGridDashedLine(4, 3, 0);
        rightAxis.setGridColor(ContextCompat.getColor(this, R.color.bar_color));
        rightAxis.setDrawLimitLinesBehindData(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setDrawAxisLine(false);

        //todo:setDataOne()
    }
}