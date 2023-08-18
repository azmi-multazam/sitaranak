package com.zam.sidik_padang.home.ppob.berita;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;


/**
 * Created by supriyadi on 8/11/17.
 */

public class BeritaViewHolder extends RecyclerView.ViewHolder {
    TextView textTanggal, textTitle, textIsi, textHits;
    View layoutHeader;
    View view;

    BeritaViewHolder(View view) {
        super(view);
        this.view = view;
        textTanggal = view.findViewById(R.id.item_berita_TextViewTanggal);
        textTitle = view.findViewById(R.id.item_berita_TextViewTitle);
        textIsi = view.findViewById(R.id.item_berita_TextViewIsi);
        textHits = view.findViewById(R.id.item_berita_TextViewHits);
        layoutHeader = view.findViewById(R.id.item_berita_LinearLayoutHeader);
    }
}
