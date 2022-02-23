package com.example.foodtopia.getUserInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.foodtopia.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GetUserInfoActivity1 extends AppCompatActivity {

    Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_info1);
        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNext();
            }
        });
    }


    public void moveToNext(){

        Intent intent=new Intent(this,GetUserInfoActivity2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

}