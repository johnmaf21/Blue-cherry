package com.example.myapplication;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class IntroSlider extends AppCompatActivity {

    ViewPager viewPager;
    SliderAdapter sliderAdapter;
    int[] layouts = new int[]{R.layout.slide,R.layout.slide2,R.layout.slide3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        sliderAdapter = new SliderAdapter(this);

    }
}
