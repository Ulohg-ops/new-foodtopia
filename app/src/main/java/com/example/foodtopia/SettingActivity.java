package com.example.foodtopia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    ListView listView_switch;
    ListView listView;
    ImageButton back;

    String[] list_name = {"常見問題", "聯絡我們", "分享給其他人"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        listView_switch = findViewById(R.id.setting_list_switch);
        listView = findViewById(R.id.setting_list);
        back = findViewById(R.id.btn_back);

        MyAdapter myAdapter = new MyAdapter();
        listView_switch.setAdapter(myAdapter);

        MyAdapter2 myAdapter2 = new MyAdapter2();
        listView.setAdapter(myAdapter2);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 1;
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
            View view1 = getLayoutInflater().inflate(R.layout.item_setting_list, null);
            TextView text = view1.findViewById(R.id.text);

            text.setText("通知");

            return view1;
        }
    }

    private class MyAdapter2 extends BaseAdapter{

        @Override
        public int getCount() {
            return list_name.length;
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
            View view1 = getLayoutInflater().inflate(R.layout.item_setting_list2, null);
            TextView text = view1.findViewById(R.id.text);

            text.setText(list_name[i]);

            return view1;
        }
    }

}