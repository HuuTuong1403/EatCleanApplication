package com.example.eatcleanapp.ui.home.profile;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eatcleanapp.R;
import com.example.eatcleanapp.SubActivity;

public class ProfileEditFragment extends Fragment {

    private View view;
    private SubActivity mSubActivity;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSubActivity = (SubActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        Mapping();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ProfileEditFragment.this).navigate(R.id.action_profile_edit_fragment_to_profile_fragment);
            }
        });


        return view;
    }

    private void Mapping() {
        toolbar = (Toolbar)mSubActivity.findViewById(R.id.toolbar);
        mSubActivity.setText("Chỉnh sửa thông tin");
        mSubActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back24);
    }
}