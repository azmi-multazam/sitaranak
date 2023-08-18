package com.zam.sidik_padang.home.sklb.print.sertifikat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.sklb.dataternak.DataTernakViewHolder;
import com.zam.sidik_padang.home.sklb.print.sertifikat.vm.SertifikatTernakPetugas;

/**
 * Created by supriyadi on 5/14/17.
 */

public class SertifikatAdapter extends RecyclerView.Adapter<DataTernakViewHolder> {

    private List<SertifikatTernakPetugas> list;
    private OnDataItemClickListener listener;

    public SertifikatAdapter(OnDataItemClickListener listener) {
        this.list = new ArrayList<>();
        this.listener = listener;
    }

    public void setList(List<SertifikatTernakPetugas> list) {
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
        final SertifikatTernakPetugas dataTernak = list.get(pos);
        holder.textViewNama.setText(dataTernak.getIdTernak());
        holder.textViewJenis.setText(dataTernak.getJenis());
        holder.textViewBangsa.setText(dataTernak.getBangsa());
        holder.textViewKelamin.setText(dataTernak.getKelamin());
        holder.textViewUmur.setText(dataTernak.getUmur() + " Tahun " + dataTernak.getBulan() + " Bulan " + dataTernak.getHari() + " Hari");
        holder.itemView.setOnClickListener(v -> listener.OnDataItemClick(dataTernak.getId(), pos));
        holder.buttonDelete.setVisibility(View.INVISIBLE);
        holder.pita.setVisibility(dataTernak.isAdaSertifikat() ? View.VISIBLE : View.GONE);
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
        //holder.header.setBackgroundColor(Color.parseColor(dataTernak.getKondisiWarna()));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface OnDataItemClickListener {
        void OnDataItemClick(String id, int position);
        //void onItemDeleteButtonClickListener(int listPosition);
    }
}
