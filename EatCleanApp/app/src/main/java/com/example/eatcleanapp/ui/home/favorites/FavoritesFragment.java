package com.example.eatcleanapp.ui.home.favorites;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eatcleanapp.MainActivity;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.model.users;
import com.example.eatcleanapp.ui.home.signin.SignInFragment;
import com.example.eatcleanapp.ui.nguoidung.data_local.DataLocalManager;
import com.google.android.material.navigation.NavigationView;

public class FavoritesFragment extends Fragment {

    private View view;
    private TextView favorites_txv_no_user, favorite_txv_signIn_signUp;
    private MainActivity mMainActivity;
    private users userIsLogin;
    private NavigationView navigationView;
    private Menu menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        userIsLogin = DataLocalManager.getUser();
        if(userIsLogin != null){
            view = inflater.inflate(R.layout.fragment_favorites, container, false);
            Mapping();
        }
        else{
            view = inflater.inflate(R.layout.layout_favorites_no_user, container, false);
            Mapping();
            favorite_txv_signIn_signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMainActivity.replaceFragment(new SignInFragment(), "Đăng nhập");
                    menu.findItem(R.id.nav_signin).setChecked(true);
                    mMainActivity.setCurrentFragment(2);
                }
            });
        }

        return view;
    }

    private void Mapping(){
        favorites_txv_no_user           = (TextView)view.findViewById(R.id.favorite_txv_no_user);
        navigationView                  = (NavigationView)mMainActivity.findViewById(R.id.nav_view);
        favorite_txv_signIn_signUp = (TextView)view.findViewById(R.id.favorite_txv_signIn_signUp);
        menu                            = navigationView.getMenu();
    }
}