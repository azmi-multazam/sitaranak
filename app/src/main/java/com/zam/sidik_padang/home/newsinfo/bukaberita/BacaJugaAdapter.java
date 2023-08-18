package com.zam.sidik_padang.home.newsinfo.bukaberita;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.memberhome.mvp.Berita;
import com.zam.sidik_padang.home.newsinfo.BeritaListAdapter;
import com.zam.sidik_padang.home.newsinfo.BeritaViewHolder;

/**
 * Created by supriyadi on 10/12/17.
 */

public class BacaJugaAdapter extends BeritaListAdapter {
    public BacaJugaAdapter(List<Berita> list) {
        super(list);
    }

    @Override
    public BeritaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BeritaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_info_baca_juga, parent, false));
    }
}
