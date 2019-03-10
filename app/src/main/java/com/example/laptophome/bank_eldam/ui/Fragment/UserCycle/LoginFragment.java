package com.example.laptophome.bank_eldam.ui.Fragment.UserCycle;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.bank_eldam.data.model.register.Register;
import com.example.laptophome.bank_eldam.data.model.registernotification.RegisterNotification;
import com.example.laptophome.bank_eldam.data.rest.ApiService;
import com.example.laptophome.bank_eldam.data.rest.RetrofitClient;
import com.example.laptophome.bank_eldam.helper.Constant;
import com.example.laptophome.bank_eldam.helper.HelperMethod;
import com.example.laptophome.bank_eldam.ui.activity.NavigationViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.login_fr_et_phnumber)
    TextInputLayout LoginPhnumber_edittext;
    @BindView(R.id.login_fr_et_pass)
    TextInputLayout LoginPassword_edittext;
    @BindView(R.id.login_fr_cb_remember)
    CheckBox RememberPassword_checkbox;
    @BindView(R.id.login_fr_tv_forget_password)
    TextView LoginForgetPassword_textview;
    @BindView(R.id.login_fr_btn_login)
    Button Loginlogin_button;
    @BindView(R.id.logn_fr_btn_signup)
    Button LoginSignup_button;
    ApiService apiService;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getclient().create(ApiService.class);


        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        loadpassword();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void savepassword()
    {
        if(RememberPassword_checkbox.isChecked())
        {
            SharedPrerefrencesManager.SetSharedPrerefrences(Constant.userpassword_keyname,LoginPassword_edittext.getEditText().getText().toString(),getContext());

        }
    }


    @SuppressLint("ResourceAsColor")
    public void loadpassword()
    {
        String userpass=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.userpassword_keyname,getContext());
        LoginPassword_edittext.getEditText().setText(userpass);

    }


    @OnClick({R.id.login_fr_tv_forget_password, R.id.login_fr_btn_login, R.id.logn_fr_btn_signup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_fr_tv_forget_password:
                ForgetpasswordFragment forgetpassword = new ForgetpasswordFragment();
                HelperMethod.replace(forgetpassword, getActivity().getSupportFragmentManager(), R.id.user_activity_root);
                break;
            case R.id.login_fr_btn_login:
                progressDialog=HelperMethod.showProgressDialog(getContext());
                Login();
                RegisterNotificationToken();


                break;
            case R.id.logn_fr_btn_signup:
                RegisterFragment registerFragment = new RegisterFragment();
                HelperMethod.replace(registerFragment, getActivity().getSupportFragmentManager(), R.id.user_activity_root);
                break;
        }
    }
    public void Login()
    {
        String phonenumber=LoginPhnumber_edittext.getEditText().getText().toString().trim();
        String password=LoginPassword_edittext.getEditText().getText().toString().trim();
        apiService.Login(phonenumber,password).enqueue(new Callback<Register>() {

            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                progressDialog.dismiss();
               try {

                   if(response.isSuccessful())
                   {

                       Register register=response.body();
                       {
                           if(register.getStatus().equals(1))
                           {
                                savepassword();
                               SharedPrerefrencesManager.SetSharedPrerefrences(Constant.AppToken_keyname,register.getData().getApiToken(),getContext());
                               startActivity(new Intent(getContext(),NavigationViewActivity.class));
                           }
                           else
                           {
                               Toast.makeText(getContext(),register.getMsg(),Toast.LENGTH_LONG).show();
                           }
                       }
                   }

               }
               catch (Exception e)
               {
               }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();


            }
        });

    }

    public void RegisterNotificationToken()
    {
        String app_token = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.AppToken_keyname, getContext());
        String notification_token=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.NotificationToken_keyname, getContext());

        apiService.RegisterNotification(notification_token,app_token,Constant.platform).enqueue(new Callback<RegisterNotification>() {
            @Override
            public void onResponse(Call<RegisterNotification> call, Response<RegisterNotification> response) {
                try {

                    if(response.body().getStatus().equals(0))
                    {
                        Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception e)
                {}
            }

            @Override
            public void onFailure(Call<RegisterNotification> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
