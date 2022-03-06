package com.example.foodtopia;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.sql.Date;
import java.util.concurrent.TimeUnit;


public class BarChartXAxisValueFormatter  extends IndexAxisValueFormatter {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String getFormattedValue(float value) {

        // Convert float value to date string
        // Convert from days back to milliseconds to format time  to show to the user
        long emissionsMilliSince1970Time = TimeUnit.DAYS.toMillis((long)value);
        // Show time in local version
        Date timeMilliseconds = new Date(emissionsMilliSince1970Time);
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM-dd");
        System.out.println("datetime"+dateTimeFormat.format(timeMilliseconds));
        return dateTimeFormat.format(timeMilliseconds);
    }
}