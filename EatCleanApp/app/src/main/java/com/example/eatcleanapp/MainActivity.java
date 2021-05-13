package com.example.eatcleanapp;

import android.annotation.SuppressLint;
import android.app.StatusBarManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatcleanapp.ui.home.HomeFragment;
import com.example.eatcleanapp.ui.home.signin.SignInFragment;
import com.example.eatcleanapp.ui.quantrivien.AdminActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavHostController;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eatcleanapp.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private TextView txvTitle;
    private boolean doubleBackToExitPressedOnce = false;

    private static final int FRAGMENT_HOME = 1;
    private static final int FRAGMENT_SIGNIN = 2;
    private static final int FRAGMENT_FAVORITES = 3;
    private static final int FRAGMENT_SETTING = 4;

    private int currentFragment = FRAGMENT_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        txvTitle = (TextView)findViewById(R.id.txvTitleHome);
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_favorites, R.id.nav_settings, R.id.nav_signin)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

        AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.app_bar_search);
        AppBarLayout appBarHome = (AppBarLayout)findViewById(R.id.app_home);
        ImageButton searchBox = (ImageButton)findViewById(R.id.searchbox);
        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBarHome.setVisibility(View.INVISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
                binding.appBarMain.toolbarSearch.setNavigationIcon(R.drawable.back24);
                binding.appBarMain.toolbarSearch.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appBarHome.setVisibility(View.VISIBLE);
                        appBarLayout.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        binding.appBarMain.btnprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                intent.putExtra("fragment-back", 3);
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        replaceFragment(new HomeFragment(), "Trang chủ");
        currentFragment = FRAGMENT_HOME;


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            if(doubleBackToExitPressedOnce){
                super.onBackPressed();
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.nav_home:{
                if(FRAGMENT_HOME != currentFragment)
                {
                    replaceFragment(new HomeFragment(), "Trang chủ");
                    currentFragment = FRAGMENT_HOME;
                }
                break;
            }
            case R.id.nav_signin:{
                if(FRAGMENT_SIGNIN != currentFragment){
                    replaceFragment(new SignInFragment(), "Đăng nhập");
                    currentFragment = FRAGMENT_SIGNIN;
                }
                break;
            }
            case R.id.nav_favorites:{
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();
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
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment);
        fragmentTransaction.commit();
    }
}