package com.example.laptophome.bank_eldam.ui.Fragment.HomeCycle;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laptophome.bank_eldam.Adapter.FragmentAdapter;
import com.example.laptophome.bank_eldam.R;
import com.example.laptophome.bank_eldam.helper.HelperMethod;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class Home_Fragment extends Fragment {

    @BindView(R.id.home_activity_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.home_activity_view_pager)
    ViewPager viewPager;
    Unbinder unbinder;
    @BindView(R.id.basic_fr_floatbtn_mkrequest)
    FloatingActionButton basicFrFloatbtnMkrequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        setViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setViewPager(ViewPager viewPager) {
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager());
        fragmentAdapter.addfragment(new ArticlesFragment(), getString(R.string.articles));
        fragmentAdapter.addfragment(new DonationRequest(), getString(R.string.donation));
        viewPager.setAdapter(fragmentAdapter);

    }

    @OnClick(R.id.basic_fr_floatbtn_mkrequest)
    public void onViewClicked() {
        AddDonationRequestFragment mkRequestFragment=new AddDonationRequestFragment();
        HelperMethod.replace(mkRequestFragment,getActivity().getSupportFragmentManager(),R.id.navigation_activity_root);

    }

}
