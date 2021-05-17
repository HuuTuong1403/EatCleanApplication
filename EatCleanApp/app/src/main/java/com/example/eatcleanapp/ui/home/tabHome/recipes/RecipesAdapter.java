package com.example.eatcleanapp.ui.home.tabHome.recipes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eatcleanapp.IClickListener;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.model.recipes;

import java.util.ArrayList;
import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> implements Filterable {

    private Context context;
    private List<recipes> mListRecipes;
    private List<recipes> mListRecipesOld;
    private IClickListener iClickListener;

    public RecipesAdapter(Context context, IClickListener iClickListener) {
        this.iClickListener = iClickListener;
        this.context = context;
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
        String s = "Công thức tạo bởi <b>" + recipes.getRecipesAuthor() + "</b>";
        holder.recipes_author.setText(HtmlCompat.fromHtml(s, HtmlCompat.FROM_HTML_MODE_LEGACY));
        Glide.with(context).load(recipes.getImage()).into(holder.recipes_image);
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
                //notifyDataSetChanged();
            }
        };
    }



    public List<recipes> change(){
        notifyDataSetChanged();
        return mListRecipes;
    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder{

        private final ImageView recipes_image;
        private final TextView recipes_name;
        private final TextView recipes_author;

        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);

            recipes_image = (ImageView)itemView.findViewById(R.id.recipes_image);
            recipes_name = (TextView)itemView.findViewById(R.id.recipes_name);
            recipes_author = (TextView)itemView.findViewById(R.id.recipes_author);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iClickListener.clickItem(getAdapterPosition());
                }
            });
        }

    }
}
