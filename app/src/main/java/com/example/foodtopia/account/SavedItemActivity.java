package com.example.foodtopia.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.foodtopia.Adpater.PostAdapter;
import com.example.foodtopia.Model.Post;
import com.example.foodtopia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SavedItemActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    String post_id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_saved_detail);

        Intent intent = getIntent();
        post_id = intent.getStringExtra("postid");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList);
        recyclerView.setAdapter(postAdapter);
        readPosts();
    }


    private void readPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if(post_id.equals(post.getPostid())){
                        postList.add(post);
                        break;
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}