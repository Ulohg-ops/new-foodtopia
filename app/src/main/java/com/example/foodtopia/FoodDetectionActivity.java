package com.example.foodtopia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoodDetectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detection);

        // start the python
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(FoodDetectionActivity.this));
        }
        Python py = Python.getInstance();
        PyObject module = py.getModule("detect");
        callPython();
    }

    void callPython() {
        Python py = Python.getInstance();
        py.getModule("detect").callAttr("getInfo");
    }

}