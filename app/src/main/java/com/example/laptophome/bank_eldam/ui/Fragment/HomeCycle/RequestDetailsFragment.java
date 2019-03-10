package com.example.laptophome.bank_eldam.ui.Fragment.HomeCycle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.bank_eldam.data.model.donationdetails.DonationDetails;
import com.example.laptophome.bank_eldam.data.rest.ApiService;
import com.example.laptophome.bank_eldam.data.rest.RetrofitClient;
import com.example.laptophome.bank_eldam.helper.Constant;
import com.example.laptophome.bank_eldam.helper.HelperMethod;
import com.example.laptophome.bank_eldam.ui.activity.NavigationViewActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RequestDetailsFragment extends Fragment implements OnMapReadyCallback {
    int donation_id;
    ApiService apiService;
    @BindView(R.id.requestdetails_tv_name)
    TextView RequestDetailsName_textview;
    @BindView(R.id.requestdetails_tv_age)
    TextView RequestDetailsAge_textview;
    @BindView(R.id.requestdetails_tv_bloodtype)
    TextView RequestDetailsBloodtype_textview;
    @BindView(R.id.requestdetails_tv_casesnumber)
    TextView RequestDetailsCasesnumber_textview;
    @BindView(R.id.requestdetails_tv_hospitalname)
    TextView RequestDetailsHospitalname_textview;
    @BindView(R.id.requestdetails_tv_hospitaladdress)
    TextView RequestDetailsHospitaladdress_textview;
    @BindView(R.id.requestdetails_tv_phonenumber)
    TextView RequestDetailsPhonenumber_textview;
    @BindView(R.id.requestdetails_tv_detailscontent)
    TextView RequestDetailsDetailscontent_textview;
    @BindView(R.id.requestdetails_btn_call)
    Button RequestDetailsCall_button;
    GoogleMap mMap;
    Location location;
    double case_locationlongtutde;
    double case_locationlatitude;
    String case_phonenumber;
    ProgressDialog progressDialog;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donationrequest_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((NavigationViewActivity) getActivity()).setSupportActionBar(apptoolbar);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        HelperMethod.toolbar(toolbarNotificationLayout, true, toolbarIvArrow, false, toolbarTitle, null);
        Bundle b = getArguments();
        donation_id = Integer.parseInt(b.getString("donation_id"));
        apiService = RetrofitClient.getclient().create(ApiService.class);
        progressDialog = HelperMethod.showProgressDialog(getContext());
        GetDonationRequestDeatils();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        statusCheck();


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.requestdetails_btn_call)
    public void onViewClicked() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", case_phonenumber, null));
        startActivity(intent);
    }

    public void GetDonationRequestDeatils() {
        String token = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.AppToken_keyname, getContext());
        apiService.getdonationrequestdetails(token, donation_id).enqueue(new Callback<DonationDetails>() {
            @Override
            public void onResponse(Call<DonationDetails> call, Response<DonationDetails> response) {
                progressDialog.dismiss();
                DonationDetails donationDetails = response.body();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals(1)) {
                            RequestDetailsName_textview.setText(donationDetails.getData().getPatientName());
                            toolbarTitle.setText(getString(R.string.donationrequest)+"  "+donationDetails.getData().getPatientName());
                            RequestDetailsBloodtype_textview.setText(donationDetails.getData().getBloodType().getName());
                            RequestDetailsHospitalname_textview.setText(donationDetails.getData().getHospitalName());
                            RequestDetailsHospitaladdress_textview.setText(donationDetails.getData().getHospitalAddress());
                            RequestDetailsPhonenumber_textview.setText(donationDetails.getData().getPhone());
                            RequestDetailsCasesnumber_textview.setText(donationDetails.getData().getBagsNum());
                            RequestDetailsDetailscontent_textview.setText(donationDetails.getData().getNotes());
                            RequestDetailsAge_textview.setText(donationDetails.getData().getPatientAge());
                            case_locationlongtutde = Double.valueOf(donationDetails.getData().getLongitude());
                            case_locationlatitude = Double.valueOf(donationDetails.getData().getLatitude());
                            case_phonenumber = donationDetails.getData().getPhone();
                            onMapReady(mMap);

                        } else {
                            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<DonationDetails> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng case_location = new LatLng(case_locationlongtutde, case_locationlatitude);
        mMap.addMarker(new MarkerOptions().position(case_location).title(getString(R.string.location))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapmarker)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(case_location, 15));


    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();


    }


}
