package com.example.eatcleanapp.ui.quantrivien.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatcleanapp.API.APIService;
import com.example.eatcleanapp.CustomAlert.CustomAlertActivity;
import com.example.eatcleanapp.IClickListener;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.model.recipeimages;
import com.example.eatcleanapp.model.recipes;
import com.example.eatcleanapp.ui.home.detail.DetailActivity;
import com.example.eatcleanapp.ui.quantrivien.AdminActivity;
import com.example.eatcleanapp.ui.quantrivien.home.adapter.ApprovalRecipeAdapter;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesApprovalAdminFragment extends Fragment implements IClickListener {

    private View view;
    private RecyclerView rcvApprovalRecipes;
    private List<recipes> listRecipes;
    private ApprovalRecipeAdapter approvalRecipeAdapter;
    private AdminActivity mAdminActivity;
    private List<recipeimages> listRecipeImage;
    private BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAdminActivity = (AdminActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_recipes_approval_admin, container, false);
        Mapping();
        CreateRecyclerView();
        GetData();
        rcvApprovalRecipes.setAdapter(approvalRecipeAdapter);
        return view;
    }

    public void GetData() {
        APIService.apiService.getRecipeWaitingApproval().enqueue(new Callback<List<recipes>>() {
            @Override
            public void onResponse(Call<List<recipes>> call, Response<List<recipes>> response) {
                listRecipes = response.body();
                setBadge();
                approvalRecipeAdapter.setData(listRecipes);
                GetImage();
            }

            @Override
            public void onFailure(Call<List<recipes>> call, Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(mAdminActivity)
                        .setTitle("Thông báo")
                        .setMessage("Lỗi không thể lấy dữ liệu")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();              }
        });
    }

    private void GetImage() {
        APIService.apiService.getImageRecipe().enqueue(new Callback<List<recipeimages>>() {
            @Override
            public void onResponse(Call<List<recipeimages>> call, Response<List<recipeimages>> response) {
                listRecipeImage = response.body();
                for(int i = 0; i < listRecipes.size(); i++){
                    for(int j = 0; j < listRecipeImage.size(); j++){
                        if(listRecipes.get(i).getIDRecipes().equals(listRecipeImage.get(j).getIDRecipes())){
                            listRecipes.get(i).setImage(listRecipeImage.get(j).getRecipesImages());
                            break;
                        }
                    }
                }
                approvalRecipeAdapter.setData(listRecipes);
            }

            @Override
            public void onFailure(Call<List<recipeimages>> call, Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(mAdminActivity)
                        .setTitle("Thông báo")
                        .setMessage("Lỗi không thể lấy dữ liệu")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();              }
        });
    }

    private void CreateRecyclerView() {
        approvalRecipeAdapter = new ApprovalRecipeAdapter(view.getContext(), this, mAdminActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvApprovalRecipes.setLayoutManager(linearLayoutManager);
    }

    public void Mapping() {
        listRecipes = new ArrayList<>();
        rcvApprovalRecipes = view.findViewById(R.id.list_recipes_approval);
        listRecipeImage = new ArrayList<>();
        bottomNavigationView = mAdminActivity.findViewById(R.id.bottom_menu_admin);
    }

    public void setBadge(){
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.menu_recipes24_not_approval);
        badgeDrawable.setNumber(listRecipes.size());
        badgeDrawable.setVisible(true);
    }

    @Override
    public void clickItem(int position) {
        Intent intent = new Intent(view.getContext(), DetailActivity.class);
        intent.putExtra("detail-back", 1);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", listRecipes.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}