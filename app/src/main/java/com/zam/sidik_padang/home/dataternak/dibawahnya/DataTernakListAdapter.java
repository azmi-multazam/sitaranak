package com.zam.sidik_padang.home.dataternak.dibawahnya;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 5/14/17.
 */

public class DataTernakListAdapter extends RecyclerView.Adapter<DataTernakViewHolder> {

    private List<DataTernak> list;
    private OnTernakItemClickListener listener;

    public DataTernakListAdapter(List<DataTernak> list, OnTernakItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public DataTernakViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DataTernakViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_ternak_dibawahnya, parent, false));
    }

    @Override
    public void onBindViewHolder(DataTernakViewHolder holder, int position) {
        final int pos = position;
        final DataTernak dataTernak = list.get(pos);
        holder.textViewNama.setText(dataTernak.id_ternak);
        holder.textViewJenis.setText(dataTernak.jenis);
        holder.textViewBangsa.setText(dataTernak.bangsa);
        holder.textViewKelamin.setText(dataTernak.kelamin);
        holder.textViewUmur.setText(dataTernak.umur + " Tahun " + dataTernak.bulan + " Bulan " + dataTernak.hari + " Hari");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnDataTernakItemClick(pos);
            }
        });
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemDeleteButtonClickListener(pos);
            }
        });

        holder.header.setBackgroundColor(Color.parseColor(dataTernak.kondisi_warna));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static interface OnTernakItemClickListener {
        void OnDataTernakItemClick(int position);

        void onItemDeleteButtonClickListener(int listPosotion);
    }
}
