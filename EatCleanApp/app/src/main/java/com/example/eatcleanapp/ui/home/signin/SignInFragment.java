package com.example.eatcleanapp.ui.home.signin;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eatcleanapp.R;
import com.example.eatcleanapp.databinding.FragmentHomeBinding;
import com.example.eatcleanapp.databinding.SignInFragmentBinding;
import com.example.eatcleanapp.ui.home.HomeViewModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SignInFragment extends Fragment {

    private SignInViewModel mViewModel;
    private SignInFragmentBinding binding;
    private LoginButton loginButton;
    private ProfilePictureView profilePictureView;
    private CallbackManager callbackManager;
    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FacebookSdk.getApplicationContext();
        callbackManager = CallbackManager.Factory.create();
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

        mViewModel = new ViewModelProvider(this).get(SignInViewModel.class);

        binding = SignInFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        AnhXa();
        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.setFragment(this);
        setLogin_Button();

        return root;
    }

    private void setLogin_Button() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                result();
                loginButton.setVisibility(View.INVISIBLE);
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
                binding.imageprofilepictureview.setProfileId(Profile.getCurrentProfile().getId());

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name, email, first_name");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SignInViewModel.class);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void AnhXa(){
        profilePictureView = binding.imageprofilepictureview;
        loginButton = binding.loginButton;
    }

    @Override
    public void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
    }
}