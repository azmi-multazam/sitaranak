package com.zam.sidik_padang.home.newsinfo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 10/11/17.
 */

public class BeritaViewHolder extends RecyclerView.ViewHolder {
    TextView textJudul, textTanggal;
    ImageView imageView;
    View itemView;

    public BeritaViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        textJudul = (TextView) itemView.findViewById(R.id.item_news_info_TextViewJudul);
        textTanggal = (TextView) itemView.findViewById(R.id.item_news_info_TextViewTanggal);
        imageView = (ImageView) itemView.findViewById(R.id.item_news_info_ImageView);
    }
}
