package com.example.eatcleanapp.ui.home.signup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import java.util.Map;
import java.util.Random;

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
    private ArrayList<users> usersList = new ArrayList<>();
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        View background = view.findViewById(R.id.backgroundSignUp);
        Drawable backgroundImage = background.getBackground();
        backgroundImage.setAlpha(80);
        Mapping();

         GetData(getUserLink);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPassword.getText().toString().trim().equals(edtPasswordAgain.getText().toString().trim())){
                    registerUser(registerUserLink);
                }
                else{
                    Toast.makeText(getActivity(), "Mật khẩu không giống nhau, vui lòng nhập lại", Toast.LENGTH_LONG).show();
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
                Random rd = new Random();
                int x = rd.nextInt((50000-1000 + 1) + 1000);
                String IDUser = "ID-U-" + x;
                params.put("IDUser", IDUser);
                params.put("Email", edtEmail.getText().toString().trim());
                params.put("Password", edtPassword.getText().toString().trim());
                params.put("FullName", edtFullName.getText().toString().trim());
                params.put("Image", "");
                params.put("LoginFB", "0");
                params.put("IDRole", "R003");
                return  params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void GetData (String url){
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i ++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        usersList.add(new users(
                                object.getString("IDUser"),
                                object.getString("Email"),
                                object.getString("Password"),
                                object.getString("FullName"),
                                object.getString("Image"),
                                object.getString("LoginFB"),
                                object.getString("IDRole")
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

}