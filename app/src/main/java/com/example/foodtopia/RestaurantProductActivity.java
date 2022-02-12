package com.example.foodtopia;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtopia.Adpater.MainRecyclerAdapter;
import com.example.foodtopia.model.AllCategory;
import com.example.foodtopia.model.CategoryItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RestaurantProductActivity extends AppCompatActivity {

    RecyclerView mainCategoryRecycler;
    MainRecyclerAdapter mainRecyclerAdapter;
    List<CategoryItem> categoryItemList = new ArrayList<>();
    List<CategoryItem> categoryItemList2 = new ArrayList<>();

    String store_name;
    TextView name;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturantproduct);

        name = findViewById(R.id.txtStoreName);
        store_name = getIntent().getStringExtra("name");
        name.setText(store_name);


        List<AllCategory> allCategoryList = new ArrayList<>();
        allCategoryList.add(new AllCategory("麥當勞食物", categoryItemList));
        allCategoryList.add(new AllCategory("麥當勞飲料", categoryItemList2));

        getDatav2(categoryItemList2);
        getDatav1(categoryItemList);
        setMainCategoryRecycler(allCategoryList);

    }

    private void setMainCategoryRecycler(List<AllCategory> allCategoryList){

        mainCategoryRecycler = findViewById(R.id.RVparent);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mainCategoryRecycler.setLayoutManager(layoutManager);
        mainRecyclerAdapter = new MainRecyclerAdapter(this, allCategoryList);
        mainCategoryRecycler.setAdapter(mainRecyclerAdapter);

    }
    private void getDatav2( List<CategoryItem> categoryItems) {
        db.collection("mcdonalds_beverage")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getString("name"));
                                categoryItems.add(new CategoryItem(document.getString("name")));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        mainRecyclerAdapter.notifyDataSetChanged();
                    }
                });

    }

    private void getDatav1( List<CategoryItem> categoryItems) {
        db.collection("mcdonalds_food")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getString("name"));
                                categoryItems.add(new CategoryItem(document.getString("name")));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        mainRecyclerAdapter.notifyDataSetChanged();
                    }
                });

    }
}