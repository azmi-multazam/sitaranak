package com.zam.sidik_padang.home.dataternak.insiminator;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 4/17/18.
 */

public class TernakIbListAdapter extends RecyclerView.Adapter<TernakIbViewHolder> {

    private List<TernakIb> list;
    private OnListItemClickListener listener;

    public TernakIbListAdapter(List<TernakIb> list, OnListItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public TernakIbViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TernakIbViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ternak_ib, parent, false));
    }

    @Override
    public void onBindViewHolder(TernakIbViewHolder holder, int position) {
        final int p = position;
        final TernakIb ternakIb = list.get(position);
        holder.textIdTernak.setText(ternakIb.id_ternak);
        holder.textJenis.setText(ternakIb.jenis);
        holder.textPemilik.setText(ternakIb.nama);
        holder.textRupun.setText(ternakIb.bangsa);
        holder.textTanggalIb.setText(ternakIb.tanggal_ib);
        holder.textUmur.setText(ternakIb.umur + " Tahun " + ternakIb.bulan + " Bulan " + ternakIb.hari + " Hari");
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDeleteButtonClick(ternakIb, p);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onListItemClick(ternakIb, p);
            }
        });
        holder.header.setBackgroundColor(Color.parseColor(ternakIb.kondisi_warna));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static interface OnListItemClickListener {
        void onListItemClick(TernakIb ternakIb, int position);

        void onDeleteButtonClick(TernakIb ternakIb, int position);
    }
}
