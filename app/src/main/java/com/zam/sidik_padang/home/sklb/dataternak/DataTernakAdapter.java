package com.zam.sidik_padang.home.sklb.dataternak;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.sklb.dataternak.vm.DataTernakPetugas;

/**
 * Created by supriyadi on 5/14/17.
 */

public class DataTernakAdapter extends RecyclerView.Adapter<DataTernakViewHolder> {

    private List<DataTernakPetugas> list;
    private OnTernakItemClickListener listener;

    public DataTernakAdapter(OnTernakItemClickListener listener) {
        this.list = new ArrayList<>();
        this.listener = listener;
    }

    public void setList(List<DataTernakPetugas> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public DataTernakViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DataTernakViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_ternak_dibawahnya, parent, false));
    }

    @Override
    public void onBindViewHolder(DataTernakViewHolder holder, int position) {
        final int pos = position;
        final DataTernakPetugas dataTernak = list.get(pos);
        holder.textViewNama.setText(dataTernak.getIdTernak());
        holder.textViewJenis.setText(dataTernak.getJenis());
        holder.textViewBangsa.setText(dataTernak.getBangsa());
        holder.textViewKelamin.setText(dataTernak.getKelamin());
        holder.textViewUmur.setText(dataTernak.getUmur() + " Tahun " + dataTernak.getBulan() + " Bulan " + dataTernak.getHari() + " Hari");
        holder.itemView.setOnClickListener(v -> listener.OnDataTernakItemClick(pos));
        holder.buttonDelete.setVisibility(View.INVISIBLE);
        //holder.buttonDelete.setOnClickListener(v -> listener.onItemDeleteButtonClickListener(pos));
        if (dataTernak.getNamaPetugas() != null) {
            holder.rowPetugas.setVisibility(View.VISIBLE);
            holder.textViewPetugas.setText(dataTernak.getNamaPetugas());
        } else {
            holder.rowPetugas.setVisibility(View.GONE);
        }

        if (dataTernak.getNamaPemilik() != null) {
            holder.rowPemilik.setVisibility(View.VISIBLE);
            holder.textViewPemilik.setText(dataTernak.getNamaPemilik());
        } else {
            holder.rowPemilik.setVisibility(View.GONE);
        }

        holder.header.setBackgroundColor(Color.parseColor(dataTernak.getKondisiWarna()));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface OnTernakItemClickListener {
        void OnDataTernakItemClick(int position);

        void onItemDeleteButtonClickListener(int listPosition);
    }
}
