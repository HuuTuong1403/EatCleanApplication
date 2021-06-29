package com.example.eatcleanapp.ui.home.tabHome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.eatcleanapp.API.APIService;
import com.example.eatcleanapp.CustomAlert.CustomAlertActivity;
import com.example.eatcleanapp.IClickListener;
import com.example.eatcleanapp.MainActivity;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.model.recipeimages;
import com.example.eatcleanapp.model.recipes;
import com.example.eatcleanapp.ui.home.LoadingDialog;
import com.example.eatcleanapp.ui.home.detail.DetailActivity;
import com.example.eatcleanapp.ui.home.tabHome.recipes.RecipesAdapter;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;


public class RecipesFragment extends Fragment implements IClickListener {

    private View view;
    private RecyclerView rcvRecipes;
    private RecipesAdapter mRecipesAdapter;
    private List<recipes> listRecipes, oldList;
    private String getRecipeLink;
    private RequestQueue requestQueue;
    private EditText edt_search_recycle;
    private MainActivity mMainActivity;
    private LoadingDialog loadingDialog;
    private List<recipeimages> listRecipeImages;
    private CountDownTimer timer;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_recipes, container, false);
        loadingDialog = new LoadingDialog(mMainActivity);
        Mapping();
        mRecipesAdapter = new RecipesAdapter(getContext(), this, false);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);;
        rcvRecipes.setLayoutManager(gridLayoutManager);
        loadingDialog.startLoadingDialog();
        GetData(getRecipeLink);
        rcvRecipes.setAdapter(mRecipesAdapter);
        Handler handler = new Handler();
        edt_search_recycle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fillText();
                        }
                    }, 400);
                    return true;
                }
                return false;
            }
        });

        edt_search_recycle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fillText();
                        }
                    }, 400);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void fillText() {
        String s = edt_search_recycle.getText().toString();
        mRecipesAdapter.getFilter().filter(s);
        listRecipes = mRecipesAdapter.change();
    }


    private void Mapping(){
        requestQueue = Volley.newRequestQueue(view.getContext());
        listRecipes = new ArrayList<>();
        oldList = new ArrayList<>();
        rcvRecipes = view.findViewById(R.id.list_recipes);
        getRecipeLink = "https://msteatclean.000webhostapp.com/getRecipes.php";
        edt_search_recycle = (EditText)mMainActivity.findViewById(R.id.edt_search_recycler);
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
                        for (recipes recipetemp: listRecipes) {
                            if (recipetemp.getIDRecipes().equals(recipe.getIDRecipes())){
                                checkExist = 1;
                                break;
                            }
                        }
                        if (checkExist == 0){
                            listRecipes.add(recipe);
                            oldList.add(recipe);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                GetImageRandom();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(mMainActivity)
                        .setTitle("Thông báo")
                        .setMessage("Đã xảy ra lỗi: " + error.toString())
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    public void GetImageRandom() {
        APIService.apiService.getRecipeImages().enqueue(new Callback<List<recipeimages>>() {
            @Override
            public void onResponse(Call<List<recipeimages>> call, retrofit2.Response<List<recipeimages>> response) {
                listRecipeImages = response.body();
                mRecipesAdapter.setData(listRecipes);
                mRecipesAdapter.setOldData(oldList);
                loadingDialog.dismissDialog();
                randomPicture ();
            }

            @Override
            public void onFailure(Call<List<recipeimages>> call, Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(mMainActivity)
                        .setTitle("Thông báo")
                        .setMessage("Lỗi không lấy được dữ liệu")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();
            }
        });
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
    public void randomPicture (){
        timer = new CountDownTimer(2000, 20) {

            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                try{
                    repeatPicture();
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();
    }
    public void repeatPicture (){
        List<recipeimages> lstTemp = new ArrayList<>();
        for (int i = 0; i < oldList.size(); i ++){
            lstTemp.clear();
            for (int j = 0; j < listRecipeImages.size(); j ++){
                if ( listRecipeImages.get(j).getIDRecipes().equals(oldList.get(i).getIDRecipes())){
                    lstTemp.add(listRecipeImages.get(j));
                }
            }
            Random rd = new Random();
            if (lstTemp.size() > 0){
                int x = rd.nextInt(((lstTemp.size() - 1) - 0 + 1) + 0);
                oldList.get(i).setImage(lstTemp.get(x).getRecipesImages());
                listRecipes.get(i).setImage(lstTemp.get(x).getRecipesImages());
            }
        }
        mRecipesAdapter.setData(listRecipes);
        mRecipesAdapter.setOldData(oldList);
        randomPicture ();
    }
}