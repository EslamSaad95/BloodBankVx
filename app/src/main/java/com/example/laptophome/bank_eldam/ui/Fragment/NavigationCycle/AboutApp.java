package com.example.laptophome.bank_eldam.ui.Fragment.NavigationCycle;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.bank_eldam.data.model.settings.Settings;
import com.example.laptophome.bank_eldam.data.rest.ApiService;
import com.example.laptophome.bank_eldam.data.rest.RetrofitClient;
import com.example.laptophome.bank_eldam.helper.Constant;
import com.example.laptophome.bank_eldam.helper.HelperMethod;
import com.example.laptophome.bank_eldam.ui.activity.NavigationViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AboutApp extends Fragment {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_iv_notification)
    ImageView toolbarIvNotification;
    @BindView(R.id.toolbar_tv_notification_number)
    TextView toolbarTvNotificationNumber;
    @BindView(R.id.toolbar_notification_layout)
    FrameLayout toolbarNotificationLayout;
    @BindView(R.id.toolbar_iv_back)
    ImageView toolbarIvArrow;
    @BindView(R.id.apptoolbar)
    Toolbar apptoolbar;
    @BindView(R.id.aboutapp_tv_body)
    TextView aboutappTvBody;
    Unbinder unbinder;
    ApiService apiService;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigationcycle_about_app, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((NavigationViewActivity)getActivity()).setSupportActionBar(apptoolbar);
        Toolbar toolbar=(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        progressDialog=HelperMethod.showProgressDialog(getContext());
        apiService = RetrofitClient.getclient().create(ApiService.class);
        aboutapp();
        HelperMethod.toolbar(toolbarNotificationLayout,true,toolbarIvArrow,false,toolbarTitle, getString(R.string.about_app));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void aboutapp()
    {        String token = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.AppToken_keyname, getContext());
            apiService.getSettings(token).enqueue(new Callback<Settings>() {
                @Override
                public void onResponse(Call<Settings> call, Response<Settings> response) {
                    progressDialog.dismiss();
                    try {

                        if(response.body().getStatus().equals(1))
                        {
                            aboutappTvBody.setText(response.body().getData().getAboutApp());
                        }
                        else
                        {
                            Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e)
                    {}
                }

                @Override
                public void onFailure(Call<Settings> call, Throwable t) {
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

                }
            });
    }
}
