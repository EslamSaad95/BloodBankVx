package com.example.laptophome.bank_eldam.ui.Fragment.NavigationCycle;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.bank_eldam.data.model.contact.Contacts;
import com.example.laptophome.bank_eldam.data.model.settings.Settings;
import com.example.laptophome.bank_eldam.data.rest.ApiService;
import com.example.laptophome.bank_eldam.data.rest.RetrofitClient;
import com.example.laptophome.bank_eldam.helper.Constant;
import com.example.laptophome.bank_eldam.helper.HelperMethod;
import com.example.laptophome.bank_eldam.ui.activity.NavigationViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CallUs extends Fragment {


    @BindView(R.id.call_logo)
    ImageView callLogo;
    @BindView(R.id.call_phone)
    TextView callPhone;
    @BindView(R.id.call_email)
    TextView callEmail;


    @BindView(R.id.contact_us_youremessage_title)
    TextInputLayout contactUsMessageTitle_edittext;
    @BindView(R.id.contact_us_yourmessage_body)
    TextInputLayout contactUsMessageBody_edittext;
    @BindView(R.id.call_btn_send)
    Button callBtnSend;
    Unbinder unbinder;
    ApiService apiService;
    String facebook, instagram, whatsapp, twitter, youtube, googleplus;
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
    @BindView(R.id.call_facebook)
    ImageView callFacebook;
    @BindView(R.id.call_twitter)
    ImageView callTwitter;
    @BindView(R.id.call_youtube)
    ImageView callYoutube;
    @BindView(R.id.call_instagram)
    ImageView callInstagram;
    @BindView(R.id.call_whatsapp)
    ImageView callWhatsapp;
    @BindView(R.id.call_google)
    ImageView callGoogle;
    @BindView(R.id.call_social_root)
    RelativeLayout callSocialRoot;
    @BindView(R.id.contact_us_header)
    TextView contactUsHeader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigationcycle_call_us, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((NavigationViewActivity) getActivity()).setSupportActionBar(apptoolbar);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        HelperMethod.toolbar(toolbarNotificationLayout, true, toolbarIvArrow, false, toolbarTitle,  String.valueOf(R.string.call_us));
        apiService = RetrofitClient.getclient().create(ApiService.class);
        sociallinks();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void whatsappcontact()
    {   String whatsappContact="+20"+whatsapp;
         // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + whatsappContact;
        try {
            PackageManager pm = getContext().getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getContext(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @OnClick({R.id.call_facebook, R.id.call_twitter, R.id.call_youtube, R.id.call_instagram, R.id.call_whatsapp, R.id.call_google, R.id.call_btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.call_facebook:
                socialmedialintent(facebook, "com.facebook.katana");
                break;
            case R.id.call_twitter:
                socialmedialintent(twitter, "com.twitter.android");
                break;
            case R.id.call_youtube:
                socialmedialintent(youtube, "com.google.android.youtube");
                break;
            case R.id.call_instagram:
                socialmedialintent(instagram, "com.instagram.android");

                break;
            case R.id.call_whatsapp:
                // socialmedialintent(whatsapp,"com.whatsapp");
                whatsappcontact();

                break;
            case R.id.call_google:
                socialmedialintent(googleplus, "com.google.android.apps.plus");

                break;
            case R.id.call_btn_send:
                SendMessage();
                break;
        }
    }

    public void socialmedialintent(String url, String packagename) {
        Uri uri = Uri.parse(url);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage(packagename);

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url)));
        }
    }




    public void SendMessage() {
        String title = contactUsMessageTitle_edittext.getEditText().getText().toString().trim();
        String Body = contactUsMessageBody_edittext.getEditText().getText().toString().trim();
        String api = SharedPrerefrencesManager.GetSharedPrerefrences("token", getContext());
        apiService.addContact(api, title, Body).enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {
                try {

                    if (response.isSuccessful()) {
                        Contacts contacts = response.body();
                        if (contacts.getStatus().equals(1)) {
                            Toast.makeText(getContext(), "Message send Successfully", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getContext(), contacts.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {

            }
        });

    }

    public void sociallinks() {
        String api = SharedPrerefrencesManager.GetSharedPrerefrences(Constant.AppToken_keyname, getContext());
        apiService.getSettings(api).enqueue(new Callback<Settings>() {
            @Override
            public void onResponse(Call<Settings> call, Response<Settings> response) {
                try {

                    if (response.isSuccessful()) {
                        facebook = response.body().getData().getFacebookUrl();
                        instagram = response.body().getData().getInstagramUrl();
                        whatsapp = response.body().getData().getWhatsapp();
                        youtube = response.body().getData().getYoutubeUrl();
                        twitter = response.body().getData().getTwitterUrl();
                        googleplus = response.body().getData().getGoogleUrl();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Settings> call, Throwable t) {

            }
        });
    }
}
