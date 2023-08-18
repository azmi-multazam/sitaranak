package com.zam.sidik_padang.home.ppob.komisi;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;

import com.zam.sidik_padang.R;


/**
 * Created by supriyadi on 7/30/17.
 */

public class KomisiLisAdapter extends RecyclerView.Adapter<KomisiViewHolder> {
    private final List<Komisi> list;

    public KomisiLisAdapter(List<Komisi> list) {
        this.list = list;
    }

    @Override
    public KomisiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new KomisiViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_komisi_trx_downline, parent, false));
    }

    @Override
    public void onBindViewHolder(KomisiViewHolder holder, int position) {
        Komisi komisi = list.get(position);
        String tanggl = komisi.tanggal;
        if (tanggl.contains(" ")) tanggl = tanggl.split(" ")[0];
        holder.textTanggal.setText(tanggl);
        holder.textViewDAri.setText(komisi.dari);
        holder.textViewKeteranan.setText(komisi.keterangan);
        holder.textViewTotal.setText(String.format("Rp. %s", NumberFormat.getInstance().format(komisi.total)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
