package com.zam.sidik_padang.home.dataternak.dibawahnya;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 5/14/17.
 */

public class DataTernakViewHolder extends RecyclerView.ViewHolder {
    public View itemView, buttonDelete, header;
    public TextView textViewNama, textViewJenis, textViewBangsa, textViewKelamin, textViewUmur;

    public DataTernakViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        header = itemView.findViewById(R.id.item_data_ternak_dibawahnya_ViewHeader);
        textViewNama = (TextView) itemView.findViewById(R.id.item_data_ternak_dibawahnya_TextViewNama);
        textViewJenis = (TextView) itemView.findViewById(R.id.item_data_ternak_dibawahnya_TextViewJenis);
        textViewBangsa = (TextView) itemView.findViewById(R.id.item_data_ternak_dibawahnya_TextViewBangsa);
        textViewKelamin = (TextView) itemView.findViewById(R.id.item_data_ternak_dibawahnya_TextViewKelamin);
        textViewUmur = (TextView) itemView.findViewById(R.id.item_data_ternak_dibawahnya_TextViewUmur);
        buttonDelete = itemView.findViewById(R.id.item_data_ternak_dibawahnya_ButttonDelete);
    }
}
