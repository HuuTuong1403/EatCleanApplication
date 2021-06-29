package com.example.eatcleanapp.ui.quantrivien.management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.model.users;
import com.example.eatcleanapp.ui.quantrivien.AdminActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserManagementSwipeViewAdapter extends RecyclerView.Adapter<UserManagementSwipeViewAdapter.UserManagementSwipeViewHolder> {

    private Context context;
    private AdminActivity adminActivity;
    private List<users> listUsers;

    public UserManagementSwipeViewAdapter(Context context, AdminActivity adminActivity) {
        this.context = context;
        this.adminActivity = adminActivity;
    }

    public void setData(List<users> list){
        this.listUsers = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserManagementSwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_user_management, parent, false);
        return new UserManagementSwipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserManagementSwipeViewHolder holder, int position) {
        users user = listUsers.get(position);
        if(user == null){
            return;
        }
        Glide.with(context).load(user.getImage()).placeholder(R.drawable.gray).into(holder.imgv_avatar_userManagement);
        holder.txv_fullName_userManagement.setText(HtmlCompat.fromHtml("Họ tên: " + getBold(user.getFullName()), HtmlCompat.FROM_HTML_MODE_LEGACY));
        holder.txv_userName_userManagement.setText(HtmlCompat.fromHtml("Tên đăng nhập: " + getBold(user.getUsername()), HtmlCompat.FROM_HTML_MODE_LEGACY));
        holder.txv_email_userManagement.setText(HtmlCompat.fromHtml("Email: " + getBold(user.getEmail()), HtmlCompat.FROM_HTML_MODE_LEGACY));

        if(user.getIDRole().equals("R003")){
            holder.btn_userManagement_Approval.setVisibility(View.GONE);
            holder.btn_userManagement_Denied.setVisibility(View.GONE);
            final float scale = holder.itemView.getContext().getResources().getDisplayMetrics().density;
            holder.bottom_wrapper.getLayoutParams().width = (int) (175 * scale + 0.5f);
        }
        else{
            if(user.getStatus().equals("waitingforapproval")){
                holder.btn_userManagement_Delete.setVisibility(View.GONE);
                holder.btn_userManagement_Lock.setVisibility(View.GONE);
            }
            if(user.getStatus().equals("approval")){
                hideShowButtonApproval(holder);
            }
            if(user.getStatus().equals("denied")){
                hideButton(holder);
            }
            final float scale = holder.itemView.getContext().getResources().getDisplayMetrics().density;
            holder.bottom_wrapper.getLayoutParams().width = (int) (175 * scale + 0.5f);
        }
        if(user.getStatus().equals("locked")){
            holder.btn_userManagement_UnLock.setVisibility(View.VISIBLE);
            holder.btn_userManagement_Lock.setVisibility(View.GONE);
        }
        holder.bottom_wrapper.requestLayout();
        holder.txv_role_userManagement.setText(HtmlCompat.fromHtml(changeNameRole(user.getIDRole()), HtmlCompat.FROM_HTML_MODE_LEGACY));
        holder.txv_status_userManagement.setText(HtmlCompat.fromHtml(changeNameStatus(user.getStatus()), HtmlCompat.FROM_HTML_MODE_LEGACY));

        holder.btn_userManagement_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(user.getIDUser());
            }
        });

        holder.btn_userManagement_UnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setStatus("approval");
                holder.txv_status_userManagement.setText(HtmlCompat.fromHtml(changeNameStatus(user.getStatus()), HtmlCompat.FROM_HTML_MODE_LEGACY));
                holder.btn_userManagement_UnLock.setVisibility(View.GONE);
                holder.btn_userManagement_Lock.setVisibility(View.VISIBLE);
                unLockUser(user.getIDUser());
            }
        });

        holder.btn_userManagement_Lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setStatus("locked");
                holder.txv_status_userManagement.setText(HtmlCompat.fromHtml(changeNameStatus(user.getStatus()), HtmlCompat.FROM_HTML_MODE_LEGACY));
                holder.btn_userManagement_UnLock.setVisibility(View.VISIBLE);
                holder.btn_userManagement_Lock.setVisibility(View.GONE);
                lockUser(user.getIDUser());
            }
        });

        holder.btn_userManagement_Approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setStatus("approval");
                holder.txv_status_userManagement.setText(HtmlCompat.fromHtml(changeNameStatus(user.getStatus()), HtmlCompat.FROM_HTML_MODE_LEGACY));
                hideShowButtonApproval(holder);
                approvalCongTacvien(user.getIDUser());
            }
        });

        holder.btn_userManagement_Denied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setStatus("denied");
                holder.txv_status_userManagement.setText(HtmlCompat.fromHtml(changeNameStatus(user.getStatus()), HtmlCompat.FROM_HTML_MODE_LEGACY));
                hideButton(holder);
                deniedCongTacVien(user.getIDUser());
            }
        });
    }

    private void hideShowButtonApproval(UserManagementSwipeViewHolder holder) {
        //HIDE BUTTON
        holder.btn_userManagement_Approval.setVisibility(View.GONE);
        holder.btn_userManagement_Denied.setVisibility(View.GONE);

        //SHOW BUTTON
        holder.btn_userManagement_Delete.setVisibility(View.VISIBLE);
        holder.btn_userManagement_Lock.setVisibility(View.VISIBLE);
    }

    private void hideButton(@NonNull UserManagementSwipeViewHolder holder) {
        //HIDE BUTTON
        holder.btn_userManagement_Approval.setVisibility(View.GONE);
        holder.btn_userManagement_Denied.setVisibility(View.GONE);
        holder.btn_userManagement_Delete.setVisibility(View.GONE);
        holder.btn_userManagement_Lock.setVisibility(View.GONE);
    }

    private void deniedCongTacVien(String IDUser) {

    }

    private void approvalCongTacvien(String IDUser) {

    }

    private void unLockUser(String IDUser) {

    }

    private void lockUser(String IDUser) {

    }

    private void deleteUser(String IDUser) {

    }

    private String changeNameStatus(String Status) {
        String s = "Tình trạng tài khoản: ";
        String result = "";
        if(Status.equals("waitingforapproval"))
            result = s + getBold("Chưa được duyệt");
        if(Status.equals("approval"))
            result = s + getBold("Đã duyệt");
        if(Status.equals("locked")){
            result = s + getBold("Đã khóa");
        }
        if(Status.equals("denied"))
            result = s + getBold("Đã từ chối");
        return result;
    }

    private String getBold(String name){
        return "<b>" + name + "<b>";
    }

    private String changeNameRole(String IDRole){
        return "Chức vụ: " + (IDRole.equals("R002") ? getBold("Cộng tác viên") : getBold("Người dùng"));
    }

    @Override
    public int getItemCount() {
        if(listUsers != null)
            return listUsers.size();
        return 0;
    }


    public class UserManagementSwipeViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgv_avatar_userManagement;
        private TextView txv_fullName_userManagement, txv_userName_userManagement, txv_email_userManagement, txv_role_userManagement, txv_status_userManagement;
        private Button btn_userManagement_Delete, btn_userManagement_UnLock, btn_userManagement_Lock, btn_userManagement_Approval, btn_userManagement_Denied;
        private LinearLayout bottom_wrapper;
        public UserManagementSwipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imgv_avatar_userManagement = itemView.findViewById(R.id.imgv_avatar_userManagement);
            txv_fullName_userManagement = itemView.findViewById(R.id.txv_fullName_userManagement);
            txv_userName_userManagement = itemView.findViewById(R.id.txv_userName_userManagement);
            txv_email_userManagement = itemView.findViewById(R.id.txv_email_userManagement);
            txv_role_userManagement = itemView.findViewById(R.id.txv_role_userManagement);
            txv_status_userManagement = itemView.findViewById(R.id.txv_status_userManagement);
            btn_userManagement_Delete = itemView.findViewById(R.id.btn_userManagement_Delete);
            btn_userManagement_UnLock = itemView.findViewById(R.id.btn_userManagement_UnLock);
            btn_userManagement_Lock = itemView.findViewById(R.id.btn_userManagement_Lock);
            btn_userManagement_Approval = itemView.findViewById(R.id.btn_userManagement_Approval);
            btn_userManagement_Denied = itemView.findViewById(R.id.btn_userManagement_Denied);
            bottom_wrapper = itemView.findViewById(R.id.bottom_wrapper);
        }
    }
}
