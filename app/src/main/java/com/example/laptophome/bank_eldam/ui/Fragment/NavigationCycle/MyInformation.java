package com.example.laptophome.bank_eldam.ui.Fragment.NavigationCycle;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.bank_eldam.data.model.cities.Cities;
import com.example.laptophome.bank_eldam.data.model.editprofile.EditProfile;
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


public class MyInformation extends Fragment {

    @BindView(R.id.myinfo_et_name)
    TextInputLayout myinfoName_edittext;
    @BindView(R.id.myinfo_et_email)
    TextInputLayout myinfoEmail_edittext;
    @BindView(R.id.myinfo_tv_dateofbirth)
    TextView myinfoDateofbirth_textview;
    @BindView(R.id.myinfo_tv_bloodtypesp_txt)
    TextView myinfoBloodtypespTxt;
    @BindView(R.id.myinfo_SpBloodtype)
    Spinner myinfoBloodtype_spinner;
    @BindView(R.id.myinfo_tv_lastdonation)
    TextView myinfoLastdonation_textview;
    @BindView(R.id.myinfo_tv_govrnsp_txt)
    TextView myinfoTvGovrnspTxt;
    @BindView(R.id.myinfo_spgovernorate)
    Spinner myinfoGovernorate_spinner;
    @BindView(R.id.myinfo_tv_citysp_txt)
    TextView myinfoTvCityspTxt;
    @BindView(R.id.myinfo_sp_city)
    Spinner myinfoCity_spinner;
    @BindView(R.id.myinfo_et_phone)
    TextInputLayout myinfoPhone_edittext;
    @BindView(R.id.myinfo_et_newpass)
    TextInputLayout myinfoNewpass_edittext;
    @BindView(R.id.myinfo_et_confirm_pass)
    TextInputLayout myinfoConfirmPass_edittext;
    @BindView(R.id.myinfo_btn_update)
    Button myinfoBtnUpdate;
    ApiService apiService;
    List<String> GovernorateList = new ArrayList<>();
    List<String> BloodtypeList=new ArrayList<>();

    ArrayAdapter<String> CitySp_ArrayAdapter;
    ArrayAdapter<String> GovernorateSp_ArrayAdapter;
    ArrayAdapter<String> BloodTypeSp_ArrayAdapter;

    DatePickerDialog dataofbirth_picker, lastdonaion_picker;

    int City_Id, Governorate_Id;
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

    @BindView(R.id.myinfo_sp_govern_layout)
    RelativeLayout myinfoSpGovernLayout;
    ProgressDialog progressDialog;
    int didi;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigationcycle_myinfo, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getclient().create(ApiService.class);

