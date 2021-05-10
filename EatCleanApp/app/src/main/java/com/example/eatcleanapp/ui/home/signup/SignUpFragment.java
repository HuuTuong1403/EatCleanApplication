package com.example.eatcleanapp.ui.home.signup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eatcleanapp.R;
import com.example.eatcleanapp.databinding.FragmentSignUpBinding;
import com.example.eatcleanapp.databinding.SignInFragmentBinding;
import com.example.eatcleanapp.ui.home.signin.SignInViewModel;

public class SignUpFragment extends Fragment {

    private SignUpViewModel mViewModel;
    private FragmentSignUpBinding binding;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(SignUpViewModel.class);

        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSignup;
        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        // TODO: Use the ViewModel
    }

}