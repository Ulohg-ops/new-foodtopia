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

    //todo: need to change the path.
    private DatabaseReference mDatabase;
    FirebaseAuth fAuth;

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

        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String calories_taken = snapshot.child("calories_taken").getValue().toString();
                String calories_per_day = snapshot.child("calories_per_day").getValue().toString();

                float cpd = Float.valueOf(calories_per_day);
                float ct = Float.valueOf(calories_taken);

                if (ct > cpd) {
                    int progress_red = Color.rgb(237, 122, 107);
                    circularProgressBar.setProgressWithAnimation(cpd);
                    circularProgressBar.setProgressBarColor(progress_red);

                    //set text inside the circular
                    String text = "-" + (ct-cpd) + "\n" + "kcal";
                    calories.setText(text);


                }else {
                    int color = Color.rgb(90, 106, 207);
                    circularProgressBar.setProgressBarColor(color);
                    circularProgressBar.setProgressWithAnimation(ct);
                    circularProgressBar.setProgressMax(cpd);

                    //set text inside the circular
                    String text = calories_taken + "\n" + "kcal";
                    calories.setText(text);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
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