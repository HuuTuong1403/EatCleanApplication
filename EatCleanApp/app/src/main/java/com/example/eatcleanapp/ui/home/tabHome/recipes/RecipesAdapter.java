package com.example.eatcleanapp.ui.home.tabHome.recipes;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.eatcleanapp.API.APIService;
import com.example.eatcleanapp.CustomAlert.CustomAlertActivity;
import com.example.eatcleanapp.IClickListener;
import com.example.eatcleanapp.MainActivity;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.model.favoriterecipes;
import com.example.eatcleanapp.model.recipes;
import com.example.eatcleanapp.model.users;
import com.example.eatcleanapp.ui.home.signin.SignInFragment;
import com.example.eatcleanapp.ui.nguoidung.data_local.DataLocalManager;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> implements Filterable {

    private final Context context;
    private List<recipes> mListRecipes;
    private List<recipes> mListRecipesOld;
    private final IClickListener iClickListener;
    private boolean checkIsFavorite;
    private List<recipes> listFavoriteRecipes;

    public RecipesAdapter(Context context, IClickListener iClickListener, boolean checkIsFavorite) {
        this.iClickListener = iClickListener;
        this.context = context;
        this.checkIsFavorite = checkIsFavorite;
    }

    public void setData(List<recipes> list){
        this.mListRecipes = list;
        notifyDataSetChanged();
    }

    public void setOldData(List<recipes> list){
        this.mListRecipesOld = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_recipes, parent, false);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        recipes recipes = mListRecipes.get(position);
        if(recipes == null){
            return;
        }
        holder.recipes_name.setText(recipes.getRecipesTitle());
        String s = "C??ng th???c t???o b???i <b>" + recipes.getRecipesAuthor() + "</b>";
        holder.recipes_author.setText(HtmlCompat.fromHtml(s, HtmlCompat.FROM_HTML_MODE_LEGACY));
        Glide.with(context).load(recipes.getImage()).placeholder(R.drawable.gray).into(holder.recipes_image);
    }

    @Override
    public int getItemCount() {
        if(mListRecipes != null){
            return mListRecipes.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                List<recipes> list = new ArrayList<>();
                if(strSearch.isEmpty()){
                    mListRecipes = mListRecipesOld;
                }
                else{
                    for(recipes recipes: mListRecipesOld){
                        if(recipes.getRecipesTitle().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(recipes);
                        }
                    }
                    mListRecipes = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mListRecipes;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mListRecipes = (List<recipes>) results.values;
            }
        };
    }

    public List<recipes> reset(){
        notifyDataSetChanged();
        return mListRecipesOld;
    }

    public List<recipes> change(){
        notifyDataSetChanged();
        return mListRecipes;
    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder{

        private final ImageView recipes_image;
        private final TextView recipes_name;
        private final TextView recipes_author;
        private final ImageButton recipes_favorite;
        private boolean check = false;


        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);

            recipes_image = (ImageView)itemView.findViewById(R.id.recipes_image);
            recipes_name = (TextView)itemView.findViewById(R.id.recipes_name);
            recipes_author = (TextView)itemView.findViewById(R.id.recipes_author);
            recipes_favorite = (ImageButton)itemView.findViewById(R.id.recipes_favorite);

            Animation animScale = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.anim_scale_favorite);
            Handler handler = new Handler();
            users user = DataLocalManager.getUser();
            if (user != null){
                showFavoriteRecipes (user.getIDRole());
            }
            if(!checkIsFavorite){
                recipes_favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.startAnimation(animScale);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int position = getAdapterPosition();
                                if(user != null){
                                    if(!check){
                                        Dialog dialog = new Dialog(v.getContext());
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setContentView(R.layout.dialog_request_favorite_recipes);
                                        Window window = dialog.getWindow();
                                        if(window == null){
                                            return;
                                        }
                                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        WindowManager.LayoutParams windowAtributes = window.getAttributes();
                                        windowAtributes.gravity = Gravity.CENTER;
                                        window.setAttributes(windowAtributes);

                                        Button btn_accept = (Button)dialog.findViewById(R.id.dialog_request_favorite_recipes_btn_accept);
                                        Button btn_cancel = (Button)dialog.findViewById(R.id.dialog_request_favorite_recipes_btn_cancel);
                                        TextView txv_title = (TextView)dialog.findViewById(R.id.dialog_request_favorite_recipes_txv_title);

                                        String title = "B???n c?? mu???n th??m m??n <b>" + mListRecipes.get(position).getRecipesTitle() + "</b> v??o danh s??ch y??u th??ch c???a m??nh kh??ng?";
                                        txv_title.setText(HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY));
                                        Animation animTranslate = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.anim_scale);
                                        btn_accept.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                v.startAnimation(animTranslate);
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        recipes_favorite.setImageDrawable(v.getResources().getDrawable(R.drawable.favorite_red));
                                                        check = true;
                                                        String IDRecipes = mListRecipes.get(position).getIDRecipes();
                                                        String IDUser = user.getIDUser();
                                                        addRecipes(IDUser, IDRecipes);
                                                        dialog.dismiss();
                                                    }
                                                }, 400);
                                            }
                                        });

                                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                v.startAnimation(animTranslate);
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dialog.dismiss();
                                                        check = false;
                                                    }
                                                }, 400);
                                            }
                                        });
                                        dialog.setCancelable(true);
                                        dialog.show();
                                    }
                                    else
                                    {
                                        recipes_favorite.setImageDrawable(v.getResources().getDrawable(R.drawable.favorite_black));
                                        check = !check;
                                    }
                                }
                                else{
                                    ((MainActivity)context).settingSignIn();
                                }
                            }
                        }, 400);
                    }
                });
            }
            else{
                recipes_favorite.setImageDrawable(itemView.getResources().getDrawable(R.drawable.favorite_red));
                recipes_favorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        v.startAnimation(animScale);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recipes_favorite.setImageDrawable(itemView.getResources().getDrawable(R.drawable.favorite_black));
                                deleteFavoritesRecipes(user.getIDUser(), mListRecipes.get(position).getIDRecipes());
                            }
                        }, 400);
                    }
                });
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iClickListener.clickItem(getAdapterPosition());
                }
            });
        }

    }

    private void deleteFavoritesRecipes(String IDUser, String IDRecipe){
        APIService.apiService.deleteFavoriteRecipes(IDUser, IDRecipe).enqueue(new Callback<favoriterecipes>() {
            @Override
            public void onResponse(Call<favoriterecipes> call, retrofit2.Response<favoriterecipes> response) {
                Toast.makeText(context, "B???n ???? h???y y??u th??ch m??n ??n th??nh c??ng", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<favoriterecipes> call, Throwable t) {
                Toast.makeText(context, "???? x???y ra l???i trong qu?? tr??nh h???y y??u th??ch", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRecipes(String IDUser, String IDRecipes){
        APIService.apiService.addFavoriteRecipes(IDUser, IDRecipes).enqueue(new Callback<favoriterecipes>() {
            @Override
            public void onResponse(Call<favoriterecipes> call, Response<favoriterecipes> response) {
                Toast.makeText(context, "B???n th??m m??n ??n v??o m???c y??u th??ch th??nh c??ng", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<favoriterecipes> call, Throwable t) {
                Toast.makeText(context, "B???n th??m m??n ??n v??o m???c y??u th??ch kh??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showFavoriteRecipes(String IDUser){
        APIService.apiService.getFavorites_User(IDUser).enqueue(new Callback<List<recipes>>() {
            @Override
            public void onResponse(Call<List<recipes>> call, Response<List<recipes>> response) {
                listFavoriteRecipes = response.body();
            }

            @Override
            public void onFailure(Call<List<recipes>> call, Throwable t) {
                Toast.makeText(context, "Call Api Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
