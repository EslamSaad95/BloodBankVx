package com.example.laptophome.bank_eldam.ui.Fragment.SplashCycle;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.laptophome.bank_eldam.Adapter.ViewPagerSliderFr;
import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.ui.activity.UserActivty;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;


public class SliderFragment extends Fragment {

    Unbinder unbinder;
    ViewPager viewPager;
    @BindView(R.id.slider_fr_view_pager)
    ViewPager sliderFrViewPager;

    @BindView(R.id.splash_fm_bn_skip)
    Button splashFmBnSkip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slider, container, false);
        TabLayout tabDots=view.findViewById(R.id.tabDots);
        viewPager = view.findViewById(R.id.slider_fr_view_pager);
        ViewPagerSliderFr viewPagerSliderFr = new ViewPagerSliderFr(getActivity().getApplicationContext());
        viewPager.setAdapter(viewPagerSliderFr);
        tabDots.setupWithViewPager(viewPager,false);


        // onViewClicked();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.splash_fm_bn_skip)
    public void onViewClicked() {
        Intent i = new Intent(getContext(), UserActivty.class);
        startActivity(i);

    }
}
