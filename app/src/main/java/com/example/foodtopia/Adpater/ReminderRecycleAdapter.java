package com.example.foodtopia.Adpater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtopia.R;
import com.example.foodtopia.Remind;
import com.example.foodtopia.ReminderEditActivity;

import java.util.List;

public class ReminderRecycleAdapter extends RecyclerView.Adapter<ReminderRecycleAdapter.ViewHolder> {
    private List<Remind> data;
    private Context context;

    public ReminderRecycleAdapter(Context context, List<Remind> data) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder_list, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Remind remind=data.get(position);
        holder.medicineName.setText(remind.getMedicine());
        holder.time.setText(remind.getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ReminderEditActivity.class);
                intent.putExtra("remindID",remind.getRemindeid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView medicineName;
        private TextView time;
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.medicineName = view.findViewById(R.id.medicineName);
            this.time=view.findViewById(R.id.time);
        }

        @Override
        public void onClick(View view) {
        }
    }

}
