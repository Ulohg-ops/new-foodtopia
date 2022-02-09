package com.example.foodtopia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Reaturant_add_Activity extends AppCompatActivity {
    TextView name;
    String store_name;
    ArrayList<restaurant_product_get_set> memberList = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaturant_add);
        name = findViewById(R.id.txtStoreName);
//        System.out.println(name);
        store_name = getIntent().getStringExtra("name");
        name.setText(store_name);

        memberList.add(new restaurant_product_get_set( "麥香雞", "150"));
        memberList.add(new restaurant_product_get_set( "麥香雞", "1250"));

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(manager);
        RestaurantFoodCardRecycleAdapter adapter = new RestaurantFoodCardRecycleAdapter(this);
        adapter.setItems(memberList);
        recyclerView.setAdapter(adapter);


    }
}