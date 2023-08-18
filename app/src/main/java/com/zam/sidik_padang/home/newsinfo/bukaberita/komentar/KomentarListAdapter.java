package com.zam.sidik_padang.home.newsinfo.bukaberita.komentar;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 10/12/17.
 */

public class KomentarListAdapter extends RecyclerView.Adapter<KomentarViewHolder> {
    private List<KomentarBerita> list;

    public KomentarListAdapter(List<KomentarBerita> list) {
        this.list = list;
    }

    @Override
    public KomentarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new KomentarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_komentar, parent, false));
    }

    @Override
    public void onBindViewHolder(KomentarViewHolder holder, int position) {
        KomentarBerita k = list.get(position);
        holder.textUserId.setText(k.getIduser() + "/" + k.getNama());
        holder.textTanggal.setText(k.getTanggal());
        holder.textisiKomentar.setText(k.getKomentar());
        if (k.getFoto() != null && !k.getFoto().isEmpty()) {
            Glide.with(holder.imageView.getContext()).load(k.getFoto()).placeholder(R.drawable.ic_person).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
