package com.example.eatcleanapp.ui.home.signup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.eatcleanapp.API.APIService;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.databinding.FragmentSignUpBinding;
import com.example.eatcleanapp.databinding.SignInFragmentBinding;
import com.example.eatcleanapp.model.users;
import com.example.eatcleanapp.ui.home.signin.SignInViewModel;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;

public class SignUpFragment extends Fragment {

    private SignUpViewModel mViewModel;
    private FragmentSignUpBinding binding;
    private View view;
    private TextInputEditText edtUsername, edtEmail, edtPassword, edtPasswordAgain, edtFullName;
    private Button btnRegister;
    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }
    private String registerUserLink = "https://eatcleanrecipes.000webhostapp.com/registerUser.php";
    private String getUserLink = "https://eatcleanrecipes.000webhostapp.com/getUser.php";
    private  List<users> usersList;
    String IDUser;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        View background = view.findViewById(R.id.backgroundSignUp);
        Drawable backgroundImage = background.getBackground();
        backgroundImage.setAlpha(80);
        Mapping();

        usersList = new ArrayList<>();
        GetData();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rd = new Random();

                String Username = edtUsername.getText().toString().trim();
                String Email = edtEmail.getText().toString().trim();

                Boolean checkIDUser = true;
                while (checkIDUser == true){
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

                Boolean checkEmail = false;
                Boolean checkUsername = false;
                for (users user: usersList) {
                    if (Email.equals(user.getEmail())){
                        checkEmail = true;
                    }
                    if (Username.equals(user.getUsername())){
                        checkUsername = true;
                    }
                }
                if (checkEmail == false){
                    if (checkUsername == false){
                        if (edtPassword.getText().toString().trim().equals(edtPasswordAgain.getText().toString().trim())){
                            registerUser(registerUserLink);
                        }
                        else{
                            Toast.makeText(getActivity(), "Mật khẩu không giống nhau, vui lòng nhập lại", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getActivity(), "Username đã bị trùng, vui lòng nhập email khác", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Email đã bị trùng, vui lòng nhập email khác", Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void Mapping(){
        edtUsername = (TextInputEditText) view.findViewById(R.id.signup_edtUsername);
        edtEmail = (TextInputEditText) view.findViewById(R.id.signup_edtEmail);
        edtPassword = (TextInputEditText) view.findViewById(R.id.signup_edtPassword);
        edtPasswordAgain = (TextInputEditText) view.findViewById(R.id.signup_edtPasswordAgain);
        edtFullName = (TextInputEditText) view.findViewById(R.id.signup_edtFullname);
        btnRegister = (Button) view.findViewById(R.id.signup_btnRegister);
    }
    private void registerUser(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("success")){
                    Toast.makeText(getActivity(), "Đăng ký thành công", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity(), "Đăng ký thất bại", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Bạn đã bị lỗi" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams () throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("IDUser", IDUser);
                params.put("Email", edtEmail.getText().toString().trim());
                params.put("Password", edtPassword.getText().toString().trim());
                params.put("FullName", edtFullName.getText().toString().trim());
                params.put("Image", "");
                params.put("LoginFB", "0");
                params.put("IDRole", "R003");
                params.put("Username", edtUsername.getText().toString().trim());
                return  params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void GetData (){
        APIService.apiService.getUser().enqueue(new Callback<List<users>>() {
            @Override
            public void onResponse(Call<List<users>> call, retrofit2.Response<List<users>> response) {
                /*for (int i = 0; i < response.body().size(); i ++){
                    usersList.add(response.body().get(i));
                }*/
                usersList = response.body();
            }

            @Override
            public void onFailure(Call<List<users>> call, Throwable t) {
                Toast.makeText(view.getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}