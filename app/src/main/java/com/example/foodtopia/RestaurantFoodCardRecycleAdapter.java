package com.example.foodtopia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class RestaurantFoodCardRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<restaurant_product_get_set> list = new ArrayList<>();
    private Context context;
    public RestaurantFoodCardRecycleAdapter(Context ctx)
    {
        this.context = ctx;
    }

    public void setItems(ArrayList<restaurant_product_get_set> products)
    {
        list.addAll(products);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.restuarant_product_cardview,parent,false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        restaurant_product_get_set foods = list.get(position);
        vh.productName.setText(foods.getFood_name());
        vh.productCalories.setText(foods.getFood_name());

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName,productCalories;

        ViewHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.txtfoodName);
            productCalories = (TextView) itemView.findViewById(R.id.txtcarlories);
        }
    }
}


