package com.zam.sidik_padang.roodiskusi.creategroup;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.zam.sidik_padang.roodiskusi.OnItemClickListener;
import com.zam.sidik_padang.util.User;


public abstract class BaseUserListAdapter extends RecyclerView.Adapter<UserViewHolder> {

    protected List<User> userList;
    protected OnItemClickListener clickListener;

    public BaseUserListAdapter(List<User> userList, OnItemClickListener clickListener) {
        this.userList = userList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.textNama.setText(userList.get(position).nama);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
