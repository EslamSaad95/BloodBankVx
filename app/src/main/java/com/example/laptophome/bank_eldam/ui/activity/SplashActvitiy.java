package com.example.laptophome.bank_eldam.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.ui.Fragment.SplashCycle.SplashFragment;
import com.example.laptophome.bank_eldam.helper.HelperMethod;

public class SplashActvitiy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SplashFragment splashFragment=new SplashFragment();
        HelperMethod.replace(splashFragment,getSupportFragmentManager(),R.id.splash_activity_root);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
