package com.zam.sidik_padang.home.koordinator;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

public class KoordinatorViewHolder extends RecyclerView.ViewHolder {
    public View buttonDelete;
    public TextView textViewNama, textViewNAmaKelompok, textViewAlamatKelompok, textViewProvinsi;

    public KoordinatorViewHolder(View v) {
        super(v);
        buttonDelete = v.findViewById(R.id.item_koordinator_ButttonDelete);
        textViewNama = (TextView) v.findViewById(R.id.item_koordinator_TextViewNama);
        textViewNAmaKelompok = (TextView) v.findViewById(R.id.item_koordinator_TextViewNamaKelompok);
        textViewAlamatKelompok = (TextView) v.findViewById(R.id.item_koordinator_TextViewAlamatKelompok);
        textViewProvinsi = (TextView) v.findViewById(R.id.item_koordinator_TextViewProvinsi);
    }
}
