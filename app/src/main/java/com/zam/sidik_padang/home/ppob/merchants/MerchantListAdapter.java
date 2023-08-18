package com.zam.sidik_padang.home.ppob.merchants;

import android.animation.ObjectAnimator;
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
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import com.zam.sidik_padang.R;


/**
 * Created by supriyadi on 2/18/18.
 */

public class MerchantListAdapter extends RecyclerView.Adapter<MerchantViewHolder> {

    List<Merchant> list;

    public MerchantListAdapter(List<Merchant> list) {
        this.list = list;
    }

    @Override
    public MerchantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MerchantViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_merchant, parent, false));
    }

    @Override
    public void onBindViewHolder(final MerchantViewHolder holder, int position) {
        Merchant m = list.get(position);
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(m.keterangan, Html.FROM_HTML_OPTION_USE_CSS_COLORS);
        } else spanned = Html.fromHtml(m.keterangan);
        holder.textViewKeterangan.setText(spanned);
        holder.textViewToko.setText(m.toko);
        ViewPropertyAnimation.Animator anim = new ViewPropertyAnimation.Animator() {

            @Override
            public void animate(View p1) {
                ObjectAnimator oa = ObjectAnimator.ofFloat(p1, View.ALPHA, 0, 1f);
                oa.setDuration(500);
            }
        };
        Glide.with(holder.imageView.getContext())
                .load(m.gambar).crossFade(1000)
                .animate(anim)
                .placeholder(R.drawable.ic_store)
                .listener(new RequestListener<Object, GlideDrawable>() {

                    @Override
                    public boolean onException(Exception p1, Object p2, Target<GlideDrawable> p3, boolean p4) {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable p1, Object p2, Target<GlideDrawable> p3, boolean p4, boolean p5) {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
