package com.example.laptophome.bank_eldam.ui.Fragment.HomeCycle;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.bank_eldam.data.model.notificationcount.NotificationCount;
import com.example.laptophome.bank_eldam.data.model.postdetails.PostDetails;
import com.example.laptophome.bank_eldam.data.model.togglefavourite.ToggleFavourite;
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

public class ArticlesDetailsFragment extends Fragment {

    @BindView(R.id.articlesdetails_fr_articleimg)
    ImageView ArticlesDetailsArticleimg_imageview;
    @BindView(R.id.articlesdetails_tv_articletitle)
    TextView ArticlesDetailsArticletitle_textview;
    @BindView(R.id.articlesdetails_fr_btn_fav)
    ImageButton ArticlesDetailsFavourite_button;
    @BindView(R.id.articlesdetails_tv_articlesdetails)
    TextView ArticlesDetailsArticlesdetails_textview;
    ApiService apiService;
    int post_id;
    // String post_title,post_details;
    //List<PostsData>detailsList=new ArrayList<>();
    String token;
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

        View view = inflater.inflate(R.layout.fragment_articles_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        Bundle b = getArguments();
        ((NavigationViewActivity)getActivity()).setSupportActionBar(apptoolbar);
        Toolbar toolbar=(Toolbar)getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        HelperMethod.toolbar(toolbarNotificationLayout,false,toolbarIvArrow,false,toolbarTitle,null);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        progressDialog = HelperMethod.showProgressDialog(getContext());
        post_id = Integer.parseInt(b.getString("postid"));
       HelperMethod.NotificationNumber(getContext(),toolbarTvNotificationNumber,toolbarNotificationLayout);
        GetArticleDetails();



        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.articlesdetails_fr_btn_fav)
    public void onViewClicked() {
    }

    public void fav() {
        apiService.AddtoFavourite(post_id, token).enqueue(new Callback<ToggleFavourite>() {
            @Override
            public void onResponse(Call<ToggleFavourite> call, Response<ToggleFavourite> response) {

            }

            @Override
            public void onFailure(Call<ToggleFavourite> call, Throwable t) {

            }
        });
    }

    public void GetArticleDetails() {
        token = SharedPrerefrencesManager.GetSharedPrerefrences("token", getContext());
        apiService.getpostdetails(token, post_id).enqueue(new Callback<PostDetails>() {
            @Override
            public void onResponse(Call<PostDetails> call, final Response<PostDetails> response) {
                progressDialog.dismiss();
                try {
                    if (response.body().getStatus().equals(1)) {
                        if (response.body().getData().getIsFavourite().equals(true)) {
                            ArticlesDetailsFavourite_button.setImageResource(R.drawable.ic_selected_fav);
                        }
                        ArticlesDetailsArticletitle_textview.setText(response.body().getData().getTitle());
                        ArticlesDetailsArticlesdetails_textview.setText(response.body().getData().getContent());
                        toolbarTitle.setText(response.body().getData().getTitle());
                        Glide.with(getContext()).load(response.body().getData().getThumbnailFullPath()).into(ArticlesDetailsArticleimg_imageview);
                        ArticlesDetailsFavourite_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (response.body().getData().getIsFavourite().equals(true)) {
                                    ArticlesDetailsFavourite_button.setImageResource(R.drawable.ic_unselect_fav);
                                    fav();
                                    response.body().getData().setIsFavourite(false);
                                } else if (response.body().getData().getIsFavourite().equals(false)) {
                                    ArticlesDetailsFavourite_button.setImageResource(R.drawable.ic_selected_fav);
                                    fav();
                                    response.body().getData().setIsFavourite(true);
                                }
                            }
                        });

                    } else {
                        Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<PostDetails> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });


    }

}
