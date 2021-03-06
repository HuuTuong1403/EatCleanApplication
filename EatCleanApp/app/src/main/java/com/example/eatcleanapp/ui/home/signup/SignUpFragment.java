package com.example.eatcleanapp.ui.home.signup;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eatcleanapp.API.APIService;
import com.example.eatcleanapp.CustomAlert.CustomAlertActivity;
import com.example.eatcleanapp.MD5.MD5Hash;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.SubActivity;
import com.example.eatcleanapp.model.users;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;

public class SignUpFragment extends Fragment {

    private View view;
    private TextInputEditText edtUsername, edtEmail, edtPassword, edtPasswordAgain, edtFullName;
    private Button btnRegister;
    private CheckBox cb_chooseCtv;
    private final String registerUserLink = "https://msteatclean.000webhostapp.com/registerUser.php";
    private  List<users> usersList;
    private SubActivity mSubActivity;
    private String IDUser;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mSubActivity = (SubActivity) getActivity();
        View background = view.findViewById(R.id.backgroundSignUp);
        Drawable backgroundImage = background.getBackground();
        backgroundImage.setAlpha(80);
        Mapping();

        usersList = new ArrayList<>();
        GetData();

        Animation animTranslate = mSubActivity.getAnimButton(view);
        Handler handler = new Handler();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animTranslate);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(edtFullName.getText().toString().isEmpty() || edtEmail.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty() || edtPasswordAgain.getText().toString().isEmpty() || edtUsername.getText().toString().isEmpty()){
                            CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                                    .setActivity(getActivity())
                                    .setTitle("Th??ng b??o")
                                    .setMessage("C??c tr?????ng th??ng tin kh??ng ???????c tr???ng")
                                    .setType("error")
                                    .Build();
                            customAlertActivity.showDialog();
                        }
                        else{
                            Random rd = new Random();

                            String Username = edtUsername.getText().toString().trim();
                            String Email = edtEmail.getText().toString().trim();

                            boolean checkIDUser = true;
                            while (checkIDUser){
                                checkIDUser = false;
                                int x = rd.nextInt((50000-1000 + 1) + 1000);
                                IDUser = "ID-U-" + x;
                                for (users user: usersList
                                ) {
                                    if (IDUser.equals(user.getIDUser())){
                                        checkIDUser = true;
                                        break;
                                    }
                                }
                            }

                            boolean checkEmail = false;
                            boolean checkUsername = false;
                            for (users user: usersList) {
                                if (Email.equals(user.getEmail())){
                                    checkEmail = true;
                                }
                                if (Username.equals(user.getUsername())){
                                    checkUsername = true;
                                }
                            }
                            if (!checkEmail){
                                if (!checkUsername){
                                    if (edtPassword.getText().toString().trim().equals(edtPasswordAgain.getText().toString().trim())){
                                        registerUser(registerUserLink);
                                    }
                                    else{
                                        CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                                                .setActivity(getActivity())
                                                .setTitle("Th??ng b??o")
                                                .setMessage("M???t kh???u nh???p l???i kh??ng kh???p")
                                                .setType("error")
                                                .Build();
                                        customAlertActivity.showDialog();
                                    }
                                }
                                else{
                                    CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                                            .setActivity(getActivity())
                                            .setTitle("Th??ng b??o")
                                            .setMessage("Username b??? tr??ng, vui l??ng nh???p username kh??c")
                                            .setType("error")
                                            .Build();
                                    customAlertActivity.showDialog();
                                }
                            }
                            else{
                                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                                        .setActivity(getActivity())
                                        .setTitle("Th??ng b??o")
                                        .setMessage("Email b??? tr??ng, vui l??ng nh???p email kh??c")
                                        .setType("error")
                                        .Build();
                                customAlertActivity.showDialog();
                            }
                        }
                    }
                }, 400);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void Mapping(){
        edtUsername         = (TextInputEditText) view.findViewById(R.id.signup_edtUsername);
        edtEmail            = (TextInputEditText) view.findViewById(R.id.signup_edtEmail);
        edtPassword         = (TextInputEditText) view.findViewById(R.id.signup_edtPassword);
        edtPasswordAgain    = (TextInputEditText) view.findViewById(R.id.signup_edtPasswordAgain);
        edtFullName         = (TextInputEditText) view.findViewById(R.id.signup_edtFullname);
        btnRegister         = (Button) view.findViewById(R.id.signup_btnRegister);
        cb_chooseCtv        = (CheckBox) view.findViewById(R.id.cb_chooseCtv);
    }
    private void registerUser(String url){
        String password = MD5Hash.MD5(edtPassword.getText().toString().trim());
        RequestQueue requestQueue = Volley.newRequestQueue(mSubActivity.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                            .setActivity(getActivity())
                            .setTitle("Th??ng b??o")
                            .setMessage("????ng k?? t??i kho???n th??nh c??ng")
                            .setType("success")
                            .Build();
                    customAlertActivity.showDialog();
                    edtUsername.setText("");
                    edtEmail.setText("");
                    edtPassword.setText("");
                    edtPasswordAgain.setText("");
                    edtFullName.setText("");
                    cb_chooseCtv.setChecked(false);
                }
                else{
                    CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                            .setActivity(getActivity())
                            .setTitle("Th??ng b??o")
                            .setMessage("C?? l???i x???y ra trong qu?? tr??nh ????ng k?? t??i kho???n")
                            .setType("error")
                            .Build();
                    customAlertActivity.showDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(getActivity())
                        .setTitle("Th??ng b??o")
                        .setMessage("???? x???y ra l???i: " + error.toString())
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();
            }
        }){
            protected Map<String, String> getParams () throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("IDUser", IDUser);
                params.put("Email", edtEmail.getText().toString().trim());
                params.put("Password", password );
                params.put("FullName", edtFullName.getText().toString().trim());
                params.put("Image", "https://msteatclean.000webhostapp.com/uploads/noavatar.png");
                params.put("LoginFB", "0");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date now = new Date();
                String createTime = df.format(now);
                params.put("createTime", createTime);
                if(cb_chooseCtv.isChecked()){
                    params.put("IDRole", "R002");
                    params.put("Status", "waitingforapproval");
                }
                else{
                    params.put("IDRole", "R003");
                    params.put("Status", "approval");
                }
                params.put("Username", edtUsername.getText().toString().trim());
                return  params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void GetData (){
        APIService.apiService.getUser().enqueue(new Callback<List<users>>() {
            @Override
            public void onResponse(@NotNull Call<List<users>> call, @NotNull retrofit2.Response<List<users>> response) {
                usersList = response.body();
            }

            @Override
            public void onFailure(@NotNull Call<List<users>> call, @NotNull Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(getActivity())
                        .setTitle("Th??ng b??o")
                        .setMessage("???? x???y ra l???i khi l???y d??? li???u")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();
            }
        });
    }
}