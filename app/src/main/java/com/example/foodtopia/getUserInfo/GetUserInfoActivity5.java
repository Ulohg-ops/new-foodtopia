package com.example.foodtopia.getUserInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.example.foodtopia.MainActivity;
import com.example.foodtopia.Model.User;
import com.example.foodtopia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class GetUserInfoActivity5 extends AppCompatActivity {
    Button btn_next;
    ImageButton btn_back;
    FirebaseAuth auth;
    DatabaseReference reference;
    Spinner workload;
    Spinner stress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_info5);
        btn_back = findViewById(R.id.btn_back);
        btn_next = findViewById(R.id.btn_next);
        workload = findViewById(R.id.spinner_workload);
        stress = findViewById(R.id.spinner_stress);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.work_load, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workload.setAdapter(adapter);


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.stress, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stress.setAdapter(adapter2);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                put_value();
                calc_calories();
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


    public void put_value() {
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        HashMap<String, Object> map = new HashMap<>();
        map.put("workload", workload.getSelectedItem().toString());
        map.put("stress", stress.getSelectedItem().toString());
        map.put("calories_taken", "3000");
        reference.updateChildren(map);
    }

    public void calc_calories() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                double calories_perday=0;
                Double height=Double.parseDouble(user.getHeight());
                Double weight=Double.parseDouble(user.getWeight());
                int age=Integer.parseInt(user.getAge());
                String gender=user.getGender();
                String workload=user.getWorkload();
                double act=0;
                switch (workload.toString()) {
                    case "臥床躺著不動":
                        act = 1.1;
                        break;
                    case "幾乎很少或坐著不動":
                        act = 1.2;
                        break;
                    case "每周1-2次":
                        act = 1.4;
                        break;
                    case "每周3-5次":
                        act = 1.6 ;
                        break;
                    case "每周6-7次":
                        act = 1.8;
                        break;
                    case "每天重度運動":
                        act = 1.9;
                        break;
                    case "重勞力工作者":
                        act = 2;
                        break;
                }
                if(gender.equals("Male")){
                    calories_perday=10*weight+6.25*height-5*age+5;
                    calories_perday*=act;
                    calories_perday=Math.round(calories_perday);
                }else{
                    calories_perday=10*weight+6.25*height-5*age-161;
                    calories_perday*=act;
                    calories_perday=Math.round(calories_perday);
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("calories_per_day",calories_perday+"");
                reference.updateChildren(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//    https://ashleexiu.com/tdee/

    public void moveToBack() {
        Intent intent = new Intent(this, GetUserInfoActivity4.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

    public void moveToNext() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}