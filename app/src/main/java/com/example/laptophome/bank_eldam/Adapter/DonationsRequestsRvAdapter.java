package com.example.laptophome.bank_eldam.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.model.donationrequests.DonationsRequestsData;
import com.example.laptophome.bank_eldam.data.model.posts.PostsData;
import com.example.laptophome.bank_eldam.helper.HelperMethod;
import com.example.laptophome.bank_eldam.ui.Fragment.HomeCycle.RequestDetailsFragment;
import com.example.laptophome.bank_eldam.ui.activity.NavigationViewActivity;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DonationsRequestsRvAdapter extends RecyclerView.Adapter<DonationsRequestsRvAdapter.Viewholder> {
    Context context;
    List<DonationsRequestsData> donationsRequestList = new ArrayList<>();



    public DonationsRequestsRvAdapter(Context context, List<DonationsRequestsData> donationsRequestList) {
        this.context = context;
        this.donationsRequestList = donationsRequestList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_donations_request_recycleview, viewGroup, false);
        return new Viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {

        setData(viewholder, i);
        setAction(viewholder, i);

    }

    private void setData(Viewholder viewholder, int i) {
        viewholder.DonationRequestBloodType_textview.setText(donationsRequestList.get(i).getBloodType().getName());
        viewholder.DonationRequestCity_textview.setText(donationsRequestList.get(i).getCity().getName());
        viewholder.DonationRequestHospital_textview.setText(donationsRequestList.get(i).getHospitalName());
        viewholder.DonationRequestName_textview.setText(donationsRequestList.get(i).getPatientName());


    }

    private void setAction(Viewholder viewholder, final int i) {
        viewholder.DonationRequestDetails_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b=new Bundle();
                String donation_id=String.valueOf(donationsRequestList.get(i).getId());
                b.putString("donation_id",donation_id);
                NavigationViewActivity activityinstance = (NavigationViewActivity) context;
                RequestDetailsFragment requestDetailsFragment=new RequestDetailsFragment();
                requestDetailsFragment.setArguments(b);
                HelperMethod.replace(requestDetailsFragment,activityinstance.getSupportFragmentManager(),R.id.navigation_activity_root);

            }
        });
        viewholder.DonationRequestCall_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", donationsRequestList.get(i).getPhone(), null));
                context.startActivity(intent);
            }
        });
    }
    public void setfilter(List<DonationsRequestsData> filterlist)
    {
        donationsRequestList=new ArrayList<>();
        donationsRequestList.addAll(filterlist);
        notifyDataSetChanged();
    }




    @Override
    public int getItemCount() {
        return donationsRequestList.size();
    }

    @OnClick({R.id.donation_request_btn_details, R.id.donation_request_btn_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.donation_request_btn_details:
                break;
            case R.id.donation_request_btn_call:
                break;
        }
    }

    public class Viewholder extends RecyclerView.ViewHolder {
            private View view;
        @BindView(R.id.donation_request_tv_blood_type)
        TextView DonationRequestBloodType_textview;
        @BindView(R.id.donation_request_btn_details)
        Button DonationRequestDetails_button;
        @BindView(R.id.donation_request_btn_call)
        Button DonationRequestCall_button;
        @BindView(R.id.donation_request_tv_name)
        TextView DonationRequestName_textview;
        @BindView(R.id.donation_request_tv_hospital)
        TextView DonationRequestHospital_textview;
        @BindView(R.id.donation_request_tv_city)
        TextView DonationRequestCity_textview;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
