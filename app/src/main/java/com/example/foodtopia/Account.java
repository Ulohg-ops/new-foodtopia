package com.example.foodtopia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Account extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Account() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Account.
     */
    // TODO: Rename and change types and number of parameters
    public static Account newInstance(String param1, String param2) {
        Account fragment = new Account();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // account list
    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    String[] list_name = {"個人資料", "鬧鐘", "居家運動建議", "飲食建議", "收藏", "設定"};
    int[] list_icon = {R.drawable.info, R.drawable.clock, R.drawable.home, R.drawable.ic_outline_description_24, R.drawable.favorite, R.drawable.setting};
    int[] list_arrow = {R.drawable.arrow, R.drawable.arrow, R.drawable.arrow, R.drawable.arrow, R.drawable.arrow, R.drawable.arrow};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        listView = (ListView) view.findViewById(R.id.profile_list);
//        arrayList = new ArrayList<String>();
//        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
//        listView.setAdapter(arrayAdapter);
//        arrayList.add("個人資料");
//        arrayList.add("鬧鐘");
//        arrayList.add("居家運動建議");
//        arrayList.add("飲食建議");
//        arrayList.add("收藏");
//        arrayList.add("設定");

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        return view;
    }

    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_icon.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.custom_profile_list, null);

            TextView text = view1.findViewById(R.id.text);
            ImageView icon = view1.findViewById(R.id.icon);
            ImageView arrow = view1.findViewById(R.id.arrow);

            text.setText(list_name[i]);
            icon.setImageResource(list_icon[i]);
            arrow.setImageResource(list_arrow[i]);

            return view1;
        }
    }
}