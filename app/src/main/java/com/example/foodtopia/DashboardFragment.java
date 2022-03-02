package com.example.foodtopia;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodtopia.Model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.security.auth.callback.Callback;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


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
                for (DataSnapshot record : snapshot.getChildren()) {
                    float calories = Float.valueOf(record.child("calories").getValue(String.class));
                    total += calories;
                }
                float finalTotal = total;
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String calories_per_day = snapshot.child("calories_per_day").getValue().toString();
                        float cpd = Float.valueOf(calories_per_day);

                        if (finalTotal > cpd) {
                            int progress_red = Color.rgb(237, 122, 107);
                            circularProgressBar.setProgressWithAnimation(cpd);
                            circularProgressBar.setProgressBarColor(progress_red);

                            //set text inside the circular
                            String text = "-" + (finalTotal-cpd) + "\n" + "kcal";
                            calories.setText(text);


                        }else {
                            int color = Color.rgb(90, 106, 207);
                            circularProgressBar.setProgressBarColor(color);
                            circularProgressBar.setProgressWithAnimation(finalTotal);
                            circularProgressBar.setProgressMax(cpd);

                            //set text inside the circular
                            String text = finalTotal + "\n" + "kcal";
                            calories.setText(text);
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


}