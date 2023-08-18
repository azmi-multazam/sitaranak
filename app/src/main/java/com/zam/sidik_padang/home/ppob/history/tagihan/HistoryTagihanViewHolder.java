package com.zam.sidik_padang.home.ppob.history.tagihan;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;


/**
 * Created by supriyadi on 8/20/17.
 */

public class HistoryTagihanViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewTitle, textViewNama, textViewTotal;
    Button button;

    HistoryTagihanViewHolder(View view) {
        super(view);
        textViewTitle = (TextView) view.findViewById(R.id.item_history_tagihan_TextViewTitle);
        textViewNama = (TextView) view.findViewById(R.id.item_history_tagihan_TextViewAtasNama);
        textViewTotal = (TextView) view.findViewById(R.id.item_history_tagihan_TextViewTotalTagihan);
        button = (Button) view.findViewById(R.id.item_history_tagihan_Button);
    }
}
