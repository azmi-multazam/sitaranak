package com.zam.sidik_padang.home.ppob.iklanmerchant.iklanpremium;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.newsinfo.gallery.bukaalbum.BukaGambarDialog;

public class IklanPremiumListAdapter extends RecyclerView.Adapter<IklanPremiumViewHolder> {

    private List<IklanPremium> list;
    private LayoutInflater layoutInflater;

    public IklanPremiumListAdapter(List<IklanPremium> list) {
        this.list = list;
    }


    @Override
    public IklanPremiumViewHolder onCreateViewHolder(ViewGroup p, int p2) {
        if (layoutInflater == null) layoutInflater = LayoutInflater.from(p.getContext());
        return new IklanPremiumViewHolder(layoutInflater.inflate(R.layout.item_iklan_premium, p, false));
    }

    @Override
    public void onBindViewHolder(final IklanPremiumViewHolder h, int p2) {
        h.progressBar.setVisibility(View.VISIBLE);
        final IklanPremium iklan = list.get(p2);
        h.textViewToko.setText(iklan.toko);
        h.textViewKeterangan.setText(iklan.keterangan);
        Glide.with(h.imageView.getContext()).load(iklan.gambar)
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

        h.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                if (context instanceof AppCompatActivity) {
                    BukaGambarDialog.getInstance(iklan.gambar).show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        // TODO: Implement this method
        return list.size();
    }

}
