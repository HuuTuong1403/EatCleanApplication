package com.example.eatcleanapp.ui.quantrivien.management;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.eatcleanapp.API.APIService;
import com.example.eatcleanapp.CustomAlert.CustomAlertActivity;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.model.users;
import com.example.eatcleanapp.ui.home.LoadingDialog;
import com.example.eatcleanapp.ui.quantrivien.AdminActivity;
import com.example.eatcleanapp.ui.quantrivien.management.adapter.UserManagementSwipeViewAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserManagementFragment extends Fragment {

    private View view;
    private AdminActivity adminActivity;
    private RecyclerView rcvUserManagement;
    private List<users> listUsers;
    private UserManagementSwipeViewAdapter userManagementSwipeViewAdapter;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adminActivity = (AdminActivity) getActivity();
        loadingDialog = new LoadingDialog(adminActivity);
        view = inflater.inflate(R.layout.fragment_user_management, container, false);
        Mapping();
        CreateRecyclerView();
        loadingDialog.startLoadingDialog();
        GetData();
        rcvUserManagement.setAdapter(userManagementSwipeViewAdapter);

        return view;
    }

    private void GetData() {
        APIService.apiService.getUser().enqueue(new Callback<List<users>>() {
            @Override
            public void onResponse(Call<List<users>> call, Response<List<users>> response) {
                List<users> list = response.body();
                for(int i = 0; i < list.size(); i++){
                    if(!list.get(i).getIDRole().equals("R001")){
                        listUsers.add(list.get(i));
                    }
                }
                userManagementSwipeViewAdapter.setData(listUsers);
                loadingDialog.dismissDialog();
            }

            @Override
            public void onFailure(Call<List<users>> call, Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(adminActivity)
                        .setTitle("Thông báo")
                        .setMessage("Lỗi không thể lấy dữ liệu")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();
            }
        });
    }

    private void CreateRecyclerView() {
        userManagementSwipeViewAdapter = new UserManagementSwipeViewAdapter(view.getContext(), adminActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvUserManagement.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        rcvUserManagement.addItemDecoration(itemDecoration);
    }

    private void Mapping() {
        listUsers = new ArrayList<>();
        rcvUserManagement = view.findViewById(R.id.list_user_management);
    }
}