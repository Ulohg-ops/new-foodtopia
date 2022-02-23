package com.example.foodtopia.getUserInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.foodtopia.MainActivity;
import com.example.foodtopia.R;

public class GetUserInfoActivity4 extends AppCompatActivity {

    Button btn_next;
    ImageButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_info4);
        btn_back=findViewById(R.id.btn_back);
        btn_next=findViewById(R.id.btn_next);

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

        Intent intent = new Intent(this, GetUserInfoActivity2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
    public void moveToNext() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}