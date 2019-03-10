package com.example.laptophome.bank_eldam.ui.Fragment.NavigationCycle;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.bank_eldam.data.model.bloodtypelist.BloodtypeList;
import com.example.laptophome.bank_eldam.data.model.governorates.Governorates;
import com.example.laptophome.bank_eldam.data.model.notificationsettings.NotificationSetting;
import com.example.laptophome.bank_eldam.data.rest.ApiService;
import com.example.laptophome.bank_eldam.data.rest.RetrofitClient;
import com.example.laptophome.bank_eldam.helper.Constant;
import com.example.laptophome.bank_eldam.helper.HelperMethod;
import com.example.laptophome.bank_eldam.ui.activity.NavigationViewActivity;

import java.util.ArrayList;
import java.util.logging.ConsoleHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationSettings extends Fragment {


    Unbinder unbinder;
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
    @BindView(R.id.notfication_setting_tv_header)
    TextView notficationSettingTvHeader;
    @BindView(R.id.navigation_setting_tv_types_titles)
    TextView navigationSettingTvTypesTitles;
    @BindView(R.id.notification_setting_cv_bloodtypes)
    CardView notificationSettingCvBloodtypes;
    @BindView(R.id.notfication_setting_tv_govrn)
    TextView notficationSettingTvGovrn;
    @BindView(R.id.notification_setting_cv_govern)
    CardView notificationSettingCvGovern;
    @BindView(R.id.notification_setting_btn_save)
    Button notificationSettingBtnSave;
    ApiService apiService;
    @BindView(R.id.notfication_setting_Anegative_cb)
    CheckBox notficationSettingAnegativeCb;
    @BindView(R.id.notfication_setting_Apoisitive_cb)
    CheckBox notficationSettingApoisitiveCb;
    @BindView(R.id.notfication_setting_Opoisitive_cb)
    CheckBox notficationSettingOpoisitiveCb;
    @BindView(R.id.notfication_setting_ABnegative_cb)
    CheckBox notficationSettingABnegativeCb;
    @BindView(R.id.notfication_setting_ABpoisitive_cb)
    CheckBox notficationSettingABpoisitiveCb;
    @BindView(R.id.notfication_setting_Onegative_cb)
    CheckBox notficationSettingOnegativeCb;
    @BindView(R.id.notfication_setting_Bnegative_cb)
    CheckBox notficationSettingBnegativeCb;
    @BindView(R.id.notfication_setting_Bpositive_cb)
    CheckBox notficationSettingBPositiveCb;
    @BindView(R.id.notification_setting_dakhalia_cb)
    CheckBox notificationSettingDakhaliaCb;
    @BindView(R.id.notification_setting_munfya_cb)
    CheckBox notificationSettingMunfyaCb;
    @BindView(R.id.notification_setting_gharbia_cb)
    CheckBox notificationSettingGharbiaCb;
    @BindView(R.id.notification_setting_kafr_elshekh_cb)
    CheckBox notificationSettingKafr_elshekhCb;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_navigationcycle_notification, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        ((NavigationViewActivity) getActivity()).setSupportActionBar(apptoolbar);
        getnotificationsettings();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        HelperMethod.toolbar(toolbarNotificationLayout, true, toolbarIvArrow, false, toolbarTitle, getString(R.string.notification_Setting));
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void getnotificationsettings() {
        String api = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.AppToken_keyname, getContext());
        final CheckBox[] checkBoxes = new CheckBox[]{notficationSettingOpoisitiveCb, notficationSettingOnegativeCb,
                                                      notficationSettingApoisitiveCb,notficationSettingAnegativeCb,
                                                        notficationSettingBPositiveCb ,notficationSettingBnegativeCb,
                                                 notficationSettingABpoisitiveCb  ,  notficationSettingABnegativeCb
                };
        final CheckBox[]governorates=new CheckBox[]{notificationSettingDakhaliaCb,notificationSettingGharbiaCb,
                notificationSettingMunfyaCb, notificationSettingKafr_elshekhCb};
        apiService.getbloodtypes().enqueue(new Callback<BloodtypeList>() {
            @Override
            public void onResponse(Call<BloodtypeList> call, Response<BloodtypeList> response) {
                if(response.body().getStatus().equals(1))
                {
                    for(int i=0;i<response.body().getData().size();i++)
                    {
                        checkBoxes[i].setText(response.body().getData().get(i).getName());

                    }


                }
            }

            @Override
            public void onFailure(Call<BloodtypeList> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
        apiService.getGovernorates().enqueue(new Callback<Governorates>() {
            @Override
            public void onResponse(Call<Governorates> call, Response<Governorates> response) {
                if(response.body().getStatus().equals(1))
                {
                    for(int i=0;i<response.body().getData().size();i++)
                    {
                        governorates[i].setText(response.body().getData().get(i).getName());
                    }
                }
                else
                {
                    Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Governorates> call, Throwable t) {

            }
        });
        apiService.getNotificationSettings(api).enqueue(new Callback<NotificationSetting>() {
            @Override
            public void onResponse(Call<NotificationSetting> call, Response<NotificationSetting> response) {
                if(response.body().getStatus().equals(1))
                {
                    for(int i=0;i<response.body().getData().getBloodTypes().size();i++)
                    {
                        checkBoxes[Integer.parseInt(response.body().getData().getBloodTypes().get(i))-1].setChecked(true);
                    }
                    for(int j=0;j<response.body().getData().getGovernorates().size();j++)
                    {
                        governorates[ Integer.parseInt((String) response.body().getData().getGovernorates().get(j))-1].setChecked(true);
                    }
                }
                else
                {
                    Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationSetting> call, Throwable t) {
                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }

    public void setsetting() {
        String api = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.AppToken_keyname, getContext());
        final CheckBox[] checkBoxes = new CheckBox[]{notficationSettingOpoisitiveCb, notficationSettingOnegativeCb,
                notficationSettingApoisitiveCb,notficationSettingAnegativeCb,
                notficationSettingBPositiveCb ,notficationSettingBnegativeCb,
                notficationSettingABpoisitiveCb  ,  notficationSettingABnegativeCb
        };
        final CheckBox[]governorates=new CheckBox[]{notificationSettingDakhaliaCb,notificationSettingGharbiaCb,
                notificationSettingMunfyaCb, notificationSettingKafr_elshekhCb};

        ArrayList<String> bloodtypeList = new ArrayList<>();
        ArrayList<String> governorateslist = new ArrayList<>();
        for (int i = 0; i < checkBoxes.length; i++) {
                CheckBox c=checkBoxes[i];
            if (c.isChecked()) {
                bloodtypeList.add(String.valueOf(i+1));
            }
            for(int j=0;j<governorates.length;j++) {
                CheckBox cc=governorates[j];
                if (cc.isChecked()) {
                    governorateslist.add(String.valueOf(j + 1));

                }
            }
        }
        apiService.setNotiicationSettings(api,bloodtypeList,governorateslist)
                .enqueue(new Callback<NotificationSetting>() {
                    @Override
                    public void onResponse(Call<NotificationSetting> call, Response<NotificationSetting> response) {
                        progressDialog.dismiss();
                        if(response.body().getStatus().equals(1))
                        {
                                startActivity(new Intent(getContext(),NavigationViewActivity.class));
                        }
                        else
                        {
                            Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationSetting> call, Throwable t) {
                            Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                    }
                });


    }

    @OnClick(R.id.notification_setting_btn_save)
    public void onViewClicked() {
        setsetting();
        progressDialog=HelperMethod.showProgressDialog(getContext());
    }
}
