package com.zam.sidik_padang.home.ppob.history.game;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class HistoryGameViewHolder extends RecyclerView.ViewHolder {
    TextView tv;

    HistoryGameViewHolder(View v) {
        super(v);
        tv = (TextView) v;
    }
}
