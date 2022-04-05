package com.example.foodtopia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtopia.Adpater.RecipePostAdapter;
import com.example.foodtopia.Adpater.ReminderRecycleAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReminderActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    private FloatingActionButton add_poster;
    private RecyclerView recyclerView;
    private RecipePostAdapter recipePostAdapter;
    private List<Remind> remindList = new ArrayList<>();
    private ReminderRecycleAdapter reminderRecycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        recyclerView = findViewById(R.id.recycler_view);

        add_poster = findViewById(R.id.add_poster);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reminderRecycleAdapter = new ReminderRecycleAdapter(this,remindList);
        recyclerView.setAdapter(reminderRecycleAdapter);
        readRemind();

        add_poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReminderActivity.this, ReminderAddActivity.class);
                startActivity(intent);

            }
        });


    }

    private void readRemind() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Remind");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                remindList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Remind remind = snapshot.getValue(Remind.class);
                    if (remind.getUserid().equals(firebaseUser.getUid())){
                        remindList.add(remind);
                    }
                }
                reminderRecycleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}