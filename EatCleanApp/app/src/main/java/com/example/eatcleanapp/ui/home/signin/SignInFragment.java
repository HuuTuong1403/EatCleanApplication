package com.example.eatcleanapp.ui.home.signin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.eatcleanapp.API.APIService;
import com.example.eatcleanapp.CustomAlert.CustomAlertActivity;
import com.example.eatcleanapp.MD5.MD5Hash;
import com.example.eatcleanapp.MainActivity;
import com.example.eatcleanapp.R;

import com.example.eatcleanapp.SubActivity;
import com.example.eatcleanapp.model.users;
import com.example.eatcleanapp.ui.home.HomeFragment;

import com.example.eatcleanapp.ui.nguoidung.data_local.DataLocalManager;
import com.example.eatcleanapp.ui.quantrivien.AdminActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SignInFragment extends Fragment {

    private View view;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextView txvSignUp, txvForgotPass;
    private TextInputEditText edtEmail, edtPassword;
    private MaterialButton btnSignIn;
    private List<users> userList = new ArrayList<>();
    private ImageButton searchBox;
    private MainActivity mMainActivity;
    
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FacebookSdk.getApplicationContext();
        mMainActivity = (MainActivity) getActivity();
        callbackManager = CallbackManager.Factory.create();
        view = inflater.inflate(R.layout.sign_in_fragment, container, false);
        loginButton();
        Mapping();
        GetUsers();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.setFragment(this);
        setLogin_Button();

        txvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SubActivity.class);
                intent.putExtra("fragment-back", 1);
                startActivity(intent);
                mMainActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        txvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), SubActivity.class);
                intent.putExtra("fragment-back", 2);
                startActivity(intent);
                mMainActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.anim_scale);
        Handler handler = new Handler();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUsers();
                v.startAnimation(anim);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String email = edtEmail.getText().toString().trim();
                        String password = MD5Hash.MD5(edtPassword.getText().toString().trim());
                        boolean checkLogin = false;
                        users userLogin = null;
                        for (users user: userList
                        ) {
                            if ((email.equals(user.getEmail()) || (email.equals(user.getUsername()))) && password.equals(user.getPassword())){
                                checkLogin = true;
                                userLogin = user;
                                break;
                            }
                        }
                        if (checkLogin){
                            switch(userLogin.getIDRole()){
                                case "R001":{
                                    DataLocalManager.setUser(userLogin);
                                    Intent intent = new Intent(view.getContext(), AdminActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("object_user", userLogin);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    mMainActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                    getActivity().finish();
                                    break;
                                }
                                case "R002":{
                                    DataLocalManager.setUser(userLogin);
                                    Bundle bundle = new Bundle();
                                    HomeFragment homeFragment = new HomeFragment();
                                    searchBox.setVisibility(View.VISIBLE);
                                    bundle.putSerializable("object_user", userLogin);
                                    homeFragment.setArguments(bundle);
                                    mMainActivity.replaceFragment(homeFragment, "Trang ch???");
                                    mMainActivity.setCurrentFragment(1);
                                    NavigationView naview = mMainActivity.findViewById(R.id.nav_view);
                                    naview.getMenu().findItem(R.id.nav_home).setChecked(true);
                                    naview.getMenu().findItem(R.id.nav_ctv).setVisible(true);
                                    break;
                                }
                                case "R003":{
                                    DataLocalManager.setUser(userLogin);
                                    HomeFragment homeFragment = new HomeFragment();
                                    Bundle bundle = new Bundle();
                                    searchBox.setVisibility(View.VISIBLE);
                                    bundle.putSerializable("object_user", userLogin);
                                    homeFragment.setArguments(bundle);
                                    mMainActivity.replaceFragment(homeFragment, "Trang ch???");
                                    mMainActivity.setCurrentFragment(1);
                                    NavigationView naview = mMainActivity.findViewById(R.id.nav_view);
                                    naview.getMenu().findItem(R.id.nav_home).setChecked(true);
                                    break;
                                }
                            }
                        }
                        else{
                            CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                                    .setActivity(getActivity())
                                    .setTitle("Th??ng b??o")
                                    .setMessage("Th??ng tin ????ng nh???p kh??ng ????ng")
                                    .setType("error")
                                    .Build();
                            customAlertActivity.showDialog();
                        }

                    }
                }, 600);
            }
        });
        return view;
    }

    private void loginButton(){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.INVISIBLE);
                result();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }



    private void setLogin_Button() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                result();
                loginButton.setVisibility(View.INVISIBLE);
                HomeFragment homeFragment = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("isloggin", 1);
                homeFragment.setArguments(bundle);

                mMainActivity.replaceFragment(homeFragment, "Trang ch???");
                NavigationView naview = mMainActivity.findViewById(R.id.nav_view);
                naview.getMenu().findItem(R.id.nav_home).setChecked(true);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    public void result(){
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("JSON", response.getJSONObject().toString());
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name, email, first_name");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void Mapping(){
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        txvSignUp = (TextView)view.findViewById(R.id.signup);
        txvForgotPass = (TextView)view.findViewById(R.id.txv_forgotpass);
        edtEmail = (TextInputEditText) view.findViewById(R.id.signin_edtEmail);
        edtPassword = (TextInputEditText) view.findViewById(R.id.signin_edtPassword);
        btnSignIn = (MaterialButton) view.findViewById(R.id.signin_btnSignIn);
        searchBox = (ImageButton)mMainActivity.findViewById(R.id.searchBox);
    }

    @Override
    public void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
    }

    public void GetUsers(){
        APIService.apiService.getUser().enqueue(new Callback<List<users>>() {
            @Override
            public void onResponse(@NotNull Call<List<users>> call, @NotNull retrofit2.Response<List<users>> response) {
                userList = response.body();
            }

            @Override
            public void onFailure(@NotNull Call<List<users>> call, @NotNull Throwable t) {
                Toast.makeText(view.getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}