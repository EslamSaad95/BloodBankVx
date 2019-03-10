package com.example.laptophome.bank_eldam.ui.Fragment.UserCycle;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.model.retrivepassword.Retrivepassword;
import com.example.laptophome.bank_eldam.data.rest.ApiService;
import com.example.laptophome.bank_eldam.data.rest.RetrofitClient;
import com.example.laptophome.bank_eldam.helper.HelperMethod;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgetpasswordFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.forgetpass_fr_iv_logo)
    ImageView forgetpassFrIvLogo;
    @BindView(R.id.forgetpass_fr_tv_forgetpassword)
    TextView forgetpassFrTvForgetpassword;
    @BindView(R.id.forgetpass_fr_ed_phnumber)
    TextInputLayout forgetpassPhnumber_edittext;
    @BindView(R.id.forgetpass_fr_bn_sendpass)
    Button forgetpassFrBnSendpass;
    ApiService apiService;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgetpassword, container, false);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.forgetpass_fr_bn_sendpass)
    public void onViewClicked() {
        progressDialog=HelperMethod.showProgressDialog(getContext());
        Getpassword();

    }

    public void Getpassword()
    {
        final String phonenumber=forgetpassPhnumber_edittext.getEditText().getText().toString().trim();
        apiService.resetPassword(phonenumber).enqueue(new Callback<Retrivepassword>() {
            @Override
            public void onResponse(Call<Retrivepassword> call, Response<Retrivepassword> response) {
                progressDialog.dismiss();
              try {
                  if(response.isSuccessful())
                  {
                      Retrivepassword retrivepassword=response.body();
                      if(retrivepassword.getStatus().equals(1))
                      {
                          ConfirmarionFragment confrmpass = new ConfirmarionFragment();
                          Bundle b=new Bundle();
                          b.putString("userphone",phonenumber);
                          confrmpass.setArguments(b);
                          HelperMethod.replace(confrmpass, getActivity().getSupportFragmentManager(), R.id.user_activity_root);
                      }
                      else
                      {
                          Toast.makeText(getContext(),retrivepassword.getMsg(),Toast.LENGTH_LONG).show();
                      }
                  }
              }
              catch (Exception e)
              {

              }
            }

            @Override
            public void onFailure(Call<Retrivepassword> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
}
