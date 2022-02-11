package com.example.foodtopia;

import static androidx.media.MediaBrowserServiceCompat.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 參考網址 :
 * https://medium.com/nerd-for-tech/how-to-add-extended-floating-action-button-in-android-android-studio-java-481cc9b3cdcb
 * https://medium.com/@waynechen323/android-%E5%9F%BA%E7%A4%8E%E7%9A%84-fragment-%E4%BD%BF%E7%94%A8%E6%96%B9%E5%BC%8F-730858c12a43
 */

public class Add extends Fragment {

    private View root;
    //餐點熱量
    private TextView breakfastKcalText,lunchKcalText,dinnerKcalText,dessertKcalText;

    private TextView breakfastText, breakfastQuantifier;
    private TextView lunchText, lunchQuantifier;
    private TextView dinnerText, dinnerQuantifier;
    private TextView dessertText, dessertQuantifier;
    // breakfast list
    private ArrayList<String> breakfastList;
    private ArrayList<String> breakfastQuantifierList;
    // lunch list
    private ArrayList<String> lunchList;
    private ArrayList<String> lunchQuantifierList;
    // dinner list
    private ArrayList<String> dinnerList;
    private ArrayList<String> dinnerQuantifierList;
    // dessert list
    private ArrayList<String> dessertList;
    private ArrayList<String> dessertQuantifierList;


    //新增按鈕
    Button addBreakfastBtn, addLunchBtn, addDinnerBtn, addDessertBtn;

    public Add() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_add, container, false);

        addBreakfastBtn = root.findViewById(R.id.addBreakfastBtn);
        addLunchBtn = root.findViewById(R.id.addLunchBtn);
        addDinnerBtn = root.findViewById(R.id.addDinnerBtn);
        addDessertBtn = root.findViewById(R.id.addDessertBtn);

        breakfastKcalText = root.findViewById(R.id.breakfastKcalText);
        lunchKcalText = root.findViewById(R.id.lunchKcalText);
        dinnerKcalText = root.findViewById(R.id.dinnerKcalText);
        dessertKcalText = root.findViewById(R.id.dessertKcalText);

        breakfastText = root.findViewById(R.id.breakfastText);
        breakfastQuantifier = root.findViewById(R.id.breakfastQuantifier);
        lunchText = root.findViewById(R.id.lunchText);
        lunchQuantifier = root.findViewById(R.id.lunchQuantifier);
        dinnerText = root.findViewById(R.id.dinnerText);
        dinnerQuantifier = root.findViewById(R.id.dinnerQuantifier);
        dessertText = root.findViewById(R.id.dessertText);
        dessertQuantifier = root.findViewById(R.id.dessertQuantifier);

        breakfastList = new ArrayList<>();
        breakfastQuantifierList = new ArrayList<>();
        lunchList = new ArrayList<>();
        lunchQuantifierList = new ArrayList<>();
        dinnerList = new ArrayList<>();
        dinnerQuantifierList = new ArrayList<>();
        dessertList = new ArrayList<>();
        dessertQuantifierList = new ArrayList<>();

        breakfastList.add("豆漿");
        breakfastQuantifierList.add("一杯(小)");
        lunchList.add("吐司");
        lunchQuantifierList.add("一片");
        dinnerList.add("豆漿");
        dinnerQuantifierList.add("一杯(小)");
        dessertList.add("吐司");
        dessertQuantifierList.add("一片");

        for (int i=0;i<breakfastList.size();i++){
            breakfastText.append(breakfastList.get(i)+"\n");
            breakfastQuantifier.append(breakfastQuantifierList.get(i)+"\n");
        }
        for (int i=0;i<lunchList.size();i++){
            lunchText.append(lunchList.get(i)+"\n");
            lunchQuantifier.append(lunchQuantifierList.get(i)+"\n");
        }
        for (int i=0;i<dinnerList.size();i++){
            dinnerText.append(dinnerList.get(i)+"\n");
            dinnerQuantifier.append(dinnerQuantifierList.get(i)+"\n");
        }
        for (int i=0;i<dessertList.size();i++){
            dessertText.append(dessertList.get(i)+"\n");
            dessertQuantifier.append(dessertQuantifierList.get(i)+"\n");
        }

        addBreakfastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return root;
    }

}