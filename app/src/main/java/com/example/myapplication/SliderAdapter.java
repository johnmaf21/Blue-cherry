package com.example.myapplication;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.annotation.Nonnull;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    int[] layouts;

    public SliderAdapter(Context context, int[] layouts){
        this.context=context;
        this.layouts=layouts;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@Nonnull View view, @Nonnull Object o) {
        return (view==o);
    }

    @Override
    public void destroyItem(@Nonnull ViewGroup container, int position, @Nonnull Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Nonnull
    @Override
    public Object instantiateItem(@Nonnull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layouts[position],container,false);
        container.addView(view);
        return view;
    }
}
