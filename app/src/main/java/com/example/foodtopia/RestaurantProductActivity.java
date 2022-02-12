package com.example.foodtopia;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtopia.Adpater.CategoryTitleAdapter;
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
import java.util.Locale;

public class RestaurantProductActivity extends AppCompatActivity {

    RecyclerView mainCategoryRecycler;
    CategoryTitleAdapter categoryTitleAdapter;

    List<CategoryItem> categoryItemList = new ArrayList<>();
    List<CategoryItem> categoryItemList2 = new ArrayList<>();
    List<CategoryItem> categoryItemList3 = new ArrayList<>();

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

        List<List<String>> titles = new ArrayList<>();
        List<String> mcdonalds= new ArrayList<>();
        mcdonalds.add("mcdonalds_beverage");
        mcdonalds.add("mcdonalds_food");
        List<String> kfc = new ArrayList<>();
        kfc.add("kfc_beverage");
        kfc.add("kfc_food");
        kfc.add("kfc_dessert");
        titles.add(mcdonalds);
        titles.add(kfc);

        for(List<String> lists : titles){
            System.out.println(titles);
            for(String items : lists){
                System.out.println(items);
                if(items.toString().equals(store_name.toLowerCase(Locale.ROOT)+"_food")){
                    allCategoryList.add(new AllCategory(items, categoryItemList));
                }else if( items.toString().equals(store_name.toLowerCase(Locale.ROOT)+"_beverage") ){
                    allCategoryList.add(new AllCategory(items, categoryItemList2));
                }

            }
        }

//
//        allCategoryList.add(new AllCategory(store_name + "食物", categoryItemList));
//        allCategoryList.add(new AllCategory(store_name + "飲料", categoryItemList2));
//        allCategoryList.add(new AllCategory(store_name + "點心", categoryItemList3));


        getData(categoryItemList, store_name.toLowerCase(Locale.ROOT) + "_food");
        getData(categoryItemList2, store_name.toLowerCase(Locale.ROOT) + "_beverage");
        getData(categoryItemList3, store_name.toLowerCase(Locale.ROOT) + "_dessert");

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
                                Log.d(TAG, document.getId() + " => " + document.getString("name"));
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