package com.example.foodtopia.getUserInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.foodtopia.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class GetUserInfoActivity3 extends AppCompatActivity {
    Button btn_next;
    ImageButton btn_back;
    TextInputLayout height;
    TextInputLayout weight;
    TextInputLayout age;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_info3);
        btn_next = findViewById(R.id.btn_next);
        btn_back = findViewById(R.id.btn_back);

        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        age=findViewById(R.id.age);



        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String userID = firebaseUser.getUid();
                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                HashMap<String, Object> map = new HashMap<>();
                String height_value = height.getEditText().getText().toString().trim();
                String weight_value = weight.getEditText().getText().toString().trim();
                String age_value=age.getEditText().getText().toString().trim();
                map.put("height", height_value);
                map.put("weight", weight_value);
                map.put("age", age_value);
                reference.updateChildren(map);
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

        Intent intent = new Intent(this, GetUserInfoActivity4.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }
}