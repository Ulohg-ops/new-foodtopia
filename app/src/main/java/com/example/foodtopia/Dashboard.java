package com.example.foodtopia;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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


public class Dashboard extends Fragment {

    private static final String TAG = "variable";

    public Dashboard() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    // retrieve data from firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CircularProgressBar circularProgressBar;
    TextView calories;

    //todo: need to change the path.
    DocumentReference doc = db.collection("test_user").document("fXnfzXMPoQl4fmzqcNVK");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        circularProgressBar = view.findViewById(R.id.circularProgressBar);
        calories = view.findViewById(R.id.calories);

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String calories_per_day = (String) document.get("calories_per_day");
                        String calories_taken = (String) document.get("calories_taken");
                        float cpd = Float.valueOf(calories_per_day);
                        float ct = Float.valueOf(calories_taken);

                        //set circular progress bar
                        circularProgressBar.setProgress(ct);
                        circularProgressBar.setProgressMax(cpd);

                        //set text inside the circular
                        String text = calories_taken + "\n" + "kcal";
                        calories.setText(text);
                    } else {
                        Log.d(TAG, "No doc.");
                    }
                }else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        return view;
    }

}