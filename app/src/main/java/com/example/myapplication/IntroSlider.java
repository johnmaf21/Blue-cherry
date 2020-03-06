package com.example.myapplication;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

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

public class IntroSlider extends AppCompatActivity {

    ViewPager viewPager;
    SliderAdapter sliderAdapter;
    LinearLayout layoutDot;
    TextView[] dotstv;
    Button openRegister;
    Button openLogin;

    int[] layouts = new int[]{R.layout.slide,R.layout.slide2,R.layout.slide3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);


        if (!isFirstTimeStartApp()){
            startMainActivity();
            finish();
        }
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        layoutDot = (LinearLayout) findViewById(R.id.dotLayout);

        setStatusBarTransparent();
        openLogin = (Button) findViewById(R.id.homeLogin);
        openRegister = (Button) findViewById(R.id.homeRegister);
        openRegister.setVisibility(View.INVISIBLE);
        openLogin.setVisibility(View.INVISIBLE);
        openLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroSlider.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        openRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroSlider.this, Register.class);
                startActivity(intent);
            }
        });

        sliderAdapter = new SliderAdapter(getApplicationContext(), layouts);
        viewPager.setAdapter(sliderAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setDotStatus(position);
                if (position<2){
                    openRegister.setVisibility(View.INVISIBLE);
                    openLogin.setVisibility(View.INVISIBLE);
                }
                else{
                    openRegister.setVisibility(View.VISIBLE);
                    openLogin.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setDotStatus(0);

    }

    private void startMainActivity() {
        setFirstTimeStartStatus(true);
        startActivity(new Intent(IntroSlider.this,Home.class));
        finish();
    }

    private boolean isFirstTimeStartApp(){
        SharedPreferences ref = getApplicationContext().getSharedPreferences("IntroSliderApp", Context.MODE_PRIVATE);
        return ref.getBoolean("FirstTimeStartFlag", true);
    }

    private void setFirstTimeStartStatus(boolean stt){
        SharedPreferences ref = getApplicationContext().getSharedPreferences("IntroSliderApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putBoolean("FirstTimeStartFlag", stt);
        editor.commit();
    }

    private void setStatusBarTransparent(){
        if (Build.VERSION.SDK_INT >=21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setDotStatus(int page){
        layoutDot.removeAllViews();
        dotstv = new TextView[layouts.length];
        for (int i=0; i< dotstv.length; i++){
            dotstv[i] = new TextView(this);
            dotstv[i].setText(Html.fromHtml("&#8226;"));
            dotstv[i].setTextSize(50);
            dotstv[i].setTextColor(Color.parseColor("#a9b4bb"));
            layoutDot.addView(dotstv[i]);
        }
        if (dotstv.length>0){
            dotstv[page].setTextColor(Color.parseColor("#ffffff"));

        }    }
}
