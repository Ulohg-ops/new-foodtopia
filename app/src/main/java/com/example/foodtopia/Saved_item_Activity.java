package com.example.foodtopia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Saved_item_Activity extends AppCompatActivity {
    //    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_item);
//         String a= prefs.getString("postid","");
//        System.out.println(a);
        Intent intent = getIntent();
        String id = intent.getStringExtra("postid");
        System.out.println(id);
    }
}