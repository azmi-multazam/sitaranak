package com.zam.sidik_padang.roodiskusi.creategroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.roodiskusi.OnItemClickListener;
import com.zam.sidik_padang.util.User;


public class AllUserListAdapter extends BaseUserListAdapter {

    public AllUserListAdapter(List<User> userList, OnItemClickListener clickListener) {
        super(userList, clickListener);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final User user = userList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(user, holder.getAdapterPosition());
            }
        });
        if (user.foto != null && user.foto.contains("/"))
            Glide.with(holder.itemView.getContext()).load(user.foto).into(holder.imageView);


    }
}
