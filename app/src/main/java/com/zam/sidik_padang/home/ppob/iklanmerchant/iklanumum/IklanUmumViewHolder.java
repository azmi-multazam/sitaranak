package com.zam.sidik_padang.home.ppob.iklanmerchant.iklanumum;

import android.view.View;
import android.widget.TextView;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.ppob.iklanmerchant.iklanpremium.IklanPremiumViewHolder;


public class IklanUmumViewHolder extends IklanPremiumViewHolder {
    View buttonKontak, buttonBeli;
    TextView textViewHarga, textViewNama;

    IklanUmumViewHolder(View v) {
        super(v);
        buttonKontak = v.findViewById(R.id.buttonKontak);
        buttonBeli = v.findViewById(R.id.buttonBeli);
        textViewHarga = (TextView) v.findViewById(R.id.textViewHarga);
        textViewNama = (TextView) v.findViewById(R.id.textViewNamaPemilik);
    }
}
