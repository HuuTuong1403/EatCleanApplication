package com.example.eatcleanapp.ui.home.tabHome;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.eatcleanapp.MainActivity;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.model.recipes;
import com.example.eatcleanapp.ui.home.detail.DetailActivity;
import com.example.eatcleanapp.ui.home.tabHome.recipes.RecipesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RecipesFragment extends Fragment implements RecipesAdapter.ItemClickListener{

    private View view;
    private RecyclerView rcvRecipes;
    private RecipesAdapter mRecipesAdapter;
    private List<recipes> listRecipes;
    private String getRecipeLink;
    private RequestQueue requestQueue;

    public static RecipesFragment newInstance() { return new RecipesFragment(); }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recipes, container, false);
        Mapping();
        mRecipesAdapter = new RecipesAdapter(getContext(), this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rcvRecipes.setLayoutManager(gridLayoutManager);

        GetData(getRecipeLink);
        rcvRecipes.setAdapter(mRecipesAdapter);
        return view;
    }

    private void Mapping(){
        requestQueue = Volley.newRequestQueue(view.getContext());
        listRecipes = new ArrayList<>();
        rcvRecipes = view.findViewById(R.id.list_recipes);
        getRecipeLink = "https://eatcleanrecipes.000webhostapp.com/getRecipes.php";
    }


    private void GetData (String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i ++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        listRecipes.add(new recipes(
                                object.getString("IDRecipes"),
                                object.getString("RecipesTitle"),
                                object.getString("RecipesAuthor"),
                                object.getString("RecipesContent"),
                                object.getString("NutritionalIngredients"),
                                object.getString("Ingredients"),
                                object.getString("Steps"),
                                object.getString("Time")
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mRecipesAdapter.setData(listRecipes);
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
    public void onItemClick(int position) {
        Intent intent = new Intent(view.getContext(), DetailActivity.class);
        intent.putExtra("detail-back", 1);
        startActivity(intent);
    }
}