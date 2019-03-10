package com.example.laptophome.bank_eldam.ui.Fragment.UserCycle;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.bank_eldam.data.model.cities.Cities;
import com.example.laptophome.bank_eldam.data.model.governorates.Governorates;
import com.example.laptophome.bank_eldam.data.model.register.Register;
import com.example.laptophome.bank_eldam.data.rest.ApiService;
import com.example.laptophome.bank_eldam.data.rest.RetrofitClient;
import com.example.laptophome.bank_eldam.helper.Constant;
import com.example.laptophome.bank_eldam.helper.HelperMethod;
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

public class RegisterFragment extends Fragment implements Constant {


    Unbinder unbinder;
    @BindView(R.id.register_et_name)
    TextInputLayout RegisterName_edittext;
    @BindView(R.id.register_et_mail)
    TextInputLayout RegisterMail_edittext;
    @BindView(R.id.register_et_phone_number)
    TextInputLayout RegisterPhoneNumber_edittext;
    @BindView(R.id.register_et_password)
    TextInputLayout RegisterPassword_edittext;
    @BindView(R.id.register_et_confirm_pass)
    TextInputLayout RegisterConfirmPass_edittext;
    @BindView(R.id.register_btn_register)
    Button RegisterRegister_Button;
    @BindView(R.id.register_tv_governsp_txt)
    TextView GovernoratesSp_textview;
    @BindView(R.id.register_sp_governorate)
    Spinner Governorate_spinner;
    @BindView(R.id.register_tv_citysp_txt)
    TextView CitySp_textview;
    @BindView(R.id.register_sp_city)
    Spinner Cities_spinner;
    ProgressDialog progressDialog;
    ApiService apiService;
    /////Spinner
    ArrayAdapter<String> GovernorateSp_arrayAdapter;
    ArrayAdapter<String> CitiesSp_arrayAdapter;
    List<String> CitiesList;
    final List<String> GovernoratesList = new ArrayList<>();
    final List<String> BloodtypeList=new ArrayList<>();
    /////////////
    //////// date picker
    DatePickerDialog Lastdonation_datePicker, dateofbirth_datepicker;
    @BindView(R.id.register_tv_dateofbirth)
    TextView RegisterDateofbirth_textview;
    @BindView(R.id.register_tv_lastdonation)
    TextView RegisterLastdonation_textview;
    @BindView(R.id.register_tv_blood_txt)
    TextView registerBlood_textview;
    @BindView(R.id.register_sp_blood)
    Spinner BloodType_spinner;
    ArrayAdapter<String> bloodSp_ArrayAdapter;
    int governorate_id,cityid,bloodtypeid;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_iv_notification)
    ImageView toolbarIvArrow;
    @BindView(R.id.toolbar_tv_notification_number)
    TextView toolbarTvNotificationNumber;
    @BindView(R.id.toolbar_notification_layout)
    FrameLayout toolbarNotificationLayout;
    @BindView(R.id.toolbar_iv_back)
    ImageView toolbarIvBack;
    @BindView(R.id.apptoolbar)
    Toolbar apptoolbar;
    @BindView(R.id.regiseter_sp_blood_layout)
    RelativeLayout regiseterSpBloodLayout;
    @BindView(R.id.regiseter_sp_govern_layout)
    RelativeLayout regiseterSpGovernLayout;
    @BindView(R.id.regiseter_sp_city_layout)
    RelativeLayout regiseterSpCityLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        HelperMethod.toolbar(toolbarNotificationLayout, true, toolbarIvArrow, true,toolbarTitle,getString(R.string.registeration_btn) );
        HelperMethod.get_governorate(getContext(), GovernoratesList, GovernorateSp_arrayAdapter, Governorate_spinner, GovernoratesSp_textview);
        HelperMethod.get_bloodtypes(false,getContext(),BloodtypeList,bloodSp_ArrayAdapter,BloodType_spinner,registerBlood_textview);

       Governorate_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CitiesList= new ArrayList<>();
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
                            Cities_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    cityid=response.body().getData().get((int) (Cities_spinner.getSelectedItemId())).getId();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                            HelperMethod.spinneradapter(false,getContext(),Cities_spinner,CitiesSp_arrayAdapter,CitySp_textview,CitiesList);

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
    @OnClick({R.id.register_tv_dateofbirth, R.id.register_tv_lastdonation, R.id.register_btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_tv_dateofbirth:
                HelperMethod.setdatepicker(dateofbirth_datepicker, RegisterDateofbirth_textview, getContext());
                break;
            case R.id.register_tv_lastdonation:
                HelperMethod.setdatepicker(Lastdonation_datePicker, RegisterLastdonation_textview, getContext());
                break;
            case R.id.register_btn_register:
                progressDialog = HelperMethod.showProgressDialog(getContext());
                Registeration();
                break;
        }
    }


    public void Registeration() {
        String name = RegisterName_edittext.getEditText().getText().toString().trim();
        String email = RegisterMail_edittext.getEditText().getText().toString().trim();

        String password = RegisterPassword_edittext.getEditText().getText().toString().trim();
        String confirmpassword = RegisterConfirmPass_edittext.getEditText().getText().toString().trim();
        String dateofbirth = RegisterDateofbirth_textview.getText().toString().trim();
        String Lastdonation = RegisterLastdonation_textview.getText().toString().trim();
        String phonenumber = RegisterPhoneNumber_edittext.getEditText().getText().toString().trim();
        bloodtypeid = (int) BloodType_spinner.getSelectedItemId() + 1;


        apiService.Register(name, email, dateofbirth, cityid, phonenumber, Lastdonation, password, confirmpassword, bloodtypeid)
                .enqueue(new Callback<Register>() {
                    @Override
                    public void onResponse(Call<Register> call, Response<Register> response) {
                        progressDialog.dismiss();
                        try {

                            if (response.isSuccessful()) {
                                Register register = response.body();
                                if (register.getStatus().equals(1)) {
                                    SharedPrerefrencesManager.SetSharedPrerefrences(Constant.userpassword_keyname,RegisterPassword_edittext.getEditText().getText().toString(),getContext());
                                    SharedPrerefrencesManager.SetSharedPrerefrences(Constant.AppToken_keyname,register.getData().getApiToken(),getContext());
                                    startActivity(new Intent(getContext(), NavigationViewActivity.class));

                                } else {
                                    Toast.makeText(getContext(), register.getMsg(), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(Call<Register> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}
