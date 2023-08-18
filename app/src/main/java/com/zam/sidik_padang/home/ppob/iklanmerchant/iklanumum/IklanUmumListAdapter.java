package com.zam.sidik_padang.home.ppob.iklanmerchant.iklanumum;

import android.content.Context;
import android.content.Intent;
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

import java.text.NumberFormat;
import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.newsinfo.gallery.bukaalbum.BukaGambarDialog;


public class IklanUmumListAdapter extends RecyclerView.Adapter<IklanUmumViewHolder> {
    public static final String EXTRA_IKLAN_UMUM = "extra_iklan_umum";

    private List<IklanUmum> list;
    private LayoutInflater layoutInflater;
    private boolean isIklanPribadi;

    public IklanUmumListAdapter(List<IklanUmum> list, boolean isIklanPribadi) {
        this.list = list;
        this.isIklanPribadi = isIklanPribadi;
    }


    @Override
    public IklanUmumViewHolder onCreateViewHolder(ViewGroup p, int p2) {
        if (layoutInflater == null) layoutInflater = LayoutInflater.from(p.getContext());
        return new IklanUmumViewHolder(layoutInflater.inflate(R.layout.item_iklan_umum, p, false));
    }

    @Override
    public void onBindViewHolder(final IklanUmumViewHolder h, int p2) {

        h.progressBar.setVisibility(View.VISIBLE);
        final IklanUmum iklan = list.get(p2);
        h.textViewToko.setText(iklan.toko);
        h.textViewNama.setText(iklan.nama);
        h.textViewKeterangan.setText(iklan.keterangan);
        h.textViewHarga.setText(iklan.harga.isEmpty() ? "" : "Rp " + NumberFormat.getNumberInstance().format(Long.parseLong(iklan.harga)));
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
        if (isIklanPribadi) {
            h.buttonKontak.setVisibility(View.INVISIBLE);
            h.buttonBeli.setVisibility(View.INVISIBLE);
        } else {
            h.buttonBeli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), TrxMerchantActivity.class);
                    intent.putExtra(EXTRA_IKLAN_UMUM, iklan);
                    view.getContext().startActivity(intent);
                }
            });
            h.buttonKontak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), IklanUmumKomentarActivity.class);
                    intent.putExtra(EXTRA_IKLAN_UMUM, iklan);
                    view.getContext().startActivity(intent);
                }
            });
        }
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
