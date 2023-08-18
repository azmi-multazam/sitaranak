package com.zam.sidik_padang.home.ppob.komisi;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;


/**
 * Created by supriyadi on 7/30/17.
 */

public class KomisiViewHolder extends RecyclerView.ViewHolder {
    TextView textTanggal, textViewDAri, textViewTotal, textViewKeteranan;

    public KomisiViewHolder(View itemView) {
        super(itemView);
        textTanggal = itemView.findViewById(R.id.item_komisi_trx_downline_TextViewTanggal);
        textViewDAri = itemView.findViewById(R.id.item_komisi_trx_downline_TextViewDari);
        textViewTotal = itemView.findViewById(R.id.item_komisi_trx_downline_TextViewTotal);
        textViewKeteranan = itemView.findViewById(R.id.item_komisi_trx_downline_TextViewKeterangan);
    }
}
