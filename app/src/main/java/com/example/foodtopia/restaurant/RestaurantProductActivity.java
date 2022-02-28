package com.example.foodtopia.restaurant;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtopia.Adpater.ProductsCategoryTitleAdapter;
import com.example.foodtopia.Model.AddTitle;
import com.example.foodtopia.Model.Post;
import com.example.foodtopia.Model.RestaurantProduct;
import com.example.foodtopia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RestaurantProductActivity extends AppCompatActivity {

    RecyclerView mainCategoryRecycler;
    ProductsCategoryTitleAdapter categoryTitleAdapter;
    FloatingActionButton back;
    String store_name;
    TextView name;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_stores);

        back=findViewById(R.id.add_menu_back_fab);

        name = findViewById(R.id.txtStoreName);
        store_name = getIntent().getStringExtra("name");
        name.setText(store_name);

        back.setOnClickListener(new View.OnClickListener() {//set the return btn
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        List<RestaurantProduct.AllCategory> allCategoryList = new ArrayList<>();
        AddTitle addTitle=new AddTitle();
        List<List<String>> title =addTitle.getTitles();

        for(List<String> lists : title){
            for(String items : lists){
                System.out.println(items);
                List<Post.CategoryItem> itemList = new ArrayList<>();
                if(items.contains(store_name.toLowerCase(Locale.ROOT))){
                    System.out.println(items);
                    allCategoryList.add(new RestaurantProduct.AllCategory(items, itemList));
                    getData(itemList, items);
                }

            }
        }
        setMainCategoryRecycler(allCategoryList);

    }

    private void setMainCategoryRecycler(List<RestaurantProduct.AllCategory> allCategoryList) {

        mainCategoryRecycler = findViewById(R.id.RVparent);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mainCategoryRecycler.setLayoutManager(layoutManager);
        categoryTitleAdapter = new ProductsCategoryTitleAdapter(this, allCategoryList);
        mainCategoryRecycler.setAdapter(categoryTitleAdapter);

    }


    private void getData(List<Post.CategoryItem> categoryItems, String collection) {
        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getString("name"));
                                categoryItems.add(new Post.CategoryItem(
                                         document.getString("name"),
                                         document.getString("calories"),
                                         document.getString("carbohydrate"),
                                         document.getString("fat"),
                                         document.getString("protein"),
                                         document.getString("saturated_fat"),
                                         document.getString("unsaturatedfat"),
                                         document.getString("sodium"),
                                         document.getString("sugar")));
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        categoryTitleAdapter.notifyDataSetChanged();
                    }
                });

    }


}