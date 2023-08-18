package com.zam.sidik_padang.home.dataternak.insiminator;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 4/17/18.
 */

public class TernakIbViewHolder extends RecyclerView.ViewHolder {

    TextView textIdTernak, textJenis, textRupun, textPemilik, textTanggalIb, textUmur;
    View buttonDelete, header;

    public TernakIbViewHolder(View v) {
        super(v);
        header = v.findViewById(R.id.viewHeader);
        buttonDelete = v.findViewById(R.id.butttonDelete);
        textIdTernak = (TextView) v.findViewById(R.id.textViewIdTernak);
        textJenis = (TextView) v.findViewById(R.id.textViewJenis);
        textRupun = (TextView) v.findViewById(R.id.textViewRumpun);
        textPemilik = (TextView) v.findViewById(R.id.textViewPemilik);
        textTanggalIb = (TextView) v.findViewById(R.id.textViewTanggalIb);
        textUmur = (TextView) v.findViewById(R.id.item_data_ternak_ib_TextViewUmur);
    }
}
