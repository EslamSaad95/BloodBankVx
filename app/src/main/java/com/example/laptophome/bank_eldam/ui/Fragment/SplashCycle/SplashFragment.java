package com.example.laptophome.bank_eldam.ui.Fragment.SplashCycle;



import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.helper.HelperMethod;


public class SplashFragment extends Fragment  {


    private  final int SPLASH_DISPLAY_LENGTH = 3000;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_splash, container, false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SliderFragment sliderFragment=new SliderFragment();
                HelperMethod.replace(sliderFragment,getActivity().getSupportFragmentManager(),R.id.splash_activity_root);

            }
        },SPLASH_DISPLAY_LENGTH);




return view;
    }




}
