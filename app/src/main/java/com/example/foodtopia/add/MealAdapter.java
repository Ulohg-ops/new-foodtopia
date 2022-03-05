package com.example.foodtopia.add;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.foodtopia.AddManualFragment;
import com.example.foodtopia.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {

    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private ArrayList<HashMap<String,String>> arrayList;
    private Context context;
    private String key;

    public MealAdapter(ArrayList<HashMap<String,String>> arrayList, Context context) {
        super();
        this.arrayList = arrayList;
        this.context = context;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private View parent;
        private TextView foodName, quantifier;
        private Button editBtn, deleteBtn;
        private SwipeRevealLayout swipeRevealLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.textView_foodName);
            quantifier = itemView.findViewById(R.id.textView_quantifier);
            editBtn = itemView.findViewById(R.id.button_menu_Edit);
            deleteBtn = itemView.findViewById(R.id.button_menu_Delete);
            parent = itemView;
            swipeRevealLayout = itemView.findViewById(R.id.swipeLayout);
        }
    }
    @NonNull
    @Override
    public MealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_swipe_menu,parent,false);
        return new ViewHolder(view );
    }

    @Override
    public void onBindViewHolder(@NonNull MealAdapter.ViewHolder holder, int position) {

        viewBinderHelper.setOpenOnlyOne(true);//設置swipe只能有一個item被拉出
        viewBinderHelper.bind(holder.swipeRevealLayout,String.valueOf(position));//綁定Layout (第三步)
        holder.foodName.setText(arrayList.get(position).get("foodname"));
        holder.quantifier.setText(arrayList.get(position).get("quantifier"));

        //編輯
        holder.editBtn.setOnClickListener(view1 -> {
            holder.swipeRevealLayout.close(true);
            key = arrayList.get(position).get("Id");
            Fragment fragment = new AddManualFragment();
            Bundle dietID = new Bundle();
            dietID.putString("key",key);
            FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
            manager.setFragmentResult("dietID",dietID);
            ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();

        });

        //刪除
        holder.deleteBtn.setOnClickListener((v -> {
            holder.swipeRevealLayout.close(true);

            //刪除資料庫裡的資料
            key = arrayList.get(position).get("Id");
            DatabaseReference mDietRef = FirebaseDatabase.getInstance().getReference()
                    .child("Diets").child(key);
            mDietRef.removeValue();

            //刪除recyclerview裡的資料
            arrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,arrayList.size());
        }));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}
