package com.zam.sidik_padang.home.kelompokternak;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 5/7/17.
 */

public class KelompokTernakListAdapter extends RecyclerView.Adapter<KelompokTernakViewHolder> {
    private List<KelompokTernak> list;
    private OnDeleteButtonClickListener listener;

    public KelompokTernakListAdapter(List<KelompokTernak> list, OnDeleteButtonClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public KelompokTernakViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new KelompokTernakViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kelompok_ternak, parent, false));
    }

    @Override
    public void onBindViewHolder(KelompokTernakViewHolder holder, int position) {
        final int pos = position;
        KelompokTernak kelompokTernak = list.get(pos);
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteItemClick(pos);
            }
        });

        holder.textViewNama.setText(kelompokTernak.nama_kelompok);
        holder.textViewAlamat.setText(kelompokTernak.alamat_kelompok);
        holder.textViewAlamatKorwil.setText(kelompokTernak.alamat_korwil);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static interface OnDeleteButtonClickListener {
        void onDeleteItemClick(int position);
    }
}
