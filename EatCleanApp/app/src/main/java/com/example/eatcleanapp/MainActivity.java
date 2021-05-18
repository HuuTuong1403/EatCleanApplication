package com.example.eatcleanapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatcleanapp.databinding.ActivityMainBinding;
import com.example.eatcleanapp.model.users;
import com.example.eatcleanapp.ui.home.HomeFragment;
import com.example.eatcleanapp.ui.home.signin.SignInFragment;
import com.example.eatcleanapp.ui.home.tabHome.RecipesFragment;
import com.example.eatcleanapp.ui.nguoidung.data_local.DataLocalManager;
import com.example.eatcleanapp.ui.quantrivien.AdminActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private TextView txvTitle;
    private boolean doubleBackToExitPressedOnce = false;
    private NavigationView navigationView;
    private users user;
    private EditText edt_search_home;
    private AppBarLayout appBarLayout;
    private AppBarLayout appBarHome;

    private static final int FRAGMENT_HOME  = 1;
    private static final int FRAGMENT_SIGNIN = 2;
    private static final int FRAGMENT_FAVORITES = 3;
    private static final int FRAGMENT_SETTING = 4;

    private int currentFragment = FRAGMENT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = DataLocalManager.getUser();
        if(user != null){
            if(user.getIDRole().equals("R001")){
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                onInit();
            }
        }
        else{
            onInit();
        }
    }
    private void onInit(){
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        txvTitle = (TextView)findViewById(R.id.txvTitleHome);
        Toolbar toolbar = binding.appBarMain.toolbar;
        setSupportActionBar(toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Mapping();

        appBarLayout = (AppBarLayout)findViewById(R.id.app_bar_search);
        appBarHome = (AppBarLayout)findViewById(R.id.app_home);
        ImageButton searchBox = (ImageButton)findViewById(R.id.searchBox);
        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBarHome.setVisibility(View.INVISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
                edt_search_home.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edt_search_home, InputMethodManager.SHOW_IMPLICIT);
                binding.appBarMain.toolbarSearch.setNavigationIcon(R.drawable.back24);
                binding.appBarMain.toolbarSearch.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appBarHome.setVisibility(View.VISIBLE);
                        appBarLayout.setVisibility(View.INVISIBLE);
                        imm.hideSoftInputFromWindow(binding.appBarMain.toolbarSearch.getWindowToken(), 0);
                    }
                });
            }
        });

        binding.appBarMain.btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                intent.putExtra("fragment-back", 3);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        replaceFragment(new HomeFragment(), "Trang chủ");
        currentFragment = FRAGMENT_HOME;
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    private void Mapping(){
        edt_search_home = (EditText)findViewById(R.id.edt_search_recycler);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if(appBarHome.getVisibility() == View.INVISIBLE){
            appBarHome.setVisibility(View.VISIBLE);
            appBarLayout.setVisibility(View.INVISIBLE);
        }
        else{
            if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            else{
                if(doubleBackToExitPressedOnce){
                    super.onBackPressed();
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.nav_home:{
                if(FRAGMENT_HOME != currentFragment)
                {
                    replaceFragment(new HomeFragment(), "Trang chủ");
                    hideKeyboard(this);
                    currentFragment = FRAGMENT_HOME;
                }
                break;
            }
            case R.id.nav_signin:{
                if(FRAGMENT_SIGNIN != currentFragment){
                    replaceFragment(new SignInFragment(), "Đăng nhập");
                    hideKeyboard(this);
                    currentFragment = FRAGMENT_SIGNIN;
                }
                break;
            }
            case R.id.nav_favorites:{
                break;
            }
            case R.id.nav_settings:{
                break;
            }
            default:
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment fragment, String title){
        txvTitle.setText(title);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.enter_to_right, R.anim.enter_from_right, R.anim.enter_to_right);
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}