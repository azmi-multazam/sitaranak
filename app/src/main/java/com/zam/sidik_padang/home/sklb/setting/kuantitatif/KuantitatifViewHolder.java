package com.zam.sidik_padang.home.sklb.setting.kuantitatif;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 5/14/17.
 */

public class KuantitatifViewHolder extends RecyclerView.ViewHolder {
    public ImageView edit;
    public TextView umur;

    public KuantitatifViewHolder(View itemView) {
        super(itemView);
        umur = itemView.findViewById(R.id.umur);
        edit = itemView.findViewById(R.id.edit);
    }
}
