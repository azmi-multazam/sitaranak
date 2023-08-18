package com.zam.sidik_padang.home.ppob.history.pulsa;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.zam.sidik_padang.R;

public class HistoryPulsaListAdapter extends RecyclerView.Adapter<HistoryPulsaViewHolder> {

    private final List<HistoryPulsa> list;
    private int colorGreySoft, colorGrey;

    public HistoryPulsaListAdapter(List<HistoryPulsa> list) {
        this.list = list;
    }


    @Override
    public HistoryPulsaViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
        // TODO: Implement this method
        colorGreySoft = ResourcesCompat.getColor(p1.getResources(), R.color.colorGreySoft, null);
        colorGrey = ResourcesCompat.getColor(p1.getResources(), R.color.colorGrey, null);
        return new HistoryPulsaViewHolder(LayoutInflater.from(p1.getContext()).inflate(R.layout.item_history_pulsa, p1, false));
    }

    @Override
    public void onBindViewHolder(HistoryPulsaViewHolder p1, int p2) {
        HistoryPulsa hp = list.get(p2);
        p1.v.setBackgroundColor(p2 % 2 == 0 ? colorGreySoft : Color.WHITE);
        p1.textStatus.setTextColor(hp.status.equalsIgnoreCase("gagal") ? Color.RED : colorGrey);

        p1.textStatus.setText(hp.status);
        p1.textProduk.setText(hp.produk);
    }

    @Override
    public int getItemCount() {
        // TODO: Implement this method
        return list.size();
    }

}
