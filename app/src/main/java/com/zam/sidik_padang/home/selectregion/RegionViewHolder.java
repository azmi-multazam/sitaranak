package com.zam.sidik_padang.home.selectregion;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RegionViewHolder extends RecyclerView.ViewHolder {
    public TextView tv;

    public RegionViewHolder(View v) {
        super(v);
        this.tv = (TextView) v;
    }
}
