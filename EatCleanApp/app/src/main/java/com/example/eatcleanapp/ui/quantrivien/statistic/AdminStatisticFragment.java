package com.example.eatcleanapp.ui.quantrivien.statistic;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.eatcleanapp.R;
import com.example.eatcleanapp.ui.home.LoadingDialog;
import com.example.eatcleanapp.ui.quantrivien.AdminActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class AdminStatisticFragment extends Fragment {

    private View view;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private AdminActivity adminActivity;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adminActivity = (AdminActivity) getActivity();
        loadingDialog = new LoadingDialog(adminActivity);
        view = inflater.inflate(R.layout.fragment_admin_statistic, container, false);

        Mapping();

        return view;
    }

    private void Mapping() {
        viewPager = view.findViewById(R.id.view_pager_statistic_admin);
        bottomNavigationView = view.findViewById(R.id.bottom_menu_statistic_admin);
        ViewPagerAdapterStatistic viewPagerAdapterStatistic = new ViewPagerAdapterStatistic(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapterStatistic);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menu_admin_statistic_userForeachMonth:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_admin_statistic_commentForeachMonth:
                        viewPager.setCurrentItem(1);
                        break;
                }
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_admin_statistic_userForeachMonth).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_admin_statistic_commentForeachMonth).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}