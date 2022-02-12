package com.example.foodtopia.Adpater;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtopia.R;
import com.example.foodtopia.Reaturant_add_Activity;
import com.example.foodtopia.RestaurantProductActivity;
import com.example.foodtopia.classes.restaurants;


import java.util.List;

public class CardRecycleAdapter extends RecyclerView.Adapter<CardRecycleAdapter.ViewHolder> {
    private Context context;
    private List<restaurants> memberList;

    public CardRecycleAdapter(Context context, List<restaurants> memberList) {
        this.context = context;
        this.memberList = memberList;
    }


    //當現有的ViewHolder不夠用時，要求Adapter產生一個新的
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.restaurant_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final restaurants member = memberList.get(position);
        holder.storeImage.setImageResource(member.getImage());
        holder.storeName.setText(member.getName());

//        if (position % 6 == 0) {
//            holder.linearBg.setBackgroundColor(ContextCompat.getColor(context, R.color.cv_c1));
//        } else if (position % 6 == 1) {
//            holder.linearBg.setBackgroundColor(ContextCompat.getColor(context, R.color.cv_c2));
//        } else if (position % 6 == 2) {
//            holder.linearBg.setBackgroundColor(ContextCompat.getColor(context, R.color.cv_c3));
//        } else if (position % 6 == 3) {
//            holder.linearBg.setBackgroundColor(ContextCompat.getColor(context, R.color.cv_c4));
//        } else if (position % 6 == 4) {
//            holder.linearBg.setBackgroundColor(ContextCompat.getColor(context, R.color.cv_c5));
//        } else if (position % 6 == 5) {
//            holder.linearBg.setBackgroundColor(ContextCompat.getColor(context, R.color.cv_c6));
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Reaturant_add_Activity.class);
                intent.putExtra("image", member.getImage());
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
        ImageView storeImage;
        LinearLayout linearBg;
        TextView  storeName;

        ViewHolder(View itemView) {
            super(itemView);
            storeImage = (ImageView) itemView.findViewById(R.id.storeImage);
            storeName = (TextView) itemView.findViewById(R.id.storeName);
            linearBg = (LinearLayout) itemView.findViewById(R.id.bg);
        }
    }
}
