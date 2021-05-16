package com.example.eatcleanapp.ui.home.profile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eatcleanapp.R;
import com.example.eatcleanapp.SubActivity;

public class ProfileFragment extends Fragment {

    private View view;
    private SubActivity mSubActivity;


    public static ProfileFragment newInstance() { return new ProfileFragment();}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSubActivity = (SubActivity) getActivity();
        mSubActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back24);
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        Mapping();

        return view;
    }

    private void Mapping() {

    }


}