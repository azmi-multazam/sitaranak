package com.zam.sidik_padang.home.ppob.downline;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 7/29/17.
 */

public class DownlineViewHolder extends RecyclerView.ViewHolder {

    TextView textViewId, textViewNama;

    public DownlineViewHolder(View itemView) {
        super(itemView);
        textViewId = (TextView) itemView.findViewById(R.id.item_downline_TextViewId);
        textViewNama = (TextView) itemView.findViewById(R.id.item_downline_TextViewNama);
    }
}
