package com.zam.sidik_padang.roodiskusi;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

public class GroupViewHolder extends RecyclerView.ViewHolder {

    TextView textName, textLastMessage, textUnread, textTanggal;
    View viewSelectionIndicator;
    ImageView imageFoto;


    public GroupViewHolder(View itemView) {
        super(itemView);
        textName = (TextView) itemView.findViewById(R.id.textViewNama);
        textLastMessage = (TextView) itemView.findViewById(R.id.textViewPesan);
        textUnread = (TextView) itemView.findViewById(R.id.textUnread);
        textTanggal = itemView.findViewById(R.id.textViewTanggal);
        viewSelectionIndicator = itemView.findViewById(R.id.viewSelectionIndicator);
        imageFoto = itemView.findViewById(R.id.imageView);
    }

}
