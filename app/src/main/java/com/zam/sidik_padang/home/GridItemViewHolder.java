package com.zam.sidik_padang.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 5/5/17.
 */

public class GridItemViewHolder extends RecyclerView.ViewHolder {
    public View view;
    public ImageView imageView;
    public TextView textView;

    public GridItemViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        this.imageView = (ImageView) itemView.findViewById(R.id.item_member_area_grid_ImageView);
        this.textView = (TextView) itemView.findViewById(R.id.item_member_area_grid_TextView);

    }
}
