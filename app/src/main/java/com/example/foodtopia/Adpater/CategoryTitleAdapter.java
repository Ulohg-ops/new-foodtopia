package com.example.foodtopia.Adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtopia.R;
import com.example.foodtopia.Model.Post;
import com.example.foodtopia.Model.restaurantProduct;

import java.util.List;

public class CategoryTitleAdapter extends RecyclerView.Adapter<CategoryTitleAdapter.MainViewHolder> {

    private Context context;
    private List<restaurantProduct.AllCategory> allCategoryList;

    public CategoryTitleAdapter(Context context, List<restaurantProduct.AllCategory> allCategoryList) {
        this.context = context;
        this.allCategoryList = allCategoryList;
    }

    public CategoryTitleAdapter(List<restaurantProduct.AllCategory> allCategoryList) {
        this.allCategoryList = allCategoryList;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.restaurant_category_title, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {

        holder.categoryTitle.setText(allCategoryList.get(position).getCategoryTitle());
        setCatItemRecycler(holder.itemRecycler, allCategoryList.get(position).getCategoryItemList());

    }

    @Override
    public int getItemCount() {
        return allCategoryList.size();
    }

    public static final class MainViewHolder extends RecyclerView.ViewHolder{

        TextView categoryTitle;
        RecyclerView itemRecycler;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryTitle = itemView.findViewById(R.id.cat_title);
            itemRecycler = itemView.findViewById(R.id.item_recycler);

        }
    }

    private void setCatItemRecycler(RecyclerView recyclerView, List<Post.CategoryItem> categoryItemList){

        CategoryItemAdapter itemRecyclerAdapter = new CategoryItemAdapter(context, categoryItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(itemRecyclerAdapter);

    }
}
