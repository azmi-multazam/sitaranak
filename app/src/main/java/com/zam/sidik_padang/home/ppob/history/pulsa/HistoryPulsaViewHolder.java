package com.zam.sidik_padang.home.ppob.history.pulsa;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;


public class HistoryPulsaViewHolder extends RecyclerView.ViewHolder {
    public View v;
    TextView textProduk, textStatus;

    public HistoryPulsaViewHolder(View v) {
        super(v);
        this.v = v;
        textProduk = v.findViewById(R.id.item_history_pulsa_TextViewProduk);
        textStatus = v.findViewById(R.id.item_history_pulsa_TextViewStatus);
    }
}