        ((NavigationViewActivity)getActivity()).setSupportActionBar(apptoolbar);
        Toolbar toolbar=(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        HelperMethod.toolbar(toolbarNotificationLayout,true,toolbarIvArrow,true,toolbarTitle,getString(R.string.update_myinformation));
        progressDialog=HelperMethod.showProgressDialog(getContext());
        HelperMethod.get_bloodtypes(false,getContext(),BloodtypeList,BloodTypeSp_ArrayAdapter,myinfoBloodtype_spinner,myinfoBloodtypespTxt);
        HelperMethod.get_governorate(getContext(), GovernorateList, GovernorateSp_ArrayAdapter, myinfoGovernorate_spinner, myinfoTvGovrnspTxt);
        myinfoGovernorate_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final List<String>CityList= new ArrayList<>();
                Governorate_Id = (int) (myinfoGovernorate_spinner.getSelectedItemId()+1);
                apiService.getCities(Governorate_Id).enqueue(new Callback<Cities>() {
                    @Override
                    public void onResponse(Call<Cities> call, final Response<Cities> response) {
                        if(response.body().getStatus().equals(1))
                        {
                            for(int i=0;i<response.body().getData().size();i++)
                            {
                                CityList.add(response.body().getData().get(i).getName());

                            }
                            myinfoCity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    City_Id=response.body().getData().get((int) (myinfoCity_spinner.getSelectedItemId())).getId();


                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                            HelperMethod.spinneradapter(false,getContext(),myinfoCity_spinner,CitySp_ArrayAdapter,myinfoTvCityspTxt,CityList);

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
        Getmyinfo();





        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.myinfo_tv_dateofbirth, R.id.myinfo_tv_lastdonation, R.id.myinfo_btn_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.myinfo_tv_dateofbirth:
                HelperMethod.setdatepicker(dataofbirth_picker, myinfoDateofbirth_textview, getContext());
                break;
            case R.id.myinfo_tv_lastdonation:
                HelperMethod.setdatepicker(lastdonaion_picker, myinfoLastdonation_textview, getContext());
                break;
            case R.id.myinfo_btn_update:
                Updatemyinfo();
                City_Id=0;
                break;
        }
    }


    public void Updatemyinfo() {
        String token = SharedPrerefrencesManager.GetSharedPrerefrences("token", getContext());
        String name=myinfoName_edittext.getEditText().getText().toString();
        String mail=myinfoEmail_edittext.getEditText().getText().toString();
        String birthday=myinfoDateofbirth_textview.getText().toString();
        String lastdonation=myinfoLastdonation_textview.getText().toString();
        String phone=myinfoPhone_edittext.getEditText().getText().toString();
        String password=myinfoNewpass_edittext.getEditText().getText().toString();
        String confirmpass=myinfoConfirmPass_edittext.getEditText().getText().toString();
        int bloodid= (int) (myinfoBloodtype_spinner.getSelectedItemId()+1);
        apiService.editProfile(name,mail,birthday,City_Id,phone,lastdonation,password,confirmpass,bloodid,token).enqueue(new Callback<EditProfile>() {
            @Override
            public void onResponse(Call<EditProfile> call, Response<EditProfile> response) {

                try {

                    if(response.body().getStatus().equals(1))
                    {
                        SharedPrerefrencesManager.SetSharedPrerefrences("userpass",myinfoConfirmPass_edittext.getEditText().getText().toString(),getContext());
                        startActivity(new Intent(getContext(),NavigationViewActivity.class));
                    }
                    else
                    {
                        Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {e.printStackTrace();}
            }

            @Override
            public void onFailure(Call<EditProfile> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }


    public void Getmyinfo()
    { final String userpassword= SharedPrerefrencesManager.GetSharedPrerefrences("userpass",getContext());
        String api = SharedPrerefrencesManager.GetSharedPrerefrences("token", getContext());
        apiService.getmyinformation(api).enqueue(new Callback<EditProfile>() {
            @Override
            public void onResponse(Call<EditProfile> call, Response<EditProfile> response) {
                progressDialog.dismiss();
                try {

                    if(response.body().getStatus().equals(1))
                    {
                        myinfoName_edittext.getEditText().setText(response.body().getData().getUser().getName());
                        myinfoEmail_edittext.getEditText().setText(response.body().getData().getUser().getEmail());
                        myinfoDateofbirth_textview.setText(response.body().getData().getUser().getBirthDate());
                        myinfoPhone_edittext.getEditText().setText(response.body().getData().getUser().getPhone());
                        myinfoLastdonation_textview.setText(response.body().getData().getUser().getDonationLastDate());
                        myinfoConfirmPass_edittext.getEditText().setText(userpassword);
                        myinfoNewpass_edittext.getEditText().setText(userpassword);
                        myinfoBloodtype_spinner.setSelection(Integer.parseInt(response.body().getData().getUser().getBloodTypeId())-1);
                        myinfoGovernorate_spinner.setSelection(Integer.parseInt(response.body().getData().getUser().getCity().getGovernorateId())-1);


                    }
                    else
                    {
                        Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {e.printStackTrace();}
            }

            @Override
            public void onFailure(Call<EditProfile> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }


}
