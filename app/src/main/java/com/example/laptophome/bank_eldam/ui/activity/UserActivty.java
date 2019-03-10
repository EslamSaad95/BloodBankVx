package com.example.laptophome.bank_eldam.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.helper.HelperMethod;
import com.example.laptophome.bank_eldam.ui.Fragment.UserCycle.LoginFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserActivty extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);

        LoginFragment loginFragment = new LoginFragment();
        HelperMethod.replace(loginFragment, getSupportFragmentManager(), R.id.user_activity_root);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    public void hidekeyboard(View view) {
       HelperMethod.hidesoftkeyboard(view,this);
    }
}
