package com.zam.sidik_padang.home.newsinfo.gallery.bukaalbum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import com.zam.sidik_padang.R;

public class GambarListAdapter extends RecyclerView.Adapter<GambarViewHolder> {
    private List<Gambar> list;
    private OnGambarClickListener listener;
    private LayoutInflater inflater;

    public GambarListAdapter(List<Gambar> list, OnGambarClickListener listener) {
        this.list = list;
        this.listener = listener;
    }


    @Override
    public GambarViewHolder onCreateViewHolder(ViewGroup p, int p2) {
        if (inflater == null) inflater = LayoutInflater.from(p.getContext());
        return new GambarViewHolder(inflater.inflate(R.layout.item_gambar_gallery, p, false));
    }

    @Override
    public void onBindViewHolder(final GambarViewHolder h, int position) {
        final Gambar gambar = list.get(position);
		/*h.textViewJudul.setText(gambar.jdl_gallery);
		if (gambar.keterangan.contains("<")) {
			Spanned keterangan = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Html.fromHtml(gambar.keterangan, 0) : Html.fromHtml(gambar.keterangan);
			h.textViewKeterangan.setText(keterangan);
		}else {
			h.textViewKeterangan.setText(gambar.keterangan);
		}
		*/
        h.textViewTanggal.setText(gambar.tanggal);
        h.imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                listener.onGambarClick(gambar);
            }
        });

        h.progressBar.setVisibility(View.VISIBLE);

        Glide.with(h.imageView.getContext()).load(gambar.gambar)
                .placeholder(R.drawable.ic_photo_library_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        h.progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        h.progressBar.setVisibility(View.INVISIBLE);

                        return false;
                    }
                })
                .into(h.imageView);
    }

    @Override
    public int getItemCount() {
        // TODO: Implement this method
        return list.size();
    }


    interface OnGambarClickListener {
        void onGambarClick(Gambar gambar);
    }
}
