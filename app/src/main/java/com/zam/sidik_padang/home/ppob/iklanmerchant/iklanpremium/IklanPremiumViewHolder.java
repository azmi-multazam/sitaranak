package com.zam.sidik_padang.home.ppob.iklanmerchant.iklanpremium;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;


public class IklanPremiumViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;
    public ImageView imageView;
    public TextView textViewToko, textViewKeterangan;

    public IklanPremiumViewHolder(View v) {
        super(v);
        imageView = (ImageView) v.findViewById(R.id.imageView);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        textViewToko = (TextView) v.findViewById(R.id.textViewToko);
        textViewKeterangan = (TextView) v.findViewById(R.id.textViewKeterangan);
    }
}
