package com.example.foodtopia.add;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.foodtopia.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {

    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private ArrayList<HashMap<String,String>> arrayList;
    public MealAdapter(ArrayList<HashMap<String,String>> arrayList) {
        super();
        this.arrayList = arrayList;
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

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Edit",
                        Toast.LENGTH_SHORT).show();
                holder.swipeRevealLayout.close(true);
            }
        });

        holder.deleteBtn.setOnClickListener((v -> {
            holder.swipeRevealLayout.close(true);
            arrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,arrayList.size());
        }));//holder.btDelete
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}
