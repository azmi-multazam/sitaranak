package com.zam.sidik_padang.home.memberhome;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.memberhome.mvp.Berita;
import com.zam.sidik_padang.home.newsinfo.NewsInfoActivity;
import com.zam.sidik_padang.home.newsinfo.bukaberita.BukaBeritaActivity;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    List<Berita> listHome;

    public HomeAdapter(List<Berita> listHome) {
        this.listHome = listHome;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Berita list = listHome.get(position);
        //holder.image.setBackgroundResource(list.getImage());
        Glide.with(holder.image.getContext()).load(list.getGambar()).placeholder(R.drawable.ic_baseline_crop_original_24).into(holder.image);
        holder.title.setText(list.getJudul());
        holder.date.setText(list.getTanggal());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Berita berita = new Berita();
                //berita.setId(list.getId();
                //berita.setGambar(list.getGambar();
                //berita.judul = list.getJudul();
                //berita.tanggal = list.getTanggal();
                Intent intent = new Intent(view.getContext(), BukaBeritaActivity.class);
                intent.putExtra(NewsInfoActivity.EXTRA_BERITA, list.getId());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listHome.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView container;
        ImageView image;
        TextView title, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            image = itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.item_title);
            date = itemView.findViewById(R.id.item_date);
        }
    }
}
