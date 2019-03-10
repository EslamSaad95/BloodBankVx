package com.example.laptophome.bank_eldam.ui.Fragment.UserCycle;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.model.retrivepassword.Retrivepassword;
import com.example.laptophome.bank_eldam.data.rest.ApiService;
import com.example.laptophome.bank_eldam.data.rest.RetrofitClient;
import com.example.laptophome.bank_eldam.helper.HelperMethod;
import com.example.laptophome.bank_eldam.ui.activity.NavigationViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmarionFragment extends Fragment {


    @BindView(R.id.passconfirm_fr_et_confirmcode)
    TextInputLayout passconfirmConfirmcode_edittext;
    @BindView(R.id.passconfirm_fr_et_new_pass)
    TextInputLayout passconfirmNewPass_edittext;
    @BindView(R.id.passconfirm_fr_et_confirm_newpass)
    TextInputLayout passconfirmConfirmNewpass_edittext;
    @BindView(R.id.passconfirm_fr_bn_change_pass)
    Button passconfirmationSubmit_button;
    ApiService apiService;
    Unbinder unbinder;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_passconfrm, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.passconfirm_fr_bn_change_pass)
    public void onViewClicked() {
        progressDialog=HelperMethod.showProgressDialog(getContext());
        PasswordConfirmation();
    }

    public void PasswordConfirmation() {
        String pincode = passconfirmConfirmcode_edittext.getEditText().getText().toString().trim();
        String newpassword = passconfirmNewPass_edittext.getEditText().getText().toString().trim();
        String confirmnewpassword = passconfirmConfirmNewpass_edittext.getEditText().getText().toString().trim();
        Bundle b=getArguments();
        String userphone=b.getString("userphone");
        apiService.newPassword(userphone, pincode, newpassword, confirmnewpassword).enqueue(new Callback<Retrivepassword>() {
            @Override
            public void onResponse(Call<Retrivepassword> call, Response<Retrivepassword> response) {
                progressDialog.dismiss();
                try {

                    if (response.isSuccessful()) {
                        Retrivepassword retrivepassword = response.body();
                        if (retrivepassword.getStatus().equals(1)) {
                            startActivity(new Intent(getContext(), NavigationViewActivity.class));
                        } else {
                            Toast.makeText(getContext(), retrivepassword.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {

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
