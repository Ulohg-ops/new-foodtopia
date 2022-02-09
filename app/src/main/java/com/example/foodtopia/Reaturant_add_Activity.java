package com.example.foodtopia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Reaturant_add_Activity extends AppCompatActivity {
    TextView name;
    String store_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaturant_add);
        name = findViewById(R.id.txtStoreName);
//        System.out.println(name);
        store_name = getIntent().getStringExtra("name");
        name.setText(store_name);
    }
}