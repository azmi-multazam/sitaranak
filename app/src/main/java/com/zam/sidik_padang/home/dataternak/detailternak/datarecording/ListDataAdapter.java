package com.zam.sidik_padang.home.dataternak.detailternak.datarecording;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 9/11/17.
 */

public class ListDataAdapter extends RecyclerView.Adapter<DataRecordingItemViewHolder> {

    private List<DataRecordingItem> list;
    private OnRecordingItemClickListener listener;

    public ListDataAdapter(List<DataRecordingItem> list, OnRecordingItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public DataRecordingItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DataRecordingItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_recording_ternak, parent, false));
    }

    @Override
    public void onBindViewHolder(DataRecordingItemViewHolder holder, int position) {
        final DataRecordingItem data = list.get(position);
        holder.textViewTanggal.setText(data.tanggal);
        holder.textViewBeratBadan.setText(data.berat_badan);
        holder.textViewPanjangBadan.setText(data.panjang_badan);
        holder.textViewLingkarDada.setText(data.lingkar_dada);
        holder.textViewDalamDada.setText(data.dalam_dada);
        holder.textViewTinggiBadan.setText(data.tinggi_badan);
        holder.textViewPbbh.setText(data.pbbh);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecordingItemClick(data);
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRecordingItemDeleteButtonClick(data);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static interface OnRecordingItemClickListener {
        void onRecordingItemClick(DataRecordingItem item);

        void onRecordingItemDeleteButtonClick(DataRecordingItem item);
    }
}
