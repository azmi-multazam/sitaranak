package com.zam.sidik_padang.home.dataternak.detailternak.riwayat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 11/1/17.
 */

public class RiwayatListAdapter extends RecyclerView.Adapter<RiwayatViewHolder> {

    private List<Riwayat> list;

    public RiwayatListAdapter(List<Riwayat> list) {
        this.list = list;
    }

    @Override
    public RiwayatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RiwayatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_riwayat, parent, false));
    }

    @Override
    public void onBindViewHolder(RiwayatViewHolder holder, int position) {
        Riwayat r = list.get(position);
        holder.textTanggal.setText(r.tanggal);
        holder.textKondisi.setText(r.kondisi);
        holder.textKeterangan.setText(r.keterangan);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
