package com.example.foodtopia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Restaurant extends Fragment {
    private View view;
    List<Member> memberList = new ArrayList<>();
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


        memberList.add(new Member( R.drawable.logo1, "McDonloads",5));
        memberList.add(new Member( R.drawable.logo2, "KFC",5));
        memberList.add(new Member( R.drawable.logo3, "Subway",5));
        memberList.add(new Member( R.drawable.logo4, "Seven-Eleven",5));
        memberList.add(new Member( R.drawable.logo1, "麥味登",5));
        memberList.add(new Member( R.drawable.logo2, "摩斯漢堡",5));
        memberList.add(new Member(R.drawable.logo3, "Seven-Eleven",5));
        memberList.add(new Member( R.drawable.logo4, "FamilyMart",5));




// MemberAdapter 會在步驟7建立

    }

    //準備建立Fragment元件的畫面
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //載入與回傳Fragment元件的畫面物件
        view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new CardRecycleAdapter(getActivity(), memberList));
        return view;
    }
}