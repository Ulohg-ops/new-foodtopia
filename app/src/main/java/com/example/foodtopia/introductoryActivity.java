package com.example.foodtopia;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.ViewPropertyAnimator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class introductoryActivity extends AppCompatActivity {

    //splash底
    ImageView background;
    //動畫
    LottieAnimationView animation;
    //Foodtopia字串
    TextView text_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);

        background = findViewById(R.id.background);
        animation = findViewById(R.id.animation);
        text_logo = findViewById(R.id.text_logo);

        // change color of text_logo
        // 配色可能要改！
        String text = "Foodtopia";
        SpannableString spannableString = new SpannableString(text);

        ForegroundColorSpan yellow = new ForegroundColorSpan(getResources().getColor(R.color.my_yellow));
        ForegroundColorSpan green = new ForegroundColorSpan(getResources().getColor(R.color.my_green));
        ForegroundColorSpan red = new ForegroundColorSpan(getResources().getColor(R.color.my_red));

        spannableString.setSpan(yellow, 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(green, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(red, 7, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        text_logo.setText(spannableString);

        // add animation to text_logo
        Animation am = new AlphaAnimation(0.0f, 1.0f);
        am.setDuration(1500);
        am.setRepeatCount(0);
        text_logo.startAnimation(am);

        // add animation
        background.animate().translationY(-2000).setDuration(1000).setStartDelay(4000);
        animation.animate().translationY(2000).setDuration(1000).setStartDelay(4000);
//        text_logo.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
        ViewPropertyAnimator anim = text_logo.animate();
        anim.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Intent intent = new Intent(introductoryActivity.this, Login.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        text_logo.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
    }
    

}