package com.example.laptophome.bank_eldam.ui.Fragment.HomeCycle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.bank_eldam.data.model.adddonationrequest.AddDonationRequest;
import com.example.laptophome.bank_eldam.data.model.cities.Cities;
import com.example.laptophome.bank_eldam.data.model.notificationcount.NotificationCount;
import com.example.laptophome.bank_eldam.data.rest.ApiService;
import com.example.laptophome.bank_eldam.data.rest.RetrofitClient;
import com.example.laptophome.bank_eldam.helper.Constant;
import com.example.laptophome.bank_eldam.helper.HelperMethod;
import com.example.laptophome.bank_eldam.ui.activity.MapsActivity;
import com.example.laptophome.bank_eldam.ui.activity.NavigationViewActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddDonationRequestFragment extends Fragment implements Constant {

    @BindView(R.id.mkrequest_fr_ev_getlocation)
    TextView mkrequestFrEvGetlocation;
    Unbinder unbinder;
    @BindView(R.id.mkrequest_ed_name)
    TextInputLayout AddrequestName_edittext;
    @BindView(R.id.mkrequest_ed_age)
    TextInputLayout AddrequestAge_edittexr;
    @BindView(R.id.mkrequest_ed_bloodbags)
    TextInputLayout AddrequestBloodbags_edittext;
    @BindView(R.id.mkrequest_tv_bloodtype_txt)
    TextView AddrequestBloodtypetxt_textview;
    @BindView(R.id.mkrequest_sp_bloodtype)
    Spinner Bloodtype_spinner;
    @BindView(R.id.mkrequest_ed_hospitalname)
    TextInputLayout AddrequestHospitalname_edittext;
    @BindView(R.id.mkrequest_fr_ed_hospitaladd)
    TextInputLayout AddrequestHospitaladdress_edittext;
    @BindView(R.id.mkrequest_tv_governsp_txt)
    TextView AddrequestGovernsptxt_textview;
    @BindView(R.id.mkrequest_sp_governorate)
    Spinner Governorate_spinner;
    @BindView(R.id.mkrequest_tv_citysp_txt)
    TextView AddrequestCityspTxt_textview;
    @BindView(R.id.mkrequest_sp_city)
    Spinner Cityspinner;
    @BindView(R.id.mkrequest_ed_phone)
    TextInputLayout AddrequestPhone_edittext;
    @BindView(R.id.mkrequest_ed_notes)
    TextInputLayout AddrequestNotes_edittext;
    ApiService apiService;
    ArrayAdapter<String> CitiesSp_arrayAdapter;
    List<String> GovernorateList = new ArrayList<>();
    List<String>BloodtypeList=new ArrayList<>();
    List<String>CitiesList;
    ArrayAdapter<String> GovernoratesSp_arrayAdapter;
    ArrayAdapter<String> Bloodtype_arrayAdapter;
    @BindView(R.id.mkrequest_btn_sendrequest)
    Button mkrequestBtnSendrequest;
    ProgressDialog progressDialog;
    int governorate_id, city_id;
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
    @BindView(R.id.mkrequest_layout_bloodtype)
    RelativeLayout mkrequestLayoutBloodtype;
    @BindView(R.id.mkrequest_layout_hospitaladd)
    LinearLayout mkrequestLayoutHospitaladd;
    @BindView(R.id.mkrequest_layout_govern)
    RelativeLayout mkrequestLayoutGovern;
    @BindView(R.id.mkrequest_layout_city)
    RelativeLayout mkrequestLayoutCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_donationrequest, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        ((NavigationViewActivity)getActivity()).setSupportActionBar(apptoolbar);
        Toolbar toolbar=(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        HelperMethod.toolbar(toolbarNotificationLayout,false,toolbarIvArrow,false,toolbarTitle,getString(R.string.add_donation));
        HelperMethod.get_governorate(getContext(), GovernorateList, GovernoratesSp_arrayAdapter, Governorate_spinner, AddrequestGovernsptxt_textview);
        HelperMethod.get_bloodtypes(false,getContext(),BloodtypeList,Bloodtype_arrayAdapter,Bloodtype_spinner,AddrequestBloodtypetxt_textview);
        HelperMethod.NotificationNumber(getContext(),toolbarTvNotificationNumber,toolbarNotificationLayout);
        Governorate_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               CitiesList = new ArrayList<>();
                governorate_id = (int) (Governorate_spinner.getSelectedItemId()+1);
                apiService.getCities(governorate_id).enqueue(new Callback<Cities>() {
                    @Override
                    public void onResponse(Call<Cities> call, final Response<Cities> response) {
                        if(response.body().getStatus().equals(1))
                        {
                            for(int i=0;i<response.body().getData().size();i++)
                            {
                                CitiesList.add(response.body().getData().get(i).getName());
                            }
                            Cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    city_id=response.body().getData().get((int) (Cityspinner.getSelectedItemId())).getId();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            HelperMethod.spinneradapter(false,getContext(),Cityspinner,CitiesSp_arrayAdapter,AddrequestCityspTxt_textview,CitiesList);

                        }

                        else
                        {
                            Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Cities> call, Throwable t) {
                        Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.mkrequest_fr_ev_getlocation, R.id.mkrequest_btn_sendrequest})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mkrequest_fr_ev_getlocation:
                startActivity(new Intent(getContext(), MapsActivity.class));
                break;
            case R.id.mkrequest_btn_sendrequest:
                Toast.makeText(getContext(), String.valueOf(MapsActivity.Latitude), Toast.LENGTH_LONG).show();
                progressDialog = HelperMethod.showProgressDialog(getContext());
                AddDonationRequest();
                break;
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            AddrequestName_edittext.getEditText().setText(savedInstanceState.getString("name"));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mkrequestBtnSendrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDonationRequest();
            }
        });
    }

    public void AddDonationRequest() {
        String token = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.AppToken_keyname, getContext());
        String name = AddrequestName_edittext.getEditText().getText().toString().trim();
        int age = Integer.parseInt(AddrequestAge_edittexr.getEditText().getText().toString().trim());
        int bloodbags = Integer.parseInt(AddrequestBloodbags_edittext.getEditText().getText().toString().trim());
        String hospitalname = AddrequestHospitalname_edittext.getEditText().getText().toString().trim();
        String hospitaladdress = AddrequestHospitaladdress_edittext.getEditText().getText().toString().trim();
        String phonenumber = AddrequestPhone_edittext.getEditText().getText().toString().trim();
        String notes = AddrequestNotes_edittext.getEditText().getText().toString().trim();
        int bloodid=(int)(Bloodtype_spinner.getSelectedItemId()+1);
        apiService.AddDonationRequest(token, name, age, bloodid, bloodbags, hospitalname, hospitaladdress, city_id, phonenumber, notes, MapsActivity.Latitude, MapsActivity.longtitude)
                .enqueue(new Callback<AddDonationRequest>() {
                    @Override
                    public void onResponse(Call<AddDonationRequest> call, Response<AddDonationRequest> response) {
                        try {
                            if (response.body().getStatus().equals(1)) {
                                Toast.makeText(getContext(), "Your Request has been sent", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onFailure(Call<AddDonationRequest> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

    }


}
