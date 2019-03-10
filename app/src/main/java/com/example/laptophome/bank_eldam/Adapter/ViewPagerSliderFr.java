package com.example.laptophome.bank_eldam.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.laptophome.bank_eldam.R;

public class ViewPagerSliderFr extends PagerAdapter {
    LayoutInflater layoutInflater;
    Context context;


    private int[] mImages = new int[] {
            R.drawable.pager1,
            R.drawable.pager2
    };

    public ViewPagerSliderFr(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return mImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view ==(RelativeLayout)o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_slider_viewpager, container, false);
        final ImageView imageView = (ImageView) view.findViewById(R.id.viewpager_image);
        Glide.with(context).load(mImages[position]).into(imageView);
        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);

    }
}
