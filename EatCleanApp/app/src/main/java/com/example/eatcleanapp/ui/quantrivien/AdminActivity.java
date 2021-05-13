package com.example.eatcleanapp.ui.quantrivien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.eatcleanapp.MainActivity;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.databinding.ActivityAdminBinding;
import com.example.eatcleanapp.databinding.ActivityMainBinding;
import com.example.eatcleanapp.ui.home.HomeFragment;
import com.example.eatcleanapp.ui.home.signin.SignInFragment;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityAdminBinding binding;
    private TextView txvTitleAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        txvTitleAdmin = (TextView)findViewById(R.id.txvTitleAdmin);
        txvTitleAdmin.setText("Trang của Admin");
        setSupportActionBar(binding.appBarAdmin.toolbarAdmin);
        DrawerLayout drawerLayout = binding.drawerAdmin;
        NavigationView navigationView = binding.navAdmin;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, binding.appBarAdmin.toolbarAdmin, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.menu_home_nav_admin)
                .setDrawerLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu24);

        navigationView.getMenu().findItem(R.id.menu_home_nav_admin).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = binding.drawerAdmin;
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){

        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_admin);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment fragment, String title){
        txvTitleAdmin.setText(title);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment);
        fragmentTransaction.commit();
    }
}