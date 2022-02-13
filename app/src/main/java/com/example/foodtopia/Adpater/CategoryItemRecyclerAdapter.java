package com.example.foodtopia.Adpater;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtopia.MainActivity;
import com.example.foodtopia.R;
import com.example.foodtopia.model.CategoryItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CategoryItemRecyclerAdapter extends RecyclerView.Adapter<CategoryItemRecyclerAdapter.CategoryItemViewHolder> {

    private Context context;
    private List<CategoryItem> categoryItemList;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;


    public CategoryItemRecyclerAdapter(Context context, List<CategoryItem> categoryItemList) {
        this.context = context;
        this.categoryItemList = categoryItemList;
    }

    @NonNull
    @Override
    public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryItemViewHolder(LayoutInflater.from(context).inflate(R.layout.resturant_category_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemViewHolder holder, int position) {
        String foodName2=categoryItemList.get(position).getFoodName();
        String calories2=categoryItemList.get(position).getCalories();
        String carbohydrate2=categoryItemList.get(position).getCarbohydrate();
        String fat2=categoryItemList.get(position).getFat();
        String protein2=categoryItemList.get(position).getProtein();
        String saturatedfat2=categoryItemList.get(position).getSaturatedfat();
        String unsaturatedfat2=categoryItemList.get(position).getUnsaturatedfat();
        String sodium2=categoryItemList.get(position).getSodium();
        String sugars2=categoryItemList.get(position).getSugars();

        holder.itemfood.setText(categoryItemList.get(position).getFoodName());
        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                View diaglogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.restaurant_popup_window, null);

                Spinner spinner = diaglogView.findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                        R.array.meal_time, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                spinner.setAdapter(adapter);

                FloatingActionButton add;
                TextView foodName;
                TextView calories;
                TextView carbohydrate;
                TextView fat;
                TextView protein;
                TextView saturatedfat;
                TextView unsaturatedfat;
                TextView sodium;
                TextView sugars;

                add=diaglogView.findViewById(R.id.add);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("ahoy");
                    }
                });

                calories=diaglogView.findViewById(R.id.calories);
                calories.setText(calories2);

                foodName = diaglogView.findViewById(R.id.foodName);
                foodName.setText(foodName2);

                carbohydrate = diaglogView.findViewById(R.id.carbohydrate);
                carbohydrate.setText(carbohydrate2);

                fat = diaglogView.findViewById(R.id.fat);
                fat.setText(fat2);

                protein = diaglogView.findViewById(R.id.protein);
                protein.setText(protein2);

                saturatedfat = diaglogView.findViewById(R.id.saturatedfat);
                saturatedfat.setText(saturatedfat2);

                unsaturatedfat = diaglogView.findViewById(R.id.unsaturatedfat);
                unsaturatedfat.setText(unsaturatedfat2);

                sodium = diaglogView.findViewById(R.id.sodium);
                sodium.setText(sodium2);

                sugars = diaglogView.findViewById(R.id.sugars);
                sugars.setText(sugars2);

                builder.setView(diaglogView);
                builder.setCancelable(true);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }

    public static final class CategoryItemViewHolder extends RecyclerView.ViewHolder {

        TextView itemfood;
        ImageButton arrow;

        public CategoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            arrow = itemView.findViewById(R.id.btn_next);
            itemfood = itemView.findViewById(R.id.product);

        }
    }

    public void createPopUpDialog() {
        dialog.setContentView(R.layout.restaurant_popup_window);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        TextView FoodTitle = dialog.findViewById(R.id.foodName);
        dialog.show();

    }
}
