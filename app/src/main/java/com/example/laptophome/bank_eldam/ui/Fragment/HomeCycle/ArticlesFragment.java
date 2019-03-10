
package com.example.laptophome.bank_eldam.ui.Fragment.HomeCycle;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.laptophome.bank_eldam.Adapter.ArticlesRvAdapter;
import com.example.laptophome.bank_eldam.Adapter.DonationsRequestsRvAdapter;
import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.bank_eldam.data.model.posts.Posts;
import com.example.laptophome.bank_eldam.data.model.posts.PostsData;
import com.example.laptophome.bank_eldam.data.rest.ApiService;
import com.example.laptophome.bank_eldam.data.rest.RetrofitClient;
import com.example.laptophome.bank_eldam.helper.Constant;
import com.example.laptophome.bank_eldam.helper.HelperMethod;
import com.example.laptophome.bank_eldam.helper.OnEndless;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ArticlesFragment extends Fragment implements SearchView.OnQueryTextListener {

    @BindView(R.id.articles_sv)
    SearchView searchView;
    Unbinder unbinder;
    @BindView(R.id.articles_sp_searchview)
    Spinner articlesSpSearchview;
    @BindView(R.id.articles_rv)
    RecyclerView articlesRv;
    ApiService apiService;
    List<PostsData>postsList=new ArrayList<>();
    ArticlesRvAdapter adapter;
    ProgressDialog progressDialog;
    int max=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_articles, container, false);
        unbinder = ButterKnife.bind(this, view);
        progressDialog=HelperMethod.showProgressDialog(getContext());
        searchView.setQueryHint(getString(R.string.Please_search_here));
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        apiService = RetrofitClient.getclient().create(ApiService.class);
        searchView.setOnQueryTextListener(this);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        articlesRv.setLayoutManager(linearLayoutManager);
        OnEndless onEndless=new OnEndless(linearLayoutManager,1) {
            @Override
            public void onLoadMore(int current_page) {

                if(current_page<=max)
                {
                    GetArticles(current_page);
                }

            }
        };
        articlesRv.addOnScrollListener(onEndless);
        adapter=new ArticlesRvAdapter(getContext(),postsList);
        articlesRv.setAdapter(adapter);
        GetArticles(1);

        return view;
    }

   public  void filterArticles(int pagenumber)
   {    String keyword=searchView.getQuery().toString();
       String token= SharedPrerefrencesManager.GetSharedPrerefrences(Constant.AppToken_keyname,getContext());
       apiService.postsfilter(token,pagenumber,keyword).enqueue(new Callback<Posts>() {
           @Override
           public void onResponse(Call<Posts> call, Response<Posts> response) {
               if(response.body().getStatus().equals(1))
               {
                   postsList.addAll(response.body().getData().getData());
                   adapter.notifyDataSetChanged();
               }
               else
               {
                   Toast.makeText(getContext(),response.body().getMsg(),Toast.LENGTH_LONG).show();
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
    public void GetArticles(int page )
    {
        String token= SharedPrerefrencesManager.GetSharedPrerefrences(Constant.AppToken_keyname,getContext());
        apiService.getallposts(token,page).enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                Posts posts=response.body();
                progressDialog.dismiss();
                try {

                    if(posts.getStatus().equals(1))
                    {
                        postsList.addAll(posts.getData().getData());
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
            public void onFailure(Call<Posts> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
            if (!searchView.isIconified()) {
                searchView.setIconified(true);
            }
            return false;
        }
        @Override
        public boolean onQueryTextChange (String s){
            String userunput=s.toLowerCase();
            List<PostsData> filterlist=new ArrayList<>();
            for(PostsData keyword:postsList)
            {
                if(keyword.getTitle().toLowerCase().contains(userunput))
                {
                    filterlist.add(keyword);
                }
            }
            adapter.setfilter(filterlist);
            return true;
        }
}
