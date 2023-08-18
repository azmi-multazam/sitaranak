package com.zam.sidik_padang.home.newsinfo.gallery.bukaalbum;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;


public class GambarViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textViewTanggal;//, textViewKeterangan;
    View progressBar;

    GambarViewHolder(View v) {
        super(v);
        imageView = (ImageView) v.findViewById(R.id.imageView);
        textViewTanggal = (TextView) v.findViewById(R.id.textView);
        //textViewKeterangan=(TextView)v.findViewById(R.id.textViewKeterangan);
        progressBar = v.findViewById(R.id.progressBar);
    }
}
