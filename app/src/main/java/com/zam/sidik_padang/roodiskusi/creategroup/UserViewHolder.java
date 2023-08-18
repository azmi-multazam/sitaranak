package com.zam.sidik_padang.roodiskusi.creategroup;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;


public class UserViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageDelete;
    TextView textNama;
    ImageView imageView;

    public UserViewHolder(View itemView) {
        super(itemView);
        this.textNama = itemView.findViewById(R.id.textViewNama);
        imageView = itemView.findViewById(R.id.imageView);
        imageDelete = itemView.findViewById(R.id.imageViewDelete);
    }

}
