package com.example.foodtopia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AddCaloriesCheckActivity extends AppCompatActivity {
    TextView foodName;
    String food_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_calories_check);
        foodName = findViewById(R.id.foodName);
        food_name = getIntent().getStringExtra("name");
        foodName.setText(food_name);
    }
}