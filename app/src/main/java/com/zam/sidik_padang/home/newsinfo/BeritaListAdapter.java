package com.zam.sidik_padang.home.newsinfo;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.memberhome.mvp.Berita;
import com.zam.sidik_padang.home.newsinfo.bukaberita.BukaBeritaActivity;

/**
 * Created by supriyadi on 10/11/17.
 */

public class BeritaListAdapter extends RecyclerView.Adapter<BeritaViewHolder> {

    private List<Berita> list;

    public BeritaListAdapter(List<Berita> list) {
        this.list = list;
    }


    @Override
    public BeritaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BeritaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_info, parent, false));
    }

    @Override
    public void onBindViewHolder(BeritaViewHolder holder, int position) {
        final Berita b = list.get(position);
        holder.textJudul.setText(b.getJudul());
        holder.textTanggal.setText(b.getTanggal());
//		Log.e(getClass().getName(), "Gambar=" + b.gambar);
//		if (b.gambar != null && !b.gambar.isEmpty())
        Glide.with(holder.imageView.getContext()).load(b.getGambar()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BukaBeritaActivity.class);
                intent.putExtra(NewsInfoActivity.EXTRA_BERITA, b.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
