package com.zam.sidik_padang.roodiskusi.group.info;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

import com.zam.sidik_padang.roodiskusi.OnItemClickListener;
import com.zam.sidik_padang.roodiskusi.creategroup.AllUserListAdapter;
import com.zam.sidik_padang.roodiskusi.creategroup.UserViewHolder;
import com.zam.sidik_padang.util.User;
import com.zam.sidik_padang.util.Util;

public class AnggotaListAdapter extends AllUserListAdapter {

    private User iam;
    private String ownerId;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;

    public AnggotaListAdapter(List<User> userList, String ownerId, OnDeleteButtonClickListener deleteClickListener) {
        super(userList, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {

            }
        });
        this.ownerId = ownerId;
        this.onDeleteButtonClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (iam == null) iam = Util.getSavedUser(parent.getContext());
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final User user = userList.get(position);
        holder.imageDelete.setVisibility((iam.userid.equals(user.userid) || iam.userid.equals(ownerId)) ? View.VISIBLE : View.GONE);
        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteButtonClickListener.onDeleteButtonClick(user);
            }
        });
    }

    static interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(User user);
    }
}
