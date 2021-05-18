package com.example.eatcleanapp.ui.home.profile;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.eatcleanapp.R;
import com.example.eatcleanapp.SubActivity;
import com.example.eatcleanapp.model.users;
import com.example.eatcleanapp.ui.nguoidung.data_local.DataLocalManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileEditFragment extends Fragment {

    private View view;
    private SubActivity mSubActivity;
    private TextInputLayout profileEdit_layout_email, profileEdit_layout_fullName;
    private TextInputEditText profileEdit_edt_userName, profileEdit_edt_email, profileEdit_edt_fullName;
    private Button profileEdt_btn_saveChange, profileEdt_btn_cancelChange;
    private Toolbar toolbar;
    private users user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSubActivity = (SubActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        Mapping();
        user = DataLocalManager.getUser();

        profileEdit_edt_userName.setText(user.getUsername());

        //Set layout
        profileEdit_layout_email.setHint(user.getEmail());
        profileEdit_layout_fullName.setHint(user.getFullName());

        //Animate and Delay
        Animation animButton = mSubActivity.getAnimButton(view);
        Handler handler = new Handler();

        profileEdt_btn_cancelChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animButton);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        profileEdit_edt_email.setText("");
                        profileEdit_edt_fullName.setText("");
                    }
                }, 400);

            }
        });

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

        profileEdit_layout_email    = (TextInputLayout)view.findViewById(R.id.profileEdit_layout_email);
        profileEdit_layout_fullName = (TextInputLayout)view.findViewById(R.id.profileEdit_layout_fullName);
        profileEdit_edt_userName    = (TextInputEditText)view.findViewById(R.id.profileEdit_edt_userName);
        profileEdit_edt_email       = (TextInputEditText)view.findViewById(R.id.profileEdit_edt_email);
        profileEdit_edt_fullName    = (TextInputEditText)view.findViewById(R.id.profileEdit_edt_fullName);
        profileEdt_btn_saveChange   = (Button)view.findViewById(R.id.profileEdit_btn_saveChange);
        profileEdt_btn_cancelChange = (Button)view.findViewById(R.id.profileEdit_btn_cancelChange);
    }
}