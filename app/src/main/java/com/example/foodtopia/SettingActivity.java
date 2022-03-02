package com.example.foodtopia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    ListView listView_switch;
    ListView listView;
    ImageButton back;
    private static final String CHANNEL_ID = "test";

    String[] list_name = {"常見問題", "聯絡我們", "分享給其他人"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // 可以傳送通知
        createNotificationChannel();

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("message", "哈囉大家好這是我們的專題，請大家多多支持");
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(SettingActivity.this, "copy successfully", Toast.LENGTH_LONG).show();
                        break;
                }
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
            Switch switcher = view1.findViewById(R.id.switcher);

            text.setText("通知");
            switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (switcher.isChecked()) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(SettingActivity.this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.notification_icon)
                                .setContentTitle("測試")
                                .setContentText("您已開啟通知")
                                .setDefaults(Notification.DEFAULT_VIBRATE)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(SettingActivity.this);
                        managerCompat.notify(0, builder.build());
                    }else {
                        // 關閉通知
                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(SettingActivity.this);
                        managerCompat.cancelAll();
                        Toast.makeText(SettingActivity.this, "您已關閉通知", Toast.LENGTH_LONG).show();
                    }
                }
            });

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

    // create notification
    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}