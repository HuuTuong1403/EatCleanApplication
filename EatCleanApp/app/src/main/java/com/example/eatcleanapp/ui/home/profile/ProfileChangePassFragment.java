package com.example.eatcleanapp.ui.home.profile;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eatcleanapp.API.APIService;
import com.example.eatcleanapp.CustomAlert.CustomAlertActivity;
import com.example.eatcleanapp.MD5.MD5Hash;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.SubActivity;
import com.example.eatcleanapp.model.users;
import com.example.eatcleanapp.ui.nguoidung.data_local.DataLocalManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileChangePassFragment extends Fragment {

    private View view;
    private SubActivity mSubActivity;
    private Toolbar toolbar;
    private EditText profileChangePass_edt_oldPassword, profileChangePass_edt_newPassword, profileChangePass_edt_newPasswordAgain;
    private Button profile_changePass_btn_changePass, profile_changePass_btn_cancel;
    private users user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_change_pass, container, false);
        mSubActivity = (SubActivity) getActivity();
        Mapping();

        Animation animButton = mSubActivity.getAnimButton(view);
        Handler handler = new Handler();
        user = DataLocalManager.getUser();
        profile_changePass_btn_changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animButton);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(profileChangePass_edt_oldPassword.getText().toString().isEmpty() || profileChangePass_edt_newPassword.getText().toString().isEmpty() || profileChangePass_edt_newPasswordAgain.getText().toString().isEmpty()){
                            CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                                    .setActivity(mSubActivity)
                                    .setTitle("Th??ng b??o")
                                    .setMessage("C??c tr?????ng th??ng tin kh??ng ???????c tr???ng")
                                    .setType("error")
                                    .Build();
                            customAlertActivity.showDialog();
                            return;
                        }
                        else{
                            String oldPass = MD5Hash.MD5(profileChangePass_edt_oldPassword.getText().toString());
                            String newPass = profileChangePass_edt_newPassword.getText().toString();
                            String newPassAgain = profileChangePass_edt_newPasswordAgain.getText().toString();
                            if(oldPass.equals(user.getPassword())){
                                if(newPass.equals(oldPass)){
                                    CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                                            .setActivity(mSubActivity)
                                            .setTitle("Th??ng b??o")
                                            .setMessage("M???t kh???u m???i ph???i kh??c m???t kh???u c??")
                                            .setType("error")
                                            .Build();
                                    customAlertActivity.showDialog();
                                }
                                else{
                                    if(newPassAgain.equals(newPass)){
                                        changePass(user.getIDUser(), newPass);
                                        profileChangePass_edt_oldPassword.setText("");
                                        profileChangePass_edt_newPassword.setText("");
                                        profileChangePass_edt_newPasswordAgain.setText("");
                                    }
                                    else{
                                        CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                                                .setActivity(mSubActivity)
                                                .setTitle("Th??ng b??o")
                                                .setMessage("M???t kh???u nh???p l???i kh??ng kh???p")
                                                .setType("error")
                                                .Build();
                                        customAlertActivity.showDialog();
                                    }
                                }
                            }
                            else{
                                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                                        .setActivity(mSubActivity)
                                        .setTitle("Th??ng b??o")
                                        .setMessage("M???t kh???u c?? kh??ng ch??nh x??c")
                                        .setType("error")
                                        .Build();
                                customAlertActivity.showDialog();
                            }
                        }
                    }
                }, 400);
            }
        });

        profile_changePass_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animButton);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        profileChangePass_edt_oldPassword.setText("");
                        profileChangePass_edt_newPassword.setText("");
                        profileChangePass_edt_newPasswordAgain.setText("");
                    }
                }, 400);
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ProfileChangePassFragment.this).navigate(R.id.action_profile_changePass_fragment_to_profile_fragment);
            }
        });

        return view;
    }

    private void getUserByUsername(String Username){
        APIService.apiService.getUserByUsername(Username).enqueue(new Callback<users>() {
            @Override
            public void onResponse(Call<users> call, Response<users> response) {
                DataLocalManager.setUser(response.body());
                user = DataLocalManager.getUser();
            }

            @Override
            public void onFailure(Call<users> call, Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(mSubActivity)
                        .setTitle("Th??ng b??o")
                        .setMessage("???? x???y ra l???i")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();
            }
        });
    }

    private void changePass(String IDUser, String Password){
        String passwordHash = MD5Hash.MD5(Password);
        APIService.apiService.changePass(IDUser, passwordHash).enqueue(new Callback<users>() {
            @Override
            public void onResponse(Call<users> call, Response<users> response) {
                getUserByUsername(user.getUsername());
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(mSubActivity)
                        .setTitle("Th??ng b??o")
                        .setMessage("?????i m???t kh???u th??nh c??ng")
                        .setType("success")
                        .Build();
                customAlertActivity.showDialog();
            }

            @Override
            public void onFailure(Call<users> call, Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(mSubActivity)
                        .setTitle("Th??ng b??o")
                        .setMessage("???? x???y ra l???i khi ?????i m???t kh???u")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();            }
        });
    }

    private void Mapping() {
        toolbar = (Toolbar)mSubActivity.findViewById(R.id.toolbar);

        mSubActivity.setText("?????i m???t kh???u");
        mSubActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back24);

        profileChangePass_edt_oldPassword       = (EditText)view.findViewById(R.id.profileChangePass_edt_oldPassword);
        profileChangePass_edt_newPassword       = (EditText)view.findViewById(R.id.profileChangePass_edt_newPassword);
        profileChangePass_edt_newPasswordAgain  = (EditText)view.findViewById(R.id.profileChangePass_edt_newPasswordAgain);
        profile_changePass_btn_changePass       = (Button)view.findViewById(R.id.profileChangePass_btn_changePass);
        profile_changePass_btn_cancel           = (Button)view.findViewById(R.id.pprofileChangePass_btn_cancel);
    }
}