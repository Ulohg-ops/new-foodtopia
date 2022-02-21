package com.example.foodtopia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.foodtopia.restaurant.RestaurantFragment;
import com.example.foodtopia.social.SocialFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    Fragment fragment = new DashboardFragment();


//    check whether user log in  if not intent to login
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(FirebaseAuth.getInstance().getCurrentUser()==null){
//            startActivity(new Intent(this,Login.class));
//            finish();
//        }
//    }

    //    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //顯示首頁
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();




        //點選下方工具列切換頁面
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Dashboard:
                        fragment = new DashboardFragment();
                        break;
                    case R.id.Restaurant:
                        fragment = new RestaurantFragment();
                        break;
                    case R.id.Add:
                        fragment = new AddFragment();
                        break;
                    case R.id.Social:
                        fragment = new SocialFragment();
                        break;
                    case R.id.Account:
                        fragment = new AccountFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
                return true;
            }
        });
    }
}