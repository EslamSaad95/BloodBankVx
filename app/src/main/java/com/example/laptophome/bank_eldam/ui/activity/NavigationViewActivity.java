package com.example.laptophome.bank_eldam.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.bank_eldam.data.model.notificationcount.NotificationCount;
import com.example.laptophome.bank_eldam.data.model.registernotification.RegisterNotification;
import com.example.laptophome.bank_eldam.data.rest.ApiService;
import com.example.laptophome.bank_eldam.data.rest.RetrofitClient;
import com.example.laptophome.bank_eldam.helper.Constant;
import com.example.laptophome.bank_eldam.helper.HelperMethod;
import com.example.laptophome.bank_eldam.ui.Fragment.HomeCycle.Home_Fragment;
import com.example.laptophome.bank_eldam.ui.Fragment.HomeCycle.NotificationFragment;
import com.example.laptophome.bank_eldam.ui.Fragment.NavigationCycle.AboutApp;
import com.example.laptophome.bank_eldam.ui.Fragment.NavigationCycle.CallUs;
import com.example.laptophome.bank_eldam.ui.Fragment.NavigationCycle.Favourite;
import com.example.laptophome.bank_eldam.ui.Fragment.NavigationCycle.MyInformation;
import com.example.laptophome.bank_eldam.ui.Fragment.NavigationCycle.NotificationSettings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    ActionBarDrawerToggle toggle;

    @BindView(R.id.toolbar_tv_notification_number)
    TextView NotificationNumber_textview;
    ApiService apiService;
    @BindView(R.id.toolbar_notification_layout)
    FrameLayout toolbarNotificationLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_view);
        ButterKnife.bind(this);
        // toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        apiService = RetrofitClient.getclient().create(ApiService.class);
        //  navigationView = (NavigationView) findViewById(R.id.nav_view);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        Home_Fragment basicfragment = new Home_Fragment();
        NotificationNumber();
       // HelperMethod.NotificationNumber(getApplicationContext(),NotificationNumber_textview,toolbarNotificationLayout);
        HelperMethod.replace(basicfragment, getSupportFragmentManager(), R.id.navigation_activity_root);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, NavigationViewActivity.class));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_ic_myinfo:
                MyInformation myInformation = new MyInformation();
                HelperMethod.replace(myInformation, getSupportFragmentManager(), R.id.navigation_activity_root);
                drawerLayout.closeDrawer(Gravity.START, false);
                break;


            case R.id.nav_ic_notification:
                NotificationSettings notificationSettings = new NotificationSettings();
                HelperMethod.replace(notificationSettings, getSupportFragmentManager(), R.id.navigation_activity_root);
                drawerLayout.closeDrawer(Gravity.START, false);
                break;

            case R.id.nav_ic_favourite:
                Favourite favourite = new Favourite();
                HelperMethod.replace(favourite, getSupportFragmentManager(), R.id.navigation_activity_root);
                drawerLayout.closeDrawer(Gravity.START, false);
                break;


            case R.id.nav_ic_home:
                Intent homeintenet = new Intent(getApplicationContext(), NavigationViewActivity.class);
                startActivity(homeintenet);
                drawerLayout.closeDrawer(Gravity.START, false);
                break;

            case R.id.nav_ic_rules:
                //write here
                break;

            case R.id.nav_ic_contact_us:
                CallUs callus = new CallUs();
                HelperMethod.replace(callus, getSupportFragmentManager(), R.id.navigation_activity_root);
                drawerLayout.closeDrawer(Gravity.START, false);
                break;

            case R.id.nav_ic_aboutapp:
                AboutApp aboutApp = new AboutApp();
                HelperMethod.replace(aboutApp, getSupportFragmentManager(), R.id.navigation_activity_root);
                drawerLayout.closeDrawer(Gravity.START, false);
                break;

            case R.id.nav_ic_rate_app:
                //write here
                break;
            case R.id.nav_ic_signout:
                Intent signoutintent = new Intent(getApplicationContext(), UserActivty.class);
                startActivity(signoutintent);
                RemoveNotification();
                break;
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @OnClick(R.id.toolbar_iv_notification)
    public void onViewClicked() {
        NotificationFragment notificationFragment = new NotificationFragment();
        HelperMethod.replace(notificationFragment, getSupportFragmentManager(), R.id.navigation_activity_root);
    }

    public void NotificationNumber() {
        String token = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.AppToken_keyname, this);
        apiService.getNotificationNumber(token).enqueue(new Callback<NotificationCount>() {
            @Override
            public void onResponse(Call<NotificationCount> call, Response<NotificationCount> response) {
                final NotificationCount NotificationNumber = response.body();
                try {
                    if (NotificationNumber.getStatus().equals(1)) {
                        if (NotificationNumber.getData().getNotificationsCount() > 0) {

                            NotificationNumber_textview.setVisibility(View.VISIBLE);
                            NotificationNumber_textview.setText(NotificationNumber.getData().getNotificationsCount().toString());
                            toolbarNotificationLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    NotificationNumber.getData().setNotificationsCount(0);
                                }
                            });
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), NotificationNumber.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                }


            }

            @Override
            public void onFailure(Call<NotificationCount> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void hidekeyboard(View view) {
        HelperMethod.hidesoftkeyboard(view, this);
    }

    public void RemoveNotification() {
        String app_token = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.AppToken_keyname, getApplicationContext());
        String notification_token = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.NotificationToken_keyname, getApplicationContext());
        apiService.RemoveNotification(notification_token, app_token).enqueue(new Callback<RegisterNotification>() {
            @Override
            public void onResponse(Call<RegisterNotification> call, Response<RegisterNotification> response) {
                try {
                    if (response.body().getStatus().equals(0)) {
                        Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<RegisterNotification> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
