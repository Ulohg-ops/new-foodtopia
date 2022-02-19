package com.example.foodtopia.restaurant;

import com.example.foodtopia.Model.restaurants;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodtopia.Adpater.CardRecycleAdapter;
import com.example.foodtopia.R;

import java.util.ArrayList;
import java.util.List;

public class Restaurant extends Fragment {
    private View view;
    List<restaurants> memberList = new ArrayList<>();
    RecyclerView recyclerView;
    public Restaurant() {
        // Required empty public constructor
    }

    public static Restaurant newInstance(String param1, String param2) {
        Restaurant fragment = new Restaurant();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        memberList.add(new restaurants( R.drawable.restaurant_picture_logo1, "Mcdonalds",5));
        memberList.add(new restaurants( R.drawable.restaurant_picture_logo2, "KFC",5));
        memberList.add(new restaurants( R.drawable.restaurant_picture_logo3, "Subway",5));
        memberList.add(new restaurants( R.drawable.restaurant_picture_logo4, "Seven-Eleven",5));
        memberList.add(new restaurants( R.drawable.restaurant_picture_logo5, "麥味登",5));
        memberList.add(new restaurants( R.drawable.restaurant_picture_logo6, "摩斯漢堡",5));
        memberList.add(new restaurants(R.drawable.restaurant_picture_logo7, "starbucks",5));
        memberList.add(new restaurants( R.drawable.restaurant_picture_logo8, "FamilyMart",5));

        // MemberAdapter 會在步驟7建立

    }
    //準備建立Fragment元件的畫面
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //載入與回傳Fragment元件的畫面物件
        view = inflater.inflate(R.layout.restaurant, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new CardRecycleAdapter(getActivity(), memberList));
        return view;
    }
}