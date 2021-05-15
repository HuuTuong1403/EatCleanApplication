package com.example.eatcleanapp.ui.home.detail.recipes;

import android.icu.text.TimeZoneFormat;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eatcleanapp.R;
import com.example.eatcleanapp.ui.home.detail.recipes.tabdetail.DetailViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

public class DetailRecipesFragment extends Fragment{

    private View view;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DetailViewPagerAdapter detailViewPagerAdapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_recipes, container, false);
        Mapping();

        return view;
    }
    private void Mapping(){
        mTabLayout = (TabLayout)view.findViewById(R.id.tab_detail_recipes);
        mViewPager = (ViewPager)view.findViewById(R.id.viewpager_detail_recipes);

        detailViewPagerAdapter = new DetailViewPagerAdapter(getActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(detailViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }
}