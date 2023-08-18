package com.zam.sidik_padang.roodiskusi.creategroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.roodiskusi.OnItemClickListener;
import com.zam.sidik_padang.util.User;


public class SelectedUserListAdapter extends BaseUserListAdapter {
    public SelectedUserListAdapter(List<User> userList, OnItemClickListener clickListener) {
        super(userList, clickListener);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = holder.getAdapterPosition();
                clickListener.onItemClick(userList.get(i), i);
            }
        });
    }
}
