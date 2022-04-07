package com.example.foodtopia.adpater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodtopia.AnalysisResultsActivity;
import com.example.foodtopia.R;

import java.util.List;
import java.util.Locale;

public class AnalysisResultRecycleAdapter extends RecyclerView.Adapter<AnalysisResultRecycleAdapter.ViewHolder> {

    private List<Float> keyList;
    private List<String> valueList;
    private String mealtime;
    private Context context;

    public AnalysisResultRecycleAdapter(String mealtime,List<Float> keyList,List<String> valueList , Context context) {
        super();
        this.mealtime = mealtime;
        this.keyList = keyList;
        this.valueList = valueList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView number, prediction, confidence;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.textView_analysis_number);
            prediction = itemView.findViewById(R.id.textView_analysis_prediction);
            confidence = itemView.findViewById(R.id.textView_analysis_confidence);
            mView = itemView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_analysis_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.number.setText(String.valueOf(position+1));
        holder.prediction.setText(valueList.get(valueList.size()-position-1));
        holder.confidence.setText(String.format(Locale.TAIWAN,"%.2f%%",keyList.get(keyList.size()-position-1)));
        holder.mView.setOnClickListener(view -> {
            Intent intentPrediction = new Intent(context, AnalysisResultsActivity.class);
            intentPrediction.putExtra("prediction",valueList.get(valueList.size()-position-1));
            intentPrediction.putExtra("mealtime",mealtime);
            context.startActivity(intentPrediction);
        });
    }

    @Override
    public int getItemCount() {
        return keyList.size();
    }


}
