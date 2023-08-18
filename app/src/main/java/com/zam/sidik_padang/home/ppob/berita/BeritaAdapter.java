package com.zam.sidik_padang.home.ppob.berita;

import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.util.Config;
import com.zam.sidik_padang.util.Util;
import com.zam.sidik_padang.util.VolleySingleton;
import com.zam.sidik_padang.util.VolleyStringRequest;

/**
 * Created by supriyadi on 8/11/17.
 */

public class BeritaAdapter extends RecyclerView.Adapter<BeritaViewHolder> {
    private final List<Berita> list;
    private int colorAccent = 0;
    private final OnBeritaItemClickListener listener;

    public BeritaAdapter(List<Berita> list, OnBeritaItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public BeritaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        colorAccent = ResourcesCompat.getColor(parent.getResources(), R.color.primaryLight, null);
        return new BeritaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_berita, parent, false));
    }

    @Override
    public void onBindViewHolder(BeritaViewHolder holder, int position) {
        final int p = position;
        final Berita berita = list.get(position);
        holder.textHits.setText(berita.hits + "X");
        holder.textTanggal.setText(berita.tanggal);
        holder.textTitle.setText(berita.title);
        holder.layoutHeader.setBackgroundColor(berita.collapsed ? Color.WHITE : colorAccent);
        holder.textIsi.setText(Html.fromHtml(berita.isi, 0));
        holder.textIsi.setVisibility(berita.collapsed ? View.GONE : View.VISIBLE);
        holder.view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                listener.onBeritaItemClick(p);
                if (!berita.collapsed && Util.isInternetAvailible(p1.getContext())) {
                    String url = Config.URL_BERITA_PPOB + "aksi=2&id=" + berita.id;
                    VolleySingleton.getInstance(p1.getContext()).getRequestQueue()
                            .add(new VolleyStringRequest(url,
                                    new VolleyStringRequest.Callback() {
                                        @Override
                                        public void onResponse(JsonObject jsonObject) {
                                            // Log.e(getClass().getName(),"response: "+jsonObject);
                                        }
                                    }));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    interface OnBeritaItemClickListener {
        void onBeritaItemClick(int p);


    }

}
