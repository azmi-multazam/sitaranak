package com.zam.sidik_padang.home.newsinfo.bukaberita.komentar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 10/12/17.
 */

public class KomentarViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textUserId, textTanggal, textisiKomentar;

    public KomentarViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.item_komentar_ImageViewFoto);
        textUserId = (TextView) itemView.findViewById(R.id.item_komentar_TextViewUserId);
        textTanggal = (TextView) itemView.findViewById(R.id.item_komentar_TextViewTanggal);
        textisiKomentar = (TextView) itemView.findViewById(R.id.item_komentar_TextViewIsiKomentar);
    }
}
