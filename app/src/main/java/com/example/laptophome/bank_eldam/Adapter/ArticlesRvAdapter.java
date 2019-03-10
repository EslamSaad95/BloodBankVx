package com.example.laptophome.bank_eldam.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.bank_eldam.data.model.posts.PostsData;
import com.example.laptophome.bank_eldam.data.model.togglefavourite.ToggleFavourite;
import com.example.laptophome.bank_eldam.data.rest.ApiService;
import com.example.laptophome.bank_eldam.data.rest.RetrofitClient;
import com.example.laptophome.bank_eldam.helper.HelperMethod;
import com.example.laptophome.bank_eldam.ui.Fragment.HomeCycle.ArticlesDetailsFragment;
import com.example.laptophome.bank_eldam.ui.activity.NavigationViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticlesRvAdapter extends RecyclerView.Adapter<ArticlesRvAdapter.Viewholder>  {


    private Context context;
    private List<PostsData> postsList ;



    ApiService apiService;

    public ArticlesRvAdapter(Context context, List<PostsData> postsList) {
        this.context = context;
        this.postsList = postsList;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_articles_recycleview, viewGroup, false);
        apiService = RetrofitClient.getclient().create(ApiService.class);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {

        setData(viewholder,i);
        setAction(viewholder, i);
    }

    private void setData(Viewholder viewholder,int i)
    {
        viewholder.articlesRvTvTitle.setText(postsList.get(i).getTitle());
        Glide.with(context).load(postsList.get(i).getThumbnailFullPath()).into(viewholder.articlesRvIvTitleimg);
        if(postsList.get(i).getIsFavourite().equals(true))
        {
            viewholder.articlesRvBtnFavourite.setImageResource(R.drawable.ic_selected_fav);
        }

    }
    private void setAction(final Viewholder viewholder, final int i)
    {
        final String token=SharedPrerefrencesManager.GetSharedPrerefrences("token",context.getApplicationContext());
        viewholder.articlesRvIvTitleimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b=new Bundle();
                String s=String.valueOf(postsList.get(i).getId());
                b.putString("postid",s);
                NavigationViewActivity activityinstance = (NavigationViewActivity) context;
                ArticlesDetailsFragment articlesDetailsFragment=new ArticlesDetailsFragment();
                articlesDetailsFragment.setArguments(b);
                HelperMethod.replace(articlesDetailsFragment,activityinstance.getSupportFragmentManager(),R.id.navigation_activity_root);
            }
        });

       viewholder.articlesRvBtnFavourite.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(postsList.get(i).getIsFavourite().equals(true))
               {
                   viewholder.articlesRvBtnFavourite.setImageResource(R.drawable.ic_unselect_fav);
                   apiService.AddtoFavourite(postsList.get(i).getId(),token).enqueue(new Callback<ToggleFavourite>() {
                       @Override
                       public void onResponse(Call<ToggleFavourite> call, Response<ToggleFavourite> response) {
                           if(response.isSuccessful())
                           {

                               postsList.get(i).setIsFavourite(false);
                           }
                       }

                       @Override
                       public void onFailure(Call<ToggleFavourite> call, Throwable t) {

                       }
                   });
               }
               else if(postsList.get(i).getIsFavourite().equals(false))
               {
                   viewholder.articlesRvBtnFavourite.setImageResource(R.drawable.ic_selected_fav);
                   apiService.AddtoFavourite(postsList.get(i).getId(),token).enqueue(new Callback<ToggleFavourite>() {
                       @Override
                       public void onResponse(Call<ToggleFavourite> call, Response<ToggleFavourite> response) {
                           postsList.get(i).setIsFavourite(true);
                       }

                       @Override
                       public void onFailure(Call<ToggleFavourite> call, Throwable t) {

                       }
                   });
               }
           }
       });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public void setfilter(List<PostsData> filterlist)
    {
        postsList=new ArrayList<>();
        postsList.addAll(filterlist);
        notifyDataSetChanged();
    }



    public class Viewholder extends RecyclerView.ViewHolder {
        private View view;
        @BindView(R.id.articles_rv_tv_title)
        TextView articlesRvTvTitle;
        @BindView(R.id.articles_rv_btn_favourite)
        ImageButton articlesRvBtnFavourite;
        @BindView(R.id.articles_rv_iv_titleimg)
        ImageView articlesRvIvTitleimg;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
