package com.example.foodtopia;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class Account extends Fragment {
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public Account() {
        // Required empty public constructor
    }

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

    }

    // account list
    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    String[] list_name = {"個人資料", "鬧鐘", "居家運動建議", "飲食建議", "收藏", "設定"};
    int[] list_icon = {R.drawable.account_icon_info, R.drawable.account_icon_clock, R.drawable.account_icon_home, R.drawable.account_icon_description, R.drawable.account_icon_favorite, R.drawable.account_icon_setting};
    int[] list_arrow = {R.drawable.account_icon_arrow, R.drawable.account_icon_arrow, R.drawable.account_icon_arrow, R.drawable.account_icon_arrow, R.drawable.account_icon_arrow, R.drawable.account_icon_arrow};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        listView = (ListView) view.findViewById(R.id.profile_list);


        //-------------------user email and name----------------------
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(user.getUid());
        TextView profile_name=view.findViewById(R.id.profile_name);
        TextView profile_email=view.findViewById(R.id.profile_email);

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    mDatabase.child("Users").child(user.getUid()).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                profile_name.setText(String.valueOf(task.getResult().getValue()));
                System.out.println("dasdasads"+String.valueOf(task.getResult().getValue()));
            }
        }
    });
     profile_email.setText(user.getEmail());
        //-------------------user email and name----------------------

        //----------------------log out btn----------------------------
        //https://firebase.google.com/docs/auth/android/custom-auth
        AppCompatButton logout=view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });
        //----------------------log out btn----------------------------

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