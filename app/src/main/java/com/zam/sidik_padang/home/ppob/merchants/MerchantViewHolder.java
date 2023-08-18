package com.zam.sidik_padang.home.ppob.merchants;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;


/**
 * Created by supriyadi on 2/18/18.
 */

public class MerchantViewHolder extends RecyclerView.ViewHolder {

    TextView textViewToko, textViewKeterangan;
    ImageView imageView;
    View progressBar;

    MerchantViewHolder(View v) {
        super(v);
        textViewToko = (TextView) v.findViewById(R.id.textViewNamaToko);
        textViewKeterangan = (TextView) v.findViewById(R.id.textViewKeterangan);
        imageView = (ImageView) v.findViewById(R.id.imageView);
        progressBar = v.findViewById(R.id.progressBar);
    }
}
