package com.example.foodtopia.Adpater;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtopia.R;
import com.example.foodtopia.Model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.CategoryItemViewHolder> {

    private Context context;
    private List<Post.CategoryItem> categoryItemList;
    final String TAG = "MyActivity";

    Date dNow ;
    SimpleDateFormat ft =
            new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CategoryItemAdapter(Context context, List<Post.CategoryItem> categoryItemList) {
        this.context = context;
        this.categoryItemList = categoryItemList;
    }

    @NonNull
    @Override
    public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_restaurant_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemViewHolder holder, int position) {
        String foodName2 = categoryItemList.get(position).getFoodName();
        String calories2 = categoryItemList.get(position).getCalories();
        String carbohydrate2 = categoryItemList.get(position).getCarbohydrate();
        String fat2 = categoryItemList.get(position).getFat();
        String protein2 = categoryItemList.get(position).getProtein();
        String saturatedfat2 = categoryItemList.get(position).getSaturatedfat();
        String unsaturatedfat2 = categoryItemList.get(position).getUnsaturatedfat();
        String sodium2 = categoryItemList.get(position).getSodium();
        String sugars2 = categoryItemList.get(position).getSugars();

        String meal;
        holder.itemfood.setText(categoryItemList.get(position).getFoodName());
        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                View diaglogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.diaglog_restaurant_add_meal, null);

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

                calories = diaglogView.findViewById(R.id.calories);
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

                add = diaglogView.findViewById(R.id.add);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mealtime=spinner.getSelectedItem().toString();
                        addMymeal(foodName2, calories2, carbohydrate2, fat2,
                                protein2, saturatedfat2, unsaturatedfat2, sodium2, sugars2, mealtime);
                    }
                });


                builder.setView(diaglogView);
                builder.setCancelable(true);
                builder.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                View diaglogView = LayoutInflater.from(view.getRootView().getContext()).inflate(R.layout.diaglog_restaurant_add_meal, null);

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

                calories = diaglogView.findViewById(R.id.calories);
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

                add = diaglogView.findViewById(R.id.add);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mealtime=spinner.getSelectedItem().toString();
                        addMymeal(foodName2, calories2, carbohydrate2, fat2,
                                protein2, saturatedfat2, unsaturatedfat2, sodium2, sugars2, mealtime);
                    }
                });


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

    public void addMymeal(String foodname, String calories, String carbohydrate, String fat, String protein,
                          String saturatedfat, String unsaturatedfat, String sodium, String sugar, String mealtime) {
        Map<String, Object> user = new HashMap<>();

        ft = new SimpleDateFormat("yyyyMMdd_hhmmss");
        dNow = new Date();
        user.put("foodname", foodname);
        user.put("calories", calories);
        user.put("carbohydrate", carbohydrate);
        user.put("fat", fat);
        user.put("protein", protein);
        user.put("saturatedfat", saturatedfat);
        user.put("unsaturatedfat", unsaturatedfat);
        user.put("sodium", sodium);
        user.put("sugar", sugar);


        db.collection("user_001").document(ft.format(dNow)+"_"+mealtime)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "新增成功!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });


    }

}
