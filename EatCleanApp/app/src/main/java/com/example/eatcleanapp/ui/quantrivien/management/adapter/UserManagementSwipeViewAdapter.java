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
            final float scale = holder.itemView.getContext().getResources().getDisplayMetrics().density;
            holder.bottom_wrapper.getLayoutParams().width = (int) (175 * scale + 0.5f);
        }
        else{
            final float scale = holder.itemView.getContext().getResources().getDisplayMetrics().density;
            holder.bottom_wrapper.getLayoutParams().width = (int) (265 * scale + 0.5f);
        }
        holder.bottom_wrapper.requestLayout();
        holder.txv_role_userManagement.setText(HtmlCompat.fromHtml(changeNameRole(user.getIDRole()), HtmlCompat.FROM_HTML_MODE_LEGACY));
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
        private Button btn_userManagement_Delete, btn_userManagement_UnLock, btn_userManagement_Lock, btn_userManagement_Approval;
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
            bottom_wrapper = itemView.findViewById(R.id.bottom_wrapper);
        }
    }
}
