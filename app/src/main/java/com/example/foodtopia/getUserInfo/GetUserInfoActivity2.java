package com.example.foodtopia.getUserInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.foodtopia.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GetUserInfoActivity2 extends AppCompatActivity {
    Button btn_next;
    ImageButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_info2);
        btn_next = findViewById(R.id.btn_next);
        btn_back=findViewById(R.id.btn_back);



        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNext();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToBack();
            }
        });


    }

    public void moveToBack() {

        Intent intent = new Intent(this, GetUserInfoActivity1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
    public void moveToNext() {

        Intent intent = new Intent(this, GetUserInfoActivity3.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
}