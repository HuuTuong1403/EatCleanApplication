package com.example.eatcleanapp.ui.quantrivien.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eatcleanapp.API.APIService;
import com.example.eatcleanapp.CustomAlert.CustomAlertActivity;
import com.example.eatcleanapp.IClickListener;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.model.recipes;
import com.example.eatcleanapp.ui.quantrivien.AdminActivity;
import com.example.eatcleanapp.ui.quantrivien.home.RecipesApprovalAdminFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ApprovalRecipeAdapter extends RecyclerView.Adapter<ApprovalRecipeAdapter.ApprovalRecipeViewHolder>{

    private Context context;
    private IClickListener iClickListener;
    private List<recipes> listRecipes;
    private AdminActivity adminActivity;
    private BottomNavigationView bottomNavigationView;

    public ApprovalRecipeAdapter(Context context, IClickListener iClickListener, AdminActivity adminActivity) {
        this.context = context;
        this.iClickListener = iClickListener;
        this.adminActivity = adminActivity;
    }

    public void setData(List<recipes> list){
        this.listRecipes = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ApprovalRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_approval_recipes, parent, false);
        return new ApprovalRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApprovalRecipeViewHolder holder, int position) {
        recipes recipe = listRecipes.get(position);
        if(recipe == null){
            return;
        }
        Glide.with(context).load(recipe.getImage()).placeholder(R.drawable.gray).into(holder.imgV_approvalRecipe);
        holder.txv_approvalRecipe_Title.setText(recipe.getRecipesTitle());
        holder.txv_approvalRecipe_Author.setText(recipe.getRecipesAuthor());

        bottomNavigationView = adminActivity.findViewById(R.id.bottom_menu_admin);


        holder.btn_approvalRecipe_Approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIService.apiService.approveRecipe(listRecipes.get(position).getIDRecipes()).enqueue(new Callback<recipes>() {
                    @Override
                    public void onResponse(Call<recipes> call, Response<recipes> response) {
                        CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                                .setActivity(adminActivity)
                                .setTitle("Th??ng b??o")
                                .setMessage("Ph?? duy???t c??ng th???c th??nh c??ng")
                                .setType("success")
                                .Build();
                        customAlertActivity.showDialog();
                        listRecipes.remove(listRecipes.get(position));
                        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.menu_recipes24_not_approval);
                        badgeDrawable.setNumber(listRecipes.size());
                        badgeDrawable.setVisible(true);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<recipes> call, Throwable t) {
                        CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                                .setActivity(adminActivity)
                                .setTitle("Th??ng b??o")
                                .setMessage("Ph?? duy???t c??ng th???c th???t b???i")
                                .setType("error")
                                .Build();
                        customAlertActivity.showDialog();
                    }
                });
            }
        });

        holder.btn_approvalRecipe_Deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIService.apiService.denyRecipe(listRecipes.get(position).getIDRecipes()).enqueue(new Callback<recipes>() {
                    @Override
                    public void onResponse(Call<recipes> call, Response<recipes> response) {
                        CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                                .setActivity(adminActivity)
                                .setTitle("Th??ng b??o")
                                .setMessage("T??? ch???i ph?? duy???t c??ng th???c th??nh c??ng")
                                .setType("success")
                                .Build();
                        customAlertActivity.showDialog();
                        listRecipes.remove(listRecipes.get(position));
                        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.menu_recipes24_not_approval);
                        badgeDrawable.setNumber(listRecipes.size());
                        badgeDrawable.setVisible(true);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<recipes> call, Throwable t) {
                        CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                                .setActivity(adminActivity)
                                .setTitle("Th??ng b??o")
                                .setMessage("T??? ch???i ph?? duy???t c??ng th???c th???t b???i")
                                .setType("error")
                                .Build();
                        customAlertActivity.showDialog();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listRecipes != null){
            return listRecipes.size();
        }
        return 0;
    }

    public class ApprovalRecipeViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgV_approvalRecipe;
        private TextView txv_approvalRecipe_Title, txv_approvalRecipe_Author;
        private Button btn_approvalRecipe_Deny, btn_approvalRecipe_Approval;
        private RecipesApprovalAdminFragment recipesApprovalAdminFragment;

        public ApprovalRecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            imgV_approvalRecipe = (ImageView)itemView.findViewById(R.id.imgV_approvalRecipe);
            txv_approvalRecipe_Title = (TextView)itemView.findViewById(R.id.txv_approvalRecipe_Title);
            txv_approvalRecipe_Author = (TextView)itemView.findViewById(R.id.txv_approvalRecipe_Author);
            btn_approvalRecipe_Deny = (Button)itemView.findViewById(R.id.btn_approvalRecipe_Deny);
            btn_approvalRecipe_Approval = (Button)itemView.findViewById(R.id.btn_approvalRecipe_Approval);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iClickListener.clickItem(getAdapterPosition());
                }
            });

        }
    }
}
