package com.zam.sidik_padang.home.dataternak.detailternak.foto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import com.zam.sidik_padang.R;


class FotoTernakRecyclerViewAdapter extends RecyclerView.Adapter<FotoTernakRecyclerViewAdapter.ViewHolder> {

    private final List<FotoTernak> list;
    private OnFotoItemClickListener listener;

    public FotoTernakRecyclerViewAdapter(List<FotoTernak> list, OnFotoItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fotot_ernak, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = list.get(position);
        holder.textTanggal.setText(holder.mItem.tanggal);
        Glide.with(holder.imageView.getContext())
                .load(holder.mItem.gambar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFotoItemClick(v, holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static interface OnFotoItemClickListener {
        void onFotoItemClick(View fotoImageView, FotoTernak fotoTernak);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        FotoTernak mItem;
        TextView textTanggal;

        ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.item_foto_ternak_ImageView);
            textTanggal = (TextView) view.findViewById(R.id.item_foto_ternak_TextViewTanggal);
        }

    }
}
