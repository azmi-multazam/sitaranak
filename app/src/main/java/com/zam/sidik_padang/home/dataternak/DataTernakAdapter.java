package com.zam.sidik_padang.home.dataternak;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.dataternak.dibawahnya.DataTernakViewHolder;
import com.zam.sidik_padang.home.dataternak.vm.list.DataTernak;

/**
 * Created by supriyadi on 5/14/17.
 */

public class DataTernakAdapter extends RecyclerView.Adapter<DataTernakViewHolder> {

    private List<DataTernak> list;
    private OnTernakItemClickListener listener;

    public DataTernakAdapter(OnTernakItemClickListener listener) {
        this.list = new ArrayList<>();
        this.listener = listener;
    }

    public void setList(List<DataTernak> list) {
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
        final DataTernak dataTernak = list.get(pos);
        holder.header.setBackgroundColor(Color.parseColor(dataTernak.getKondisiWarna()));
        holder.textViewNama.setText(dataTernak.getIdTernak());
        holder.textViewJenis.setText(dataTernak.getJenis());
        holder.textViewBangsa.setText(dataTernak.getBangsa());
        holder.textViewKelamin.setText(dataTernak.getKelamin());
        String umur  = dataTernak.getUmur() + " Tahun " + dataTernak.getBulan() + " Bulan " + dataTernak.getHari() + " Hari";
        holder.textViewUmur.setText(umur);
        holder.itemView.setOnClickListener(v ->
                listener.OnDataTernakItemClick(dataTernak.getIdTernak(), pos));
        holder.buttonDelete.setOnClickListener(v ->
                listener.onItemDeleteButtonClickListener(dataTernak.getIdTernak(),
                        dataTernak.getNama(), pos));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void clearList() {
        if (list.size() > 0) list.clear();
        notifyDataSetChanged();
    }

    public void addToList(List<DataTernak> listTernak) {
        final int positionStart = list.size();
        list.addAll(listTernak);
        notifyItemRangeInserted(positionStart, listTernak.size());
    }

    public void remove(int listPos) {
        list.remove(listPos);
        notifyDataSetChanged();
    }
}