package com.zam.sidik_padang.home.newsinfo.gallery;

import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
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
import com.zam.sidik_padang.home.newsinfo.gallery.bukaalbum.BukaAlbumActivity;
import com.zam.sidik_padang.home.newsinfo.video.VideoViewHolder;


public class AlbumListAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private LayoutInflater inflater;
    private List<Album> list;

    public AlbumListAdapter(List<Album> list) {
        this.list = list;
    }


    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
        if (inflater == null) inflater = LayoutInflater.from(p1.getContext());
        return new VideoViewHolder(inflater.inflate(R.layout.item_video, p1, false));
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder h, int p2) {
        final Album album = list.get(p2);
        h.textViewJudul.setText(album.jumlah_foto + " foto");
        if (album.keterangan.contains("<")) {
            Spanned keterangan = Html.fromHtml(album.keterangan, 0);
            h.textViewTanggal.setText(keterangan);
        } else {
            h.textViewTanggal.setText(album.keterangan);
        }
        ((View) h.textViewJudul.getParent()).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                Intent it = new Intent(p1.getContext(), BukaAlbumActivity.class);
                it.putExtra(BukaAlbumActivity.EXTRA_ALBUM, album);
                p1.getContext().startActivity(it);
            }
        });
        //Glide.with(p1.thumbnailView.getContext()).load("https://img.youtube.com/vi/"+"hkzpLJjZQbA"+"/0.jpg").into(p1.thumbnailView);
        h.progressBar.setVisibility(View.VISIBLE);
        Glide.with(h.thumbnailView.getContext()).load(album.gbr_album)
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
                .into(h.thumbnailView);
    }

    @Override
    public int getItemCount() {
        // TODO: Implement this method
        return list.size();
    }


}
