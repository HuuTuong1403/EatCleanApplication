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
import com.example.eatcleanapp.model.blogimages;
import com.example.eatcleanapp.model.blogs;
import com.example.eatcleanapp.ui.home.LoadingDialog;
import com.example.eatcleanapp.ui.home.detail.DetailActivity;
import com.example.eatcleanapp.ui.quantrivien.AdminActivity;
import com.example.eatcleanapp.ui.quantrivien.home.adapter.ApprovalBlogAdapter;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogApprovalAdminFragment extends Fragment implements IClickListener {

    private View view;
    private RecyclerView rcvApprovalBlogs;
    private List<blogs> listBlogs;
    private ApprovalBlogAdapter approvalBlogAdapter;
    private AdminActivity adminActivity;
    private List<blogimages> listBlogImage;
    private BottomNavigationView bottomNavigationView;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adminActivity = (AdminActivity) getActivity();
        loadingDialog = new LoadingDialog(adminActivity);
        view = inflater.inflate(R.layout.fragment_blog_approval_admin, container, false);
        Mapping();
        CreateRecyclerView();
        loadingDialog.startLoadingDialog();
        GetData();
        rcvApprovalBlogs.setAdapter(approvalBlogAdapter);
        return view;
    }

    private void GetData() {
        APIService.apiService.getBlogWaitingApproval().enqueue(new Callback<List<blogs>>() {
            @Override
            public void onResponse(Call<List<blogs>> call, Response<List<blogs>> response) {
                listBlogs = response.body();
                setBadge();
                approvalBlogAdapter.setData(listBlogs);
                GetImage();
                loadingDialog.dismissDialog();
            }

            @Override
            public void onFailure(Call<List<blogs>> call, Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(adminActivity)
                        .setTitle("Thông báo")
                        .setMessage("Lỗi không thể lấy dữ liệu")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();              }
        });
    }

    private void GetImage() {
        APIService.apiService.getImageBlog().enqueue(new Callback<List<blogimages>>() {
            @Override
            public void onResponse(Call<List<blogimages>> call, Response<List<blogimages>> response) {
                listBlogImage = response.body();
                for(int i = 0; i < listBlogs.size(); i++){
                    for(int j = 0; j < listBlogImage.size(); j++){
                        if(listBlogs.get(i).getIDBlog().equals(listBlogImage.get(j).getIDBlog())){
                            listBlogs.get(i).setImage(listBlogImage.get(j).getBlogImages());
                            break;
                        }
                    }
                }
                approvalBlogAdapter.setData(listBlogs);
            }

            @Override
            public void onFailure(Call<List<blogimages>> call, Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(adminActivity)
                        .setTitle("Thông báo")
                        .setMessage("Lỗi không thể lấy dữ liệu")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();              }
        });
    }

    private void setBadge() {
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.menu_blog_not_approval);
        badgeDrawable.setNumber(listBlogs.size());
        badgeDrawable.setVisible(true);
    }

    private void CreateRecyclerView() {
        approvalBlogAdapter = new ApprovalBlogAdapter(view.getContext(), adminActivity, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvApprovalBlogs.setLayoutManager(linearLayoutManager);
    }

    private void Mapping() {
        listBlogs = new ArrayList<>();
        listBlogImage = new ArrayList<>();
        rcvApprovalBlogs = view.findViewById(R.id.list_blogs_approval);
        bottomNavigationView = adminActivity.findViewById(R.id.bottom_menu_admin);
    }

    @Override
    public void clickItem(int position) {
        Intent intent = new Intent(view.getContext(), DetailActivity.class);
        intent.putExtra("detail-back", 2);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", listBlogs.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}