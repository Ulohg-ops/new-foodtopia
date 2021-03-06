package com.example.foodtopia.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;



public class OnboardingViewPagerAdapter extends PagerAdapter {
    private LayoutInflater inflater;
    int[] layouts;
    Context context;

    public OnboardingViewPagerAdapter(int[] layouts, Context context) {
        this.layouts = layouts;
        this.context = context;
    }

    @Override

    public int getCount() { //get item count
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(layouts[position],container,false);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View v=(View)object;
        container.removeView(v);

    }
}
