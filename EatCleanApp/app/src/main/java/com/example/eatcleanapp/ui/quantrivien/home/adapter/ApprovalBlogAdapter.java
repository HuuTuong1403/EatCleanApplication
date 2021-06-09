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
import com.example.eatcleanapp.IClickListener;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.model.blogs;
import com.example.eatcleanapp.ui.home.LoadingDialog;
import com.example.eatcleanapp.ui.quantrivien.AdminActivity;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApprovalBlogAdapter extends RecyclerView.Adapter<ApprovalBlogAdapter.ApprovalBlogViewHolder>{

    private Context context;
    private AdminActivity adminActivity;
    private IClickListener iClickListener;
    private List<blogs> listBlogs;
    private BottomNavigationView bottomNavigationView;

    public ApprovalBlogAdapter(Context context, AdminActivity adminActivity, IClickListener iClickListener) {
        this.context = context;
        this.adminActivity = adminActivity;
        this.iClickListener = iClickListener;
    }

    public void setData(List<blogs> list){
        this.listBlogs = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ApprovalBlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_approval_blog, parent, false);
        return new ApprovalBlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApprovalBlogViewHolder holder, int position) {
        blogs blog = listBlogs.get(position);
        if(blog == null){
            return;
        }
        Glide.with(context).load(blog.getImage()).placeholder(R.drawable.gray).into(holder.imgV_approvalBlog);
        holder.txv_approvalBlog_Title.setText(blog.getBlogTitle());
        holder.txv_approvalBlog_Author.setText(blog.getBlogAuthor());
        String date = FormatDate(blog.getTime());
        holder.txv_approvalBlog_Time.setText("Ngày đăng: " + date);

        bottomNavigationView = adminActivity.findViewById(R.id.bottom_menu_admin);

        holder.btn_approvalBlog_Approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approvalBlog(listBlogs.get(position).getIDBlog());
                listBlogs.remove(listBlogs.get(position));
                BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.menu_blog_not_approval);
                badgeDrawable.setNumber(listBlogs.size());
                badgeDrawable.setVisible(true);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });

        holder.btn_approvalBlog_Deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                denyBlog(listBlogs.get(position).getIDBlog());
                listBlogs.remove(listBlogs.get(position));
                BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.menu_blog_not_approval);
                badgeDrawable.setNumber(listBlogs.size());
                badgeDrawable.setVisible(true);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(listBlogs != null){
            return listBlogs.size();
        }
        return 0;
    }

    private void denyBlog(String IDBlog){
        APIService.apiService.denyBlog(IDBlog).enqueue(new Callback<blogs>() {
            @Override
            public void onResponse(Call<blogs> call, Response<blogs> response) {
                Toast.makeText(context, "Từ chối phê duyệt blog thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<blogs> call, Throwable t) {
                Toast.makeText(context, "Từ chối phê duyệt blog thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void approvalBlog(String IDBlog){
        APIService.apiService.approveBlog(IDBlog).enqueue(new Callback<blogs>() {
            @Override
            public void onResponse(Call<blogs> call, Response<blogs> response) {
                Toast.makeText(context, "Phê duyệt blog thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<blogs> call, Throwable t) {
                Toast.makeText(context, "Phê duyệt blog thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String FormatDate(String date){
        String newDate = null;
        try {
            Date Date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
            newDate = new SimpleDateFormat("dd/MM/yyyy").format(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public class ApprovalBlogViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgV_approvalBlog;
        private TextView txv_approvalBlog_Title, txv_approvalBlog_Author, txv_approvalBlog_Time;
        private Button btn_approvalBlog_Deny, btn_approvalBlog_Approval;

        public ApprovalBlogViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imgV_approvalBlog = (ImageView)itemView.findViewById(R.id.imgV_approvalBlog);
            txv_approvalBlog_Title = (TextView)itemView.findViewById(R.id.txv_approvalBlog_Title);
            txv_approvalBlog_Author = (TextView)itemView.findViewById(R.id.txv_approvalBlog_Author);
            txv_approvalBlog_Time = (TextView)itemView.findViewById(R.id.txv_approvalBlog_Time);
            btn_approvalBlog_Deny = (Button)itemView.findViewById(R.id.btn_approvalBlog_Deny);
            btn_approvalBlog_Approval = (Button)itemView.findViewById(R.id.btn_approvalBlog_Approval);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iClickListener.clickItem(getAdapterPosition());
                }
            });

        }
    }

}
