package com.example.eatcleanapp.ui.quantrivien.statistic;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.eatcleanapp.ui.quantrivien.statistic.tabStatistic.StatisticAdminBlogFragment;
import com.example.eatcleanapp.ui.quantrivien.statistic.tabStatistic.StatisticAdminRecipeFragment;
import com.example.eatcleanapp.ui.quantrivien.statistic.tabStatistic.StatisticAdminUserFragment;

public class ViewPagerAdapterStatistic extends FragmentStatePagerAdapter {

    public ViewPagerAdapterStatistic(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new StatisticAdminUserFragment();
            case 1:
                return new StatisticAdminRecipeFragment();
            case 2:
                return new StatisticAdminBlogFragment();
            default:
                return new StatisticAdminUserFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
