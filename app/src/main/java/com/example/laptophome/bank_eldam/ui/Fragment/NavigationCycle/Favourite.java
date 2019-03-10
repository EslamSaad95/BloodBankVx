package com.example.laptophome.bank_eldam.ui.Fragment.NavigationCycle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.bank_eldam.Adapter.ArticlesRvAdapter;
import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.bank_eldam.data.model.notificationcount.NotificationCount;
import com.example.laptophome.bank_eldam.data.model.posts.Posts;
import com.example.laptophome.bank_eldam.data.model.posts.PostsData;
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

public class Favourite extends Fragment {


    @BindView(R.id.favourite_rv)
    RecyclerView favouriteRv;
    ApiService apiService;
    List<PostsData> favouritelist = new ArrayList<>();
    ArticlesRvAdapter adapter;
    int max = 0;

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
        View view = inflater.inflate(R.layout.fragment_navigationcycle_favourite, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        ((NavigationViewActivity) getActivity()).setSupportActionBar(apptoolbar);
        HelperMethod.toolbar(toolbarNotificationLayout, false, toolbarIvArrow, false, toolbarTitle, getString(R.string.favourite));
        HelperMethod.NotificationNumber(getContext(),toolbarTvNotificationNumber,toolbarNotificationLayout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        favouriteRv.setLayoutManager(linearLayoutManager);
        adapter = new ArticlesRvAdapter(getContext(), favouritelist);
        favouriteRv.setAdapter(adapter);
        GetFavouriteList();
        return view;
    }


    public void GetFavouriteList() {
        String token = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.AppToken_keyname, getContext());
        apiService.MyFavourites(token).enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                if (response.body().getStatus().equals(1)) {
                    favouritelist.addAll(response.body().getData().getData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    }

