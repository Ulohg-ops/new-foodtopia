package com.example.foodtopia.Adpater;

import android.app.AlertDialog;
import android.content.Context;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.CategoryItemViewHolder> {

    private Context context;
    private List<Post.CategoryItem> categoryItemList;
    final String TAG = "MyActivity";
    DatabaseReference reference;
    FirebaseAuth auth;


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
                                protein2, sodium2, sugars2, mealtime);
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
                                protein2, sodium2, sugars2, mealtime);
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
                         String sodium, String sugar, String mealtime) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Diets");


        Date current = new Date();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
        String date = sdfDate.format(current);
        SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
        String time = sdfTime.format(current);


        String mealid = reference.push().getKey();

        HashMap<String, Object> map = new HashMap<>();
        map.put("foodname", foodname);
        map.put("calories", calories);
        map.put("userid",userID);
        map.put("carbohydrate", carbohydrate);
        map.put("fat", fat);
        map.put("protein", protein);
        map.put("sodium", sodium);
        map.put("sugar", sugar);
        map.put("mealtime",mealtime);
        map.put("time",time);
        map.put("date",date);

        reference.child(mealid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "新增成功!!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"發生了一點錯誤!!",Toast.LENGTH_SHORT).show();
            }
        });




    }

}



