package com.example.foodtopia;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.foodtopia.add.Diet;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import com.google.firebase.auth.FirebaseAuth;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private static final String TAG = "variable";

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // retrieve data from firebase
    CircularProgressBar circularProgressBar;
    TextView calories;
    AppCompatButton tracking_btn;
    ProgressBar carbonProgressbar;
    ProgressBar proteinProgressBar;
    ProgressBar fatProgressBar;
    TextView carbonTextView;
    TextView proteinTextView;
    TextView fatTextView;

    DatabaseReference mDatabase;
    DatabaseReference reference;
    FirebaseAuth fAuth;
    Query allRecordFromUser;

    // get the date
    String date = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        circularProgressBar = view.findViewById(R.id.circularProgressBar);
        calories = view.findViewById(R.id.calories);
        tracking_btn = view.findViewById(R.id.tracking_btn);
        carbonProgressbar = view.findViewById(R.id.carbonProgressBar);
        proteinProgressBar = view.findViewById(R.id.proteinProgressBar);
        fatProgressBar = view.findViewById(R.id.fatProgressBar);
        carbonTextView = view.findViewById(R.id.carbonTextView);
        proteinTextView = view.findViewById(R.id.proteinTextView);
        fatTextView = view.findViewById(R.id.fatTextView);


        // get user id
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = fAuth.getCurrentUser();
        String userID = firebaseUser.getUid();

        String user_date = String.format("%s_%s", userID, date);

        mDatabase = FirebaseDatabase.getInstance().getReference("Diets");
        allRecordFromUser = mDatabase.orderByChild("userid_date").equalTo(user_date);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        // query
        allRecordFromUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float total = 0;
                float carbonTotal = 0;
                float proteinTotal = 0;
                float fatTotal = 0;

                if (snapshot.exists()){
                    for (DataSnapshot record : snapshot.getChildren()){
                        Diet diet = record.getValue(Diet.class);
                        if (!diet.getCalories().matches("")){
                            total += Float.parseFloat(diet.getCalories());
                        }
                        if (!diet.getCarbohydrate().matches("")){
                            carbonTotal += Float.parseFloat(diet.getCarbohydrate());
                        }
                        if (!diet.getProtein().matches("")){
                            proteinTotal += Float.parseFloat(diet.getProtein());
                        }
                        if (!diet.getFat().matches("")){
                            fatTotal += Float.parseFloat(diet.getFat());
                        }
                    }

                }
                float finalTotal = round(total);
                float carbonFinalTotal = carbonTotal;
                float proteinFinalTotal = proteinTotal;
                float fatFinalTotal = fatTotal;

                reference.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String calories_per_day = snapshot.child("calories_per_day").getValue().toString();
                        float cpd = Float.valueOf(calories_per_day);
                        cpd = round(cpd);
                        if (finalTotal > cpd) {
                            int progress_red = Color.rgb(237, 122, 107);
                            circularProgressBar.setProgressWithAnimation(cpd);

                            //set text inside the circular
                            String text = "超出" + "\n" + (finalTotal-cpd) + "\n" + "kcal";
                            calories.setText(text);
                            calories.setTextColor(progress_red);
                        }else {
                            int color = Color.rgb(90, 106, 207);
                            circularProgressBar.setProgressBarColor(color);
                            circularProgressBar.setProgressWithAnimation(finalTotal);
                            circularProgressBar.setProgressMax(cpd);

                            //set text inside the circular
                            String text = finalTotal + "\n" + "kcal";
                            calories.setText(text);
                        }

                        Log.d(TAG, "carbon=" + carbonFinalTotal);
                        //碳水化合物
                        float carbonPD = Float.valueOf(calories_per_day)/8;
                        if (carbonFinalTotal >= carbonPD) {
                            carbonProgressbar.setProgress(100);
//                            carbonProgressbar.setMax((int)carbonPD);
                            //顯示文字
                            String text = "超出" + (Math.round(carbonFinalTotal-carbonPD)) + "g";
                            carbonTextView.setText(text);
                            carbonTextView.setTextColor(Color.rgb(237, 122, 107));
                        }else {
//                            carbonProgressbar.setMax((int)carbonPD);
                            carbonProgressbar.setProgress((int)(carbonFinalTotal/carbonPD * 100));
                            //顯示文字
                            String text = "剩餘" + Math.round(carbonPD-carbonFinalTotal) + "g";
                            carbonTextView.setText(text);
                        }
                        //蛋白質
                        float proteinPD = (float)(Float.valueOf(calories_per_day) * 0.25 * 0.3);
                        if (proteinFinalTotal >= proteinPD) {
                            proteinProgressBar.setProgress(100);
                            //顯示文字
                            String text = "超出" + Math.round(proteinFinalTotal-proteinPD) + "g";
                            proteinTextView.setText(text);
                            proteinTextView.setTextColor(Color.rgb(237, 122, 107));
                        }else {
                            proteinProgressBar.setProgress((int)(proteinFinalTotal/proteinPD * 100));
//                            proteinProgressBar.setMax((int)proteinPD);
                            //顯示文字
                            String text = "剩餘" + Math.round(proteinPD-proteinFinalTotal) + "g";
                            proteinTextView.setText(text);
                        }
                        //脂肪
                        float fatPD = (float)(Float.valueOf(calories_per_day) * 0.2 / 9);
                        if (fatFinalTotal >= fatPD) {
                            fatProgressBar.setProgress(100);
                            //顯示文字
                            String text = "超出" + Math.round(fatFinalTotal-fatPD) + "g";
                            fatTextView.setText(text);
                            fatTextView.setTextColor(Color.rgb(237, 122, 107));
                        }else {
                            fatProgressBar.setProgress((int)(fatFinalTotal/fatPD * 100));
//                            fatProgressBar.setMax((int)fatPD);
                            //顯示文字
                            String text = "剩餘" + Math.round(fatPD-fatFinalTotal) + "g";
                            fatTextView.setText(text);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG, "loadPost:onCancelled", error.toException());
                    }
                });
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tracking_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CaloriesTrackingActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    // 取兩位小數
    public static float round(float d) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

}