package com.example.foodtopia.adpater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtopia.AddCaloriesCheckActivity;
import com.example.foodtopia.model.Foods;
import com.example.foodtopia.R;

import java.util.List;

public class AddFoodNameRecycleAdapter extends RecyclerView.Adapter<AddFoodNameRecycleAdapter.ViewHolder>{
    private Context context;
    private List<Foods> memberList;

    public AddFoodNameRecycleAdapter(Context context, List<Foods> memberList) {
        this.context = context;
        this.memberList = memberList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_add_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Foods member = memberList.get(position);
        holder.foodName.setText(member.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddCaloriesCheckActivity.class);
                intent.putExtra("name", member.getName());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return memberList.size();
    }

    //Adapter 需要一個 ViewHolder，只要實作它的 constructor 就好，保存起來的view會放在itemView裡面
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButton;
        TextView foodName;

        ViewHolder(View itemView) {
            super(itemView);
            foodName = (TextView) itemView.findViewById(R.id.foodName);
            imageButton=itemView.findViewById(R.id.btn_next);
        }
    }
}
