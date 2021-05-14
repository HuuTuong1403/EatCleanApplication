package com.example.eatcleanapp.ui.home.tabHome;

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
import com.example.eatcleanapp.ui.home.tabHome.recipes.RecipesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RecipesFragment extends Fragment {

    private View view;
    private RecyclerView rcvRecipes;
    private RecipesAdapter mRecipesAdapter;
    List<recipes> listRecipes = new ArrayList<recipes>();
    public static RecipesFragment newInstance() { return new RecipesFragment(); }
    private String getRecipeLink = "https://eatcleanrecipes.000webhostapp.com/getRecipes.php";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recipes, container, false);
        rcvRecipes = view.findViewById(R.id.list_recipes);
        mRecipesAdapter = new RecipesAdapter(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);

        rcvRecipes.setLayoutManager(gridLayoutManager);

        GetData(getRecipeLink);
        rcvRecipes.setAdapter(mRecipesAdapter);
        return view;
    }
    private void GetData (String url){
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //oast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                for (int i = 0; i < response.length(); i ++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        listRecipes.add(new recipes(
                                object.getString("IDRecipes"),
                                object.getString("RecipesAuthor"),
                                object.getString("RecipesTitle"),
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
    /*private List<Recipes> getListData(){
        List<Recipes> list = new ArrayList<>();
        list.add(new Recipes(R.drawable.background, "Tường"));
        list.add(new Recipes(R.drawable.background, "Tường"));
        list.add(new Recipes(R.drawable.background, "Tường"));
        list.add(new Recipes(R.drawable.background, "Tường"));
        list.add(new Recipes(R.drawable.background, "Tường"));
        list.add(new Recipes(R.drawable.background, "Tường"));
        list.add(new Recipes(R.drawable.background, "Tường"));
        list.add(new Recipes(R.drawable.background, "Tường"));
        list.add(new Recipes(R.drawable.background, "Tường"));
        list.add(new Recipes(R.drawable.background, "Tường"));
        list.add(new Recipes(R.drawable.background, "Tường"));
        list.add(new Recipes(R.drawable.background, "Tường"));

        return list;
    }*/
}