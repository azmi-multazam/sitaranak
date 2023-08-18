package com.zam.sidik_padang.home.sklb.setting.kuantitatif;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.sklb.setting.skor.ScoreEntity;

/**
 * Created by supriyadi on 5/14/17.
 */

public class KuantitatifAdapter extends RecyclerView.Adapter<KuantitatifViewHolder> {

    private List<ScoreEntity> list;
    private OnDataItemClickListener listener;
    private boolean isJantan;

    public KuantitatifAdapter(List<ScoreEntity> list, boolean isJantan, OnDataItemClickListener listener) {
        this.list = list;
        this.isJantan = isJantan;
        this.listener = listener;
    }

    public void setList(List<ScoreEntity> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public KuantitatifViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new KuantitatifViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kuantitatif, parent, false));
    }

    private String handleNol(int angka) {
        if (angka == 0) return "";
        else return angka + " ≤ ";
    }

    private String handleNol2(int angka) {
        if (angka == 0) return "";
        else return String.valueOf(angka);
    }

    @Override
    public void onBindViewHolder(@NotNull KuantitatifViewHolder holder, int position) {
        final ScoreEntity data = list.get(position);
        String umur = data.getBulanMax() == 0 && data.getBulanMin() == 0 ?
                handleNol(data.getHariMin()) + handleNol2(data.getHariMax()) + " hari" :
                handleNol(data.getBulanMin()) + handleNol2(data.getBulanMax()) + " bulan";

        holder.umur.setText(umur);
        holder.umur.setOnClickListener(v -> listener.OnDataItemClick(isJantan, position, umur));
        holder.edit.setOnClickListener(v -> listener.OnEditClick(isJantan, position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface OnDataItemClickListener {
        void OnDataItemClick(boolean isJantan, int position, String title);

        void OnEditClick(boolean isJantan, int position);
    }
}
