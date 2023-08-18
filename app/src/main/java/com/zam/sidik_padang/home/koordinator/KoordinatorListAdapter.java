package com.zam.sidik_padang.home.koordinator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.zam.sidik_padang.R;

public class KoordinatorListAdapter extends RecyclerView.Adapter<KoordinatorViewHolder> {
    private List<Koordinator> list;
    private OnKoordinatorDeleteButtonClickListener listener;

    public KoordinatorListAdapter(List<Koordinator> list, OnKoordinatorDeleteButtonClickListener listener) {
        this.list = list;
        this.listener = listener;
    }


    @Override
    public KoordinatorViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
        // TODO: Implement this method
        return new KoordinatorViewHolder(LayoutInflater.from(p1.getContext()).inflate(R.layout.item_koordinator, p1, false));
    }

    @Override
    public void onBindViewHolder(KoordinatorViewHolder h, int p2) {
        final int position = p2;
        Koordinator koordinator = list.get(position);
        h.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onKoordinatorDeleteButtonClick(position);
            }
        });

        h.textViewNama.setText(koordinator.nama);
        h.textViewNAmaKelompok.setText(koordinator.nama_kelompok);
        h.textViewAlamatKelompok.setText(koordinator.alamat_kelompok);
        h.textViewProvinsi.setText(koordinator.Provinsi);
    }

    @Override
    public int getItemCount() {
        // TODO: Implement this method
        return list.size();
    }

    public static interface OnKoordinatorDeleteButtonClickListener {
        void onKoordinatorDeleteButtonClick(int position);
    }

}
