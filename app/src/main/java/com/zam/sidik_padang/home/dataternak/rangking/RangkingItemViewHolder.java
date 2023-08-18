package com.zam.sidik_padang.home.dataternak.rangking;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 9/10/17.
 */

public class RangkingItemViewHolder extends RecyclerView.ViewHolder {
    TextView textViewIDTernak, textViewBeratLahir;
    View view;

    public RangkingItemViewHolder(View view) {
        super(view);
        this.view = view;
        textViewIDTernak = (TextView) view.findViewById(R.id.item_rangking_TextViewIdTernak);
        textViewBeratLahir = (TextView) view.findViewById(R.id.item_rangking_TextViewBeratLahir);
    }
}
