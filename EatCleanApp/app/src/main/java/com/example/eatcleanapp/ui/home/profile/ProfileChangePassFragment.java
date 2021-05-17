package com.example.eatcleanapp.ui.home.profile;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.eatcleanapp.R;
import com.example.eatcleanapp.SubActivity;

public class ProfileChangePassFragment extends Fragment {

    private View view;
    private SubActivity mSubActivity;
    private Toolbar toolbar;
    private EditText profileChangePass_edt_oldPassword, profileChangePass_edt_newPassword, profileChangePass_edt_newPasswordAgain;
    private Button profile_changePass_btn_changePass, profile_changePass_btn_cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_change_pass, container, false);
        mSubActivity = (SubActivity) getActivity();
        Mapping();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ProfileChangePassFragment.this).navigate(R.id.action_profile_changePass_fragment_to_profile_fragment);
            }
        });
        return view;
    }

    private void Mapping() {
        toolbar = (Toolbar)mSubActivity.findViewById(R.id.toolbar);

        mSubActivity.setText("Đổi mật khẩu");
        mSubActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back24);

        profileChangePass_edt_oldPassword       = (EditText)view.findViewById(R.id.profileChangePass_edt_oldPassword);
        profileChangePass_edt_newPassword       = (EditText)view.findViewById(R.id.profileChangePass_edt_newPassword);
        profileChangePass_edt_newPasswordAgain  = (EditText)view.findViewById(R.id.profileChangePass_edt_newPasswordAgain);
        profile_changePass_btn_changePass       = (Button)view.findViewById(R.id.profileChangePass_btn_changePass);
        profile_changePass_btn_cancel           = (Button)view.findViewById(R.id.pprofileChangePass_btn_cancel);
    }
}