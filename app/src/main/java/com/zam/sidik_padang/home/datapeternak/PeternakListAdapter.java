package com.zam.sidik_padang.home.datapeternak;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.koordinator.KoordinatorListAdapter;
import com.zam.sidik_padang.home.koordinator.KoordinatorViewHolder;

/**
 * Created by supriyadi on 5/10/17.
 */

public class PeternakListAdapter extends RecyclerView.Adapter<KoordinatorViewHolder> {

    private List<Peternak> list;
    private KoordinatorListAdapter.OnKoordinatorDeleteButtonClickListener listener;

    public PeternakListAdapter(List<Peternak> list, KoordinatorListAdapter.OnKoordinatorDeleteButtonClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public KoordinatorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new KoordinatorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_koordinator, parent, false));
    }

    @Override
    public void onBindViewHolder(KoordinatorViewHolder h, int itemPosition) {
        final int pos = itemPosition;
        Peternak p = list.get(pos);
        h.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onKoordinatorDeleteButtonClick(pos);
            }
        });

        h.textViewNama.setText(p.nama + " (" + p.kelamin + ")");
        h.textViewNAmaKelompok.setText(p.nama_kelompok);
        h.textViewAlamatKelompok.setText(p.alamat_kelompok);
        h.textViewProvinsi.setText(p.Provinsi);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
