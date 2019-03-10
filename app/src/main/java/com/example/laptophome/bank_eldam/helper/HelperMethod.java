package com.example.laptophome.bank_eldam.helper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.bank_eldam.data.model.bloodtypelist.BloodtypeList;
import com.example.laptophome.bank_eldam.data.model.cities.Cities;
import com.example.laptophome.bank_eldam.data.model.governorates.Governorates;
import com.example.laptophome.bank_eldam.data.model.notificationcount.NotificationCount;
import com.example.laptophome.bank_eldam.data.rest.ApiService;
import com.example.laptophome.bank_eldam.data.rest.RetrofitClient;
import com.example.laptophome.bank_eldam.ui.Fragment.HomeCycle.Home_Fragment;
import com.example.laptophome.bank_eldam.ui.Fragment.HomeCycle.NotificationFragment;
import com.example.laptophome.bank_eldam.ui.activity.NavigationViewActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelperMethod {
    static ApiService apiService;



    public static void replace(Fragment fragment, FragmentManager supportfragment, int id) {
        FragmentTransaction transaction = supportfragment.beginTransaction();
        transaction.replace(id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public static void setdatepicker(DatePickerDialog datePickerDialog, final TextView textView, Context context) {
        final Calendar calendar = Calendar.getInstance();
        final StringBuilder stringBuilder = new StringBuilder();
        final int year = calendar.get(Calendar.YEAR);
        int months = calendar.get(Calendar.MONTH);
        // String monthstring=String.valueOf(months);


        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String daystring = String.valueOf(dayOfMonth);
                int actual_month = month + 1;//as month begin with index 0
                String monthstring = String.valueOf(actual_month);

                if (daystring.length() == 1) {
                    daystring = "0" + daystring;
                }
                if (monthstring.length() == 1) {
                    monthstring = "0" + monthstring;
                }

                stringBuilder.append(year).append("-").append(monthstring).append("-").append(daystring);
                textView.setText(stringBuilder);

            }
        }, year, months, day);
        datePickerDialog.show();
    }

    public static void spinneradapter(boolean smallspinner, Context context, final Spinner spinner, ArrayAdapter<String> arrayAdapter, final TextView txt, final List<String> list) {   ////you must add element to list
        arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        if (arrayAdapter != null) {
            if (smallspinner == false) {
                txt.setVisibility(View.GONE);
            } else {
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        txt.setText(spinner.getSelectedItem().toString());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }



    }

    public static ProgressDialog showProgressDialog(Context context) {
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage(context.getString(R.string.loading));
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        return m_Dialog;
    }

    public static void  get_governorate( final Context context, final List<String> GovernoratesList, final ArrayAdapter<String> GovernorateSp_arrayAdapter
            , final Spinner spinner, final TextView spinnertextview) {
        //////////////////////Get ////////////////////////
        apiService = RetrofitClient.getclient().create(ApiService.class);
        apiService.getGovernorates().enqueue(new Callback<Governorates>() {
            @Override
            public void onResponse(Call<Governorates> call, Response<Governorates> response) {
                for (int i = 0; i < response.body().getData().size(); i++) {
                    GovernoratesList.add(response.body().getData().get(i).getName());

                }
                HelperMethod.spinneradapter(false, context, spinner, GovernorateSp_arrayAdapter, spinnertextview, GovernoratesList);
            }

            @Override
            public void onFailure(Call<Governorates> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public static void get_bloodtypes(final boolean small, final Context context, final  List<String> BloodtypeList, final  ArrayAdapter<String>BloodtypeSp_arrayAdapter
            , final Spinner spinner, final  TextView spinnertextview)
    {
        apiService = RetrofitClient.getclient().create(ApiService.class);
        apiService.getbloodtypes().enqueue(new Callback<com.example.laptophome.bank_eldam.data.model.bloodtypelist.BloodtypeList>() {
            @Override
            public void onResponse(Call<com.example.laptophome.bank_eldam.data.model.bloodtypelist.BloodtypeList> call, Response<com.example.laptophome.bank_eldam.data.model.bloodtypelist.BloodtypeList> response) {
                try
                {
                    for(int i=0;i<response.body().getData().size();i++)
                    {
                        BloodtypeList.add(response.body().getData().get(i).getName());
                    }
                    if(small==false) {
                        HelperMethod.spinneradapter(false, context, spinner, BloodtypeSp_arrayAdapter, spinnertextview, BloodtypeList);
                    }
                    else
                    {
                        HelperMethod.spinneradapter(true, context, spinner, BloodtypeSp_arrayAdapter, spinnertextview, BloodtypeList);
                    }

                }
                catch ( Exception e)
                {}
            }

            @Override
            public void onFailure(Call<com.example.laptophome.bank_eldam.data.model.bloodtypelist.BloodtypeList> call, Throwable t) {

            }
        });
    }



    public  static void toolbar(final FrameLayout notificationicon, boolean notficationhide, ImageView arrowicon, boolean arrowhide,
                                TextView title, String titletxt)
    {
        if(title!=null)
        {
            title.setText(titletxt);
        }

        if(notficationhide==true)
        {
            notificationicon.setVisibility(View.GONE);
        }
        if(arrowhide==true)
        {
            arrowicon.setVisibility(View.GONE);
        }
        if(notficationhide==false)
        {
            notificationicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                    NotificationFragment notificationFragment = new NotificationFragment();
                    HelperMethod.replace(notificationFragment,((FragmentActivity) v.getContext()).getSupportFragmentManager(), R.id.navigation_activity_root);
                }
            });
        }
        if(arrowhide==false)
        {
            arrowicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   v.getContext().startActivity(new Intent(v.getContext(),NavigationViewActivity.class));


                }
            });
        }
    }

    public  static void hidesoftkeyboard(View view,Activity activity)
    {
        InputMethodManager inputMethodManager=(InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public static void NotificationNumber(final Context context, final TextView Notificationnumbertxt, final FrameLayout NotificationLayout)
    { String token=SharedPrerefrencesManager.GetSharedPrerefrences(Constant.AppToken_keyname,context.getApplicationContext());
        apiService.getNotificationNumber(token).enqueue(new Callback<NotificationCount>() {
            @Override
            public void onResponse(Call<NotificationCount> call, final Response<NotificationCount> response) {
                final NotificationCount NotificationNumber=response.body();
                try
                {
                    if(NotificationNumber.getStatus().equals(1))
                    {
                        if(NotificationNumber.getData().getNotificationsCount()>0) {
                            Notificationnumbertxt.setVisibility(View.VISIBLE);
                            Notificationnumbertxt.setText(NotificationNumber.getData().getNotificationsCount().toString());

                        }
                    }
                    else
                    {
                        Toast.makeText(context.getApplicationContext(),NotificationNumber.getMsg(),Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {}


            }

            @Override
            public void onFailure(Call<NotificationCount> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }



}