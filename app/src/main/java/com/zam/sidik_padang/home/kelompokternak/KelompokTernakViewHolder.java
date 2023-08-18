package com.zam.sidik_padang.home.kelompokternak;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 5/7/17.
 */

public class KelompokTernakViewHolder extends RecyclerView.ViewHolder {

    public View view, buttonDelete;
    public TextView textViewNama, textViewAlamat, textViewAlamatKorwil;

    public KelompokTernakViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        textViewNama = (TextView) itemView.findViewById(R.id.item_kelompok_ternak_TextViewNama);
        textViewAlamat = (TextView) itemView.findViewById(R.id.item_kelompok_ternak_TextViewAlamat);
        textViewAlamatKorwil = (TextView) itemView.findViewById(R.id.item_kelompok_ternak_TextViewAlamatKorwil);
        buttonDelete = itemView.findViewById(R.id.item_kelompok_ternak_ButttonDelete);
    }
}
