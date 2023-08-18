package com.zam.sidik_padang.home.ppob.history.mutasi;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.zam.sidik_padang.R;


public class HistoryMutasiAdapter extends RecyclerView.Adapter<HistoryMutasiViewHolder> {
    private final List<HistoryMutasi> list;
    private int colorGreySoft = 0;
    private final NumberFormat formater;

    public HistoryMutasiAdapter(List<HistoryMutasi> list) {
        this.list = list;
        formater = NumberFormat.getInstance(Locale.getDefault());
    }

    @Override
    public HistoryMutasiViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
        if (colorGreySoft == 0)
            colorGreySoft = ResourcesCompat.getColor(p1.getResources(), R.color.colorGreySoft, null);
        return new HistoryMutasiViewHolder(LayoutInflater.from(p1.getContext()).inflate(R.layout.item_history_mutasi, p1, false));
    }

    @Override
    public void onBindViewHolder(HistoryMutasiViewHolder h, int p) {
        h.v.setBackgroundColor(p % 2 == 0 ? colorGreySoft : Color.WHITE);
        HistoryMutasi history = list.get(p);
        h.textDebit.setText(formater.format(history.debet));
        h.textKredit.setText(formater.format(history.kredit));
        h.textTanggal.setText(history.tanggal);
        h.textKode.setText(history.kode);
    }

    @Override
    public int getItemCount() {
        // TODO: Implement this method
        return list.size();
    }

}
