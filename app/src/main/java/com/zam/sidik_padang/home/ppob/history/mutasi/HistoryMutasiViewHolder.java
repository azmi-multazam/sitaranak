package com.zam.sidik_padang.home.ppob.history.mutasi;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

public class HistoryMutasiViewHolder extends RecyclerView.ViewHolder {
    View v;
    TextView textTanggal, textKredit, textDebit, textKode;

    HistoryMutasiViewHolder(View v) {
        super(v);
        this.v = v;
        textTanggal = v.findViewById(R.id.itemhistorymutasiTextViewTanggal);
        textKredit = v.findViewById(R.id.itemhistorymutasiTextViewKredit);
        textDebit = v.findViewById(R.id.itemhistorymutasiTextViewDebet);
        textKode = v.findViewById(R.id.itemhistorymutasiTextViewKode);
    }
}
