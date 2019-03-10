package com.example.laptophome.bank_eldam.ui.Fragment.HomeCycle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.bank_eldam.Adapter.DonationsRequestsRvAdapter;
import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.bank_eldam.data.model.cities.Cities;
import com.example.laptophome.bank_eldam.data.model.donationrequests.DonationsRequests;
import com.example.laptophome.bank_eldam.data.model.donationrequests.DonationsRequestsData;
import com.example.laptophome.bank_eldam.data.model.governorates.Governorates;
import com.example.laptophome.bank_eldam.data.rest.ApiService;
import com.example.laptophome.bank_eldam.data.rest.RetrofitClient;
import com.example.laptophome.bank_eldam.helper.Constant;
import com.example.laptophome.bank_eldam.helper.HelperMethod;
import com.example.laptophome.bank_eldam.helper.OnEndless;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DonationRequest extends Fragment implements Constant {
    @BindView(R.id.donation_request_rv)
    RecyclerView DonationRequestRv;
    ApiService apiService;
    List<DonationsRequestsData> donationsRequestsList = new ArrayList<>();
    List<String> citieslist = new ArrayList<>();
    List<String> BloodtypeList = new ArrayList<>();
    Unbinder unbinder;
    @BindView(R.id.donation_request_tv_city)
    TextView donationRequestCity_textview;
    @BindView(R.id.city_sp)
    Spinner citySp;
    ArrayAdapter<String> CitiesSp_arrayAdapter;
    ArrayAdapter<String> BloodSp_arrayAdapter;
    @BindView(R.id.donation_request_tv_blood)
    TextView donationRequestBlood_textview;
    @BindView(R.id.blood_sp)
    Spinner bloodSp;
    DonationsRequestsRvAdapter Adapter;
    int maxdonationpage = 1;
    @BindView(R.id.donation_request_btn_search)
    ImageView donationRequestBtnSearch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donation_request, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        GetCity();
        citieslist.add(getString(R.string.choosecity));
        BloodtypeList.add(getString(R.string.bloodtype));
        HelperMethod.get_bloodtypes(true, getContext(), BloodtypeList, BloodSp_arrayAdapter, bloodSp, donationRequestBlood_textview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DonationRequestRv.setLayoutManager(linearLayoutManager);
        OnEndless onEndless = new OnEndless(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxdonationpage) {
                    GetDonationRequests(current_page);
                }

            }
        };
        DonationRequestRv.addOnScrollListener(onEndless);
        Adapter = new DonationsRequestsRvAdapter(getContext(), donationsRequestsList);
        DonationRequestRv.setAdapter(Adapter);
        GetDonationRequests(maxdonationpage);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void GetDonationRequests(int page) {
        String token = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.AppToken_keyname, getContext());
        apiService.getdonationrequests(token, page).enqueue(new Callback<DonationsRequests>() {
            @Override
            public void onResponse(Call<DonationsRequests> call, Response<DonationsRequests> response) {

                try {

                    if (response.body().getStatus().equals(1)) {
                        donationsRequestsList.addAll(response.body().getData().getData());
                        Adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<DonationsRequests> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void GetCity() {
        apiService.getGovernorates().enqueue(new Callback<Governorates>() {
            @Override
            public void onResponse(Call<Governorates> call, Response<Governorates> response) {
                try {
                    if (response.body().getStatus().equals(1)) {
                        for (int i = 0; i < response.body().getData().size(); i++) {

                            apiService.getCities(response.body().getData().get(i).getId()).enqueue(new Callback<Cities>() {

                                @Override
                                public void onResponse(Call<Cities> call, Response<Cities> response) {
                                    try {

                                        if (response.body().getStatus().equals(1)) {
                                            for (int i = 0; i < response.body().getData().size(); i++) {
                                                Cities cities = response.body();
                                                citieslist.add(cities.getData().get(i).getName());

                                            }

                                            HelperMethod.spinneradapter(true, getContext(), citySp, CitiesSp_arrayAdapter, donationRequestCity_textview, citieslist);

                                        } else {
                                            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (Exception e) {
                                    }
                                }

                                @Override
                                public void onFailure(Call<Cities> call, Throwable t) {
                                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            });
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Governorates> call, Throwable t) {

            }
        });
    }


    public void DonationFilter(int pagenumber) {
        String bloodtype = bloodSp.getSelectedItem().toString();
        donationsRequestsList.clear();
        String token = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.AppToken_keyname, getContext());
        int Cityid = (int) citySp.getSelectedItemId();
        if (bloodtype.equals(bloodSp.getItemAtPosition(0).toString())) {
            bloodtype = null;
        }
        apiService.donationfilters(token, bloodtype, Cityid, pagenumber).enqueue(new Callback<DonationsRequests>() {
            @Override
            public void onResponse(Call<DonationsRequests> call, Response<DonationsRequests> response) {
                try {

                    if (response.body().getStatus().equals(1)) {
                        donationsRequestsList.addAll(response.body().getData().getData());
                        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        //  DonationRequestRv.setLayoutManager(linearLayoutManager);
                        //DonationRequestRv.setAdapter(Adapter);
                        if (response.body().getData().getData().size() == 0) {
                            Toast.makeText(getContext(), "No result", Toast.LENGTH_LONG).show();
                        }
                        Adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<DonationsRequests> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }


    @OnClick(R.id.donation_request_btn_search)
    public void onViewClicked() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DonationRequestRv.setLayoutManager(linearLayoutManager);

        OnEndless onEndless = new OnEndless(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= maxdonationpage) {
                    DonationFilter(current_page);
                }

            }
        };
        DonationRequestRv.addOnScrollListener(onEndless);
        Adapter = new DonationsRequestsRvAdapter(getContext(), donationsRequestsList);
        DonationRequestRv.setAdapter(Adapter);
        DonationFilter(maxdonationpage);
    }
}
