package com.example.eatcleanapp.ui.congtacvien.tabCTV;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.eatcleanapp.API.APIService;
import com.example.eatcleanapp.CustomAlert.CustomAlertActivity;
import com.example.eatcleanapp.IClickListener;
import com.example.eatcleanapp.MainActivity;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.model.recipeimages;
import com.example.eatcleanapp.model.recipes;
import com.example.eatcleanapp.ui.home.LoadingDialog;
import com.example.eatcleanapp.ui.home.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListAddRecipeFragment extends Fragment implements IClickListener {

    private View view;
    private RecyclerView rcvUpdateRecipe;
    private UpdateRecipeAdapter updateRecipeAdapter;
    private List<recipes> listRecipes;
    private MainActivity mMainActivity;
    private List<recipeimages> listRecipeImage;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_list_add_recipe, container, false);
        loadingDialog = new LoadingDialog(mMainActivity);
        Mapping();
        CreateRecyclerView();
        loadingDialog.startLoadingDialog();
        GetData();
        rcvUpdateRecipe.setAdapter(updateRecipeAdapter);
        return view;
    }

    private void GetImage() {
        APIService.apiService.getRecipeImages().enqueue(new Callback<List<recipeimages>>() {
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
                updateRecipeAdapter.setData(listRecipes);
            }

            @Override
            public void onFailure(Call<List<recipeimages>> call, Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(mMainActivity)
                        .setTitle("Th??ng b??o")
                        .setMessage("L???i kh??ng l???y ???????c d??? li???u")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();
            }
        });
    }

    private void GetData() {
        APIService.apiService.getRecipesNoImage().enqueue(new Callback<List<recipes>>() {
            @Override
            public void onResponse(Call<List<recipes>> call, Response<List<recipes>> response) {
                listRecipes = response.body();
                updateRecipeAdapter.setData(listRecipes);
                GetImage();
                loadingDialog.dismissDialog();
            }

            @Override
            public void onFailure(Call<List<recipes>> call, Throwable t) {
                loadingDialog.dismissDialog();
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(getActivity())
                        .setTitle("Th??ng b??o")
                        .setMessage("L???i kh??ng l???y ???????c d??? li???u")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();
            }
        });
    }

    private void CreateRecyclerView() {
        updateRecipeAdapter = new UpdateRecipeAdapter(getContext(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvUpdateRecipe.setLayoutManager(linearLayoutManager);
    }

    private void Mapping() {
        listRecipes = new ArrayList<>();
        rcvUpdateRecipe = view.findViewById(R.id.list_updateRecipe);
        listRecipeImage = new ArrayList<>();
    }

    @Override
    public void clickItem(int position) {
        Intent intent = new Intent(view.getContext(), DetailActivity.class);
        intent.putExtra("detail-back", 3);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", listRecipes.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}