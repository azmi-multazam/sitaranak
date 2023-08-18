package com.zam.sidik_padang.home.dataternak.detailternak.riwayat;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 11/1/17.
 */

public class RiwayatViewHolder extends RecyclerView.ViewHolder {

    TextView textTanggal, textKondisi, textKeterangan;

    public RiwayatViewHolder(View itemView) {
        super(itemView);
        textTanggal = (TextView) itemView.findViewById(R.id.item_riwayat_textViewTanggal);
        textKondisi = (TextView) itemView.findViewById(R.id.item_riwayat_textViewKondisi);
        textKeterangan = (TextView) itemView.findViewById(R.id.item_riwayat_textViewKeterangan);
    }
}
