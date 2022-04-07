package com.example.foodtopia.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.foodtopia.adpater.SocialMyRecipeAdapter;
import com.example.foodtopia.R;
import com.example.foodtopia.social.MypostFragment;
import com.example.foodtopia.social.MysaveFragment;
import com.google.android.material.tabs.TabLayout;

public class SavedActivity extends AppCompatActivity {
    private TabLayout tableLayout;
    private ViewPager viewPager;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_saved);
        back = findViewById(R.id.back);
        tableLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.frame);
        tableLayout.setupWithViewPager(viewPager);
        SocialMyRecipeAdapter.VPAdapter vpAdapter = new SocialMyRecipeAdapter.VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new MypostFragment(), "我的食譜");
        vpAdapter.addFragment(new MysaveFragment(), "收藏食譜");
        viewPager.setAdapter(vpAdapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}