package com.example.foodtopia.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.foodtopia.Adpater.MyFotosAdapter;
import com.example.foodtopia.R;
import com.example.foodtopia.social.MypostFragment;
import com.example.foodtopia.social.MysaveFragment;
import com.google.android.material.tabs.TabLayout;

public class SavedActivity extends AppCompatActivity {
    //    ImageView image_profile, options;
//    private List<String> mySaves;
//
//    FirebaseUser firebaseUser;
//    String profileid;
//
//    private RecyclerView recyclerView;
//    private MyFotosAdapter myFotosAdapter;
//    private List<Post> postList;
//
//    private RecyclerView recyclerView_saves;
//    private MyFotosAdapter myFotosAdapter_saves;
//    private List<Post> postList_saves;
//
//    ImageButton my_fotos, saved_fotos;
    private TabLayout tableLayout;
    private ViewPager viewPager;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_saved);
        back=findViewById(R.id.back);
        tableLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.frame);
        tableLayout.setupWithViewPager(viewPager);
        MyFotosAdapter.VPAdapter vpAdapter=new MyFotosAdapter.VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new MypostFragment(),"我的食譜");
        vpAdapter.addFragment(new MysaveFragment(),"收藏食譜");
        viewPager.setAdapter(vpAdapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        my_fotos = findViewById(R.id.my_fotos);
//        saved_fotos = findViewById(R.id.saved_fotos);
//        recyclerView = findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
//
//
//        recyclerView.setLayoutManager(mLayoutManager);
//        postList = new ArrayList<>();
//        myFotosAdapter = new MyFotosAdapter(this, postList);
//        recyclerView.setAdapter(myFotosAdapter);
//
//        recyclerView_saves = findViewById(R.id.recycler_view_save);
//        recyclerView_saves.setHasFixedSize(true);
//        LinearLayoutManager mLayoutManagers = new GridLayoutManager(this, 3);
//
//
//        recyclerView_saves.setLayoutManager(mLayoutManagers);
//        postList_saves = new ArrayList<>();
//        myFotosAdapter_saves = new MyFotosAdapter(this, postList_saves);
//        recyclerView_saves.setAdapter(myFotosAdapter_saves);
//
//        recyclerView.setVisibility(View.VISIBLE);
//        recyclerView_saves.setVisibility(View.GONE);
//
//        myFotos();
//        mySaves();
//
//
//        my_fotos.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recyclerView.setVisibility(View.VISIBLE);
//                recyclerView_saves.setVisibility(View.GONE);
//            }
//        });
//
//        saved_fotos.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recyclerView.setVisibility(View.GONE);
//                recyclerView_saves.setVisibility(View.VISIBLE);
//            }
//        });


    }
//    private void myFotos(){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                postList.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Post post = snapshot.getValue(Post.class);
//                    if (post.getPublisher().equals(firebaseUser.getUid())){
//                        postList.add(post);
//                    }
//                }
//                Collections.reverse(postList);
//                myFotosAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

//    private void mySaves()  {
//        mySaves = new ArrayList<>();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saves").child(firebaseUser.getUid());
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    mySaves.add(snapshot.getKey());
//                }
//                readSaves();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

//    private void readSaves(){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                postList_saves.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Post post = snapshot.getValue(Post.class);
//
//                    for (String id : mySaves) {
//                        if (post.getPostid().equals(id)) {
//                            postList_saves.add(post);
//                        }
//                    }
//                }
//                myFotosAdapter_saves.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
}