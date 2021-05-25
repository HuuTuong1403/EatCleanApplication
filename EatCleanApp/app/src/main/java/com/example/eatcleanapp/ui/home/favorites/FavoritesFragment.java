package com.example.eatcleanapp.ui.home.favorites;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.eatcleanapp.IClickListener;
import com.example.eatcleanapp.MainActivity;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.model.recipes;
import com.example.eatcleanapp.model.users;
import com.example.eatcleanapp.ui.home.LoadingDialog;
import com.example.eatcleanapp.ui.home.detail.DetailActivity;
import com.example.eatcleanapp.ui.home.signin.SignInFragment;
import com.example.eatcleanapp.ui.home.tabHome.recipes.RecipesAdapter;
import com.example.eatcleanapp.ui.nguoidung.data_local.DataLocalManager;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment implements IClickListener {

    private View view;
    private TextView favorite_txv_signIn_signUp;
    private MainActivity mMainActivity;
    private users userIsLogin;
    private NavigationView navigationView;
    private Menu menu;
    private RecyclerView rcv_Favorites_Recipes;
    private RecipesAdapter mRecipesAdapter;
    private List<recipes> listFavoritesRecipes;
    private RequestQueue requestQueue;
    private String getRecipeLink;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        userIsLogin = DataLocalManager.getUser();
        if(userIsLogin != null){
            view = inflater.inflate(R.layout.fragment_favorites, container, false);
            Mapping();
            CreateViewRycler();
            loadingDialog.startLoadingDialog();
            GetData(getRecipeLink);
            rcv_Favorites_Recipes.setAdapter(mRecipesAdapter);
            Handler handler = new Handler();
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

    private void CreateViewRycler(){
        mRecipesAdapter = new RecipesAdapter(getContext(), this);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);;
        rcv_Favorites_Recipes.setLayoutManager(gridLayoutManager);
    }

    private void Mapping(){
        navigationView                  = (NavigationView)mMainActivity.findViewById(R.id.nav_view);
        favorite_txv_signIn_signUp = (TextView)view.findViewById(R.id.favorite_txv_signIn_signUp);
        menu                            = navigationView.getMenu();
        rcv_Favorites_Recipes           = (RecyclerView)view.findViewById(R.id.rcv_favoritesList_User);
        getRecipeLink = "https://msteatclean.000webhostapp.com/getRecipes.php";
        requestQueue = Volley.newRequestQueue(view.getContext());
        listFavoritesRecipes = new ArrayList<>();
        loadingDialog = new LoadingDialog(mMainActivity);
    }

    public void GetData (String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i ++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        int checkExist = 0;
                        recipes recipe = new recipes(
                                object.getString("IDRecipes"),
                                object.getString("RecipesTitle"),
                                object.getString("RecipesAuthor"),
                                object.getString("RecipesContent"),
                                object.getString("NutritionalIngredients"),
                                object.getString("Ingredients"),
                                object.getString("Steps"),
                                object.getString("Time"),
                                object.getString("Status"),
                                object.getString("RecipesImages")
                        );
                        for (recipes recipetemp: listFavoritesRecipes) {
                            if (recipetemp.getIDRecipes().equals(recipe.getIDRecipes())){
                                checkExist = 1;
                                break;
                            }
                        }
                        if (checkExist == 0){
                            listFavoritesRecipes.add(recipe);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mRecipesAdapter.setData(listFavoritesRecipes);
                loadingDialog.dismissDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void clickItem(int position) {
        Intent intent = new Intent(view.getContext(), DetailActivity.class);
        intent.putExtra("detail-back", 1);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", listFavoritesRecipes.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}