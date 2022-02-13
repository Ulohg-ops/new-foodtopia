package com.example.foodtopia;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.foodtopia.Adpater.ViewPagerAdapter;

// 首次使用app的介紹頁面

public class OnboardingActivity extends AppCompatActivity {

    ActionBar actionBar;
    ViewPager viewPager;
    LinearLayout linearLayout;
    TextView[] dostTv;
    int[] layouts;
    Button mNextBtn, mSkipBtn;
    ViewPagerAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //If is first launch app run onboarding screen and set the App_START to FALSE
        //determine whether the App_START is FALSE
        //If it is FALSE,go to Login page

        if (!isFirstTimeAppStart()) {
            setAppStartStatus(false);
            startActivity(new Intent(OnboardingActivity.this, Login.class));
            finish();
        }
        setContentView(R.layout.activity_onboarding);


        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        viewPager = findViewById(R.id.viewPager);
        linearLayout = findViewById(R.id.dotlayout);
        mNextBtn = findViewById(R.id.btn_next);
        mSkipBtn = findViewById(R.id.btn_skip);

        statusBarTransparent();

        mSkipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnboardingActivity.this, Login.class));
                finish();
            }
        });
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPage = viewPager.getCurrentItem() + 1;
                if (currentPage < layouts.length) {//length==3
                    viewPager.setCurrentItem(currentPage);
                    //switch to page[currentPage]
                    //currentPage[0] -> slide1
                    //currentPage[1] -> slide2
                    //currentPage[2] -> slide3
                } else {
                    setAppStartStatus(false);// if user have watched set App_START to False
                    startActivity(new Intent(OnboardingActivity.this, Login.class));
                    finish();
                }
            }
        });
        layouts = new int[]{R.layout.onboarding_slide_1, R.layout.onboarding_slide_2, R.layout.onboarding_slide_3};
        myAdapter = new ViewPagerAdapter(layouts, getApplicationContext());
        viewPager.setAdapter(myAdapter);
        setDots(0); //first page dot to white
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == layouts.length - 1) {
                    mNextBtn.setText("START");
                    mSkipBtn.setVisibility(View.GONE);
                } else {
                    mNextBtn.setText("Next");
                    mSkipBtn.setVisibility(View.VISIBLE);
                    System.out.println(View.VISIBLE);
                }
                setDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private boolean isFirstTimeAppStart() {
//        https://developer.android.com/training/tv/playback/onboarding
//        https://litotom.com/ch7-1-sharedpreferences/
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SLIDE_APP", Context.MODE_PRIVATE);
        return pref.getBoolean("App_START", true);
        // If App_START　is false return false , otherwise return true.
    }

    private void setAppStartStatus(boolean status) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SLIDE_APP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("App_START", status);
        editor.apply();
    }//set the App_START to be false

    private void statusBarTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }

    }

    private void setDots(int page) {
        linearLayout.removeAllViews();
        dostTv = new TextView[layouts.length]; //3
        for (int i = 0; i < dostTv.length; i++) {
            dostTv[i] = new TextView(this);
            dostTv[i].setTextSize(30);
            //https://www.toptal.com/designers/htmlarrows/arrows/
            dostTv[i].setText(Html.fromHtml("&#8226;")); //
            dostTv[i].setTextColor(Color.parseColor("#a9b4bb"));//藏青色
            linearLayout.addView(dostTv[i]);
        }
        if (dostTv.length > 0) {
            dostTv[page].setTextColor(Color.parseColor("#ffffff"));
        }
    }
}
