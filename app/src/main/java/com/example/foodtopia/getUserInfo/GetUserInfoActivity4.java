package com.example.foodtopia.getUserInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.foodtopia.MainActivity;
import com.example.foodtopia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class GetUserInfoActivity4 extends AppCompatActivity {

    Button btn_next;
    ImageButton btn_back;
    RadioGroup radioGroup;
    RadioButton radioButton;
    FirebaseAuth auth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_info4);
        btn_back=findViewById(R.id.btn_back);
        btn_next=findViewById(R.id.btn_next);
        radioGroup=findViewById(R.id.radioGroup);


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String userID = firebaseUser.getUid();
                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                HashMap<String, Object> map = new HashMap<>();
                map.put("target", radioButton.getText());
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

        Intent intent = new Intent(this, GetUserInfoActivity3.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
    public void moveToNext() {

        Intent intent = new Intent(this, GetUserInfoActivity5.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}