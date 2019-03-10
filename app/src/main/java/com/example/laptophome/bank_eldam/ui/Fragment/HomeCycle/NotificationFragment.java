package com.example.laptophome.bank_eldam.ui.Fragment.HomeCycle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.bank_eldam.Adapter.ArticlesRvAdapter;
import com.example.laptophome.bank_eldam.Adapter.NotificationRvAdapter;
import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.bank_eldam.data.model.notificationlist.NotificationData;
import com.example.laptophome.bank_eldam.data.model.notificationlist.NotificationList;
import com.example.laptophome.bank_eldam.data.rest.ApiService;
import com.example.laptophome.bank_eldam.data.rest.RetrofitClient;
import com.example.laptophome.bank_eldam.helper.Constant;
import com.example.laptophome.bank_eldam.helper.HelperMethod;
import com.example.laptophome.bank_eldam.helper.OnEndless;
import com.example.laptophome.bank_eldam.ui.activity.NavigationViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationFragment extends Fragment {


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
    @BindView(R.id.notfication_rv)
    RecyclerView notficationRv;
    int max=0;

    List<NotificationData> notificationList=new ArrayList<>();
    NotificationRvAdapter adapter;
    ApiService apiService;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((NavigationViewActivity)getActivity()).setSupportActionBar(apptoolbar);
        Toolbar toolbar=(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        HelperMethod.toolbar(toolbarNotificationLayout,true,toolbarIvArrow,false,toolbarTitle,getString(R.string.notification));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        notficationRv.setLayoutManager(linearLayoutManager);
        getnotification();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void getnotification()
    {
        String token= SharedPrerefrencesManager.GetSharedPrerefrences(Constant.AppToken_keyname,getContext());
        apiService.getNotificationList(token).enqueue(new Callback<NotificationList>() {
            @Override
            public void onResponse(Call<NotificationList> call, Response<NotificationList> response) {
                try {

                    if(response.body().getStatus().equals(1))
                    {
                                notificationList.addAll(response.body().getData().getData());
                              adapter=new NotificationRvAdapter(getContext(),notificationList);
                              notficationRv.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                    }

                    else
                    {
                        Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {}
            }

            @Override
            public void onFailure(Call<NotificationList> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

    }
}
