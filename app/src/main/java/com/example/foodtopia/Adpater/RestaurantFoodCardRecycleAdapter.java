package com.example.foodtopia.Adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtopia.R;
import com.example.foodtopia.classes.restaurantProduct;

import java.util.ArrayList;


public class RestaurantFoodCardRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<restaurantProduct> list = new ArrayList<>();
    private Context context;
    public RestaurantFoodCardRecycleAdapter(Context ctx,ArrayList list)
    {
        this.context = ctx;
        this.list=list;
    }

    public void setItems(ArrayList<restaurantProduct> products)
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
        restaurantProduct foods = list.get(position);
        vh.productName.setText(foods.getName());
        vh.productCalories.setText(foods.getCalories());

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


