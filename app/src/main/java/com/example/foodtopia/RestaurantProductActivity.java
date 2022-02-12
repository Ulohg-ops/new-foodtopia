package com.example.foodtopia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtopia.Adpater.CategoryTitleAdapter;
import com.example.foodtopia.classes.AddTitle;
import com.example.foodtopia.model.AllCategory;
import com.example.foodtopia.model.CategoryItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RestaurantProductActivity extends AppCompatActivity {

    RecyclerView mainCategoryRecycler;
    CategoryTitleAdapter categoryTitleAdapter;
    FloatingActionButton back;
    List<CategoryItem> categoryItemList = new ArrayList<>();
    List<CategoryItem> categoryItemList2 = new ArrayList<>();
    List<CategoryItem> categoryItemList3 = new ArrayList<>();


    String store_name;
    TextView name;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturantproduct);

        back=findViewById(R.id.add_menu_back_fab);

        name = findViewById(R.id.txtStoreName);
        store_name = getIntent().getStringExtra("name");
        name.setText(store_name);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        List<AllCategory> allCategoryList = new ArrayList<>();

//        System.out.println(store_name.toLowerCase(Locale.ROOT)+"_food");
//
//        allCategoryList.add(new AllCategory(store_name + "食物", categoryItemList));
//        allCategoryList.add(new AllCategory(store_name + "飲料", categoryItemList2));
//        allCategoryList.add(new AllCategory(store_name + "點心", categoryItemList3));
//
//
//        getData(categoryItemList, store_name.toLowerCase(Locale.ROOT) + "_food");
//        getData(categoryItemList2, store_name.toLowerCase(Locale.ROOT) + "_beverage");
//        getData(categoryItemList3, store_name.toLowerCase(Locale.ROOT) + "_dessert");



        AddTitle addTitle=new AddTitle();
        List<List<String>> title =addTitle.getTitles();


//        List<List<String>> titles =new ArrayList<>();

//        List<String> mcdonalds= new ArrayList<>();
//        mcdonalds.add("mcdonalds_food");
//        mcdonalds.add("mcdonalds_beverage");
//        List<String> kfc = new ArrayList<>();
//        kfc.add("kfc_food");
//        kfc.add("kfc_beverage");
//        kfc.add("kfc_dessert");
//        titles.add(mcdonalds);
//        titles.add(kfc);



        for(List<String> lists : title){
            for(String items : lists){
                System.out.println(items);
                List<CategoryItem> itemList = new ArrayList<>();
//                System.out.println(items);
//                System.out.println(store_name);
                if(items.contains(store_name.toLowerCase(Locale.ROOT))){
                    System.out.println(items);
                    allCategoryList.add(new AllCategory(items, itemList));
                    getData(itemList, items);
                }

            }
        }

//        for(List<String> lists : titles){
//            System.out.println(lists);
//            for(String items : lists){
//                System.out.println(items);
//                List<CategoryItem> itemList = new ArrayList<>();
//                if(items.toString().equals(store_name.toLowerCase(Locale.ROOT)+"_food")){
//                    System.out.println(items);
//                    allCategoryList.add(new AllCategory(items, itemList));
//                    getData(itemList, store_name.toLowerCase(Locale.ROOT) + "_food");
//                }else if( items.toString().equals(store_name.toLowerCase(Locale.ROOT)+"_beverage") ){
//                    System.out.println(items);
//                    allCategoryList.add(new AllCategory(items, itemList));
//                    getData(itemList, store_name.toLowerCase(Locale.ROOT) + "_beverage");
//                }
//
//            }
//        }

        setMainCategoryRecycler(allCategoryList);


    }

    private void setMainCategoryRecycler(List<AllCategory> allCategoryList) {

        mainCategoryRecycler = findViewById(R.id.RVparent);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mainCategoryRecycler.setLayoutManager(layoutManager);
        categoryTitleAdapter = new CategoryTitleAdapter(this, allCategoryList);
        mainCategoryRecycler.setAdapter(categoryTitleAdapter);

    }


    private void getData(List<CategoryItem> categoryItems, String collection) {
        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getString("name"));
                                categoryItems.add(new CategoryItem(document.getString("name")));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        categoryTitleAdapter.notifyDataSetChanged();
                    }
                });

    }


}