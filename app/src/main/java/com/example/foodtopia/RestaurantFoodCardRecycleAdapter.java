package com.example.foodtopia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RestaurantFoodCardRecycleAdapter extends RecyclerView.Adapter<RestaurantFoodCardRecycleAdapter.ViewHolder> {
    private Context context;
    private List<Member> memberList;

    RestaurantFoodCardRecycleAdapter(Context context, List<Member> memberList) {
        this.context = context;
        this.memberList = memberList;
    }


    //當現有的ViewHolder不夠用時，要求Adapter產生一個新的
    @Override
    public RestaurantFoodCardRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.restuarant_product_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Member member = memberList.get(position);
        holder.foodCarlories.setText(member.getImage());
        holder.foodName.setText(member.getName());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, Reaturant_add_Activity.class);
//                intent.putExtra("image", member.getImage());
//                intent.putExtra("name", member.getName());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    //Adapter 需要一個 ViewHolder，只要實作它的 constructor 就好，保存起來的view會放在itemView裡面
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView  foodName,foodCarlories;

        ViewHolder(View itemView) {
            super(itemView);
            foodName = (TextView) itemView.findViewById(R.id.txtfoodName);
            foodCarlories = (TextView) itemView.findViewById(R.id.txtcarlories);
        }
    }
}

