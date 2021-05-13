package com.example.eatcleanapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.eatcleanapp.R;
import com.example.eatcleanapp.databinding.FragmentHomeBinding;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {

    //private HomeViewModel homeViewModel;
    //private FragmentHomeBinding binding;

    private View view;

    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        //binding = FragmentHomeBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();
        /*final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        view = inflater.inflate(R.layout.fragment_home, container, false);
        Bundle bundle = getArguments();
        if(bundle != null){
            int id = bundle.getInt("isloggin");
            if(id == 1){
                NavigationView navigationView = (NavigationView)getActivity().findViewById(R.id.nav_view);
                Menu menu = navigationView.getMenu();
                menu.findItem(R.id.nav_signin).setVisible(false);
                ImageButton btnProfile = (ImageButton)getActivity().findViewById(R.id.btnprofile);
                btnProfile.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }
}