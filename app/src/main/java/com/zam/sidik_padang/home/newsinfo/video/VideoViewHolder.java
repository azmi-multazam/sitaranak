package com.zam.sidik_padang.home.newsinfo.video;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;


public class VideoViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewJudul, textViewTanggal;
    public ImageView thumbnailView;
    public View progressBar;

    public VideoViewHolder(View v) {
        super(v);
        textViewJudul = (TextView) v.findViewById(R.id.textViewJudul);
        textViewTanggal = (TextView) v.findViewById(R.id.textViewTanggal);
        thumbnailView = (ImageView) v.findViewById(R.id.thumbnail);
        progressBar = v.findViewById(R.id.progressBar);
    }
}
