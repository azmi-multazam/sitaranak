package com.zam.sidik_padang.home.ppob.iklanmerchant.komentar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 10/12/17.
 */

public class KomentarListAdapter extends RecyclerView.Adapter<KomentarViewHolder> {
    private List<Komentar> list;

    public KomentarListAdapter(List<Komentar> list) {
        this.list = list;
    }

    @Override
    public KomentarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_komentar, parent, false);
        return new KomentarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(KomentarViewHolder holder, int position) {
        Komentar k = list.get(position);
        holder.textUserId.setText(k.iduser + "/" + k.nama);
        holder.textTanggal.setText(k.tanggal);
        holder.textisiKomentar.setText(k.komentar);
        if (k.foto != null && !k.foto.isEmpty()) {
            Glide.with(holder.imageView.getContext()).load(k.foto).placeholder(R.drawable.ic_person).into(holder.imageView);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
