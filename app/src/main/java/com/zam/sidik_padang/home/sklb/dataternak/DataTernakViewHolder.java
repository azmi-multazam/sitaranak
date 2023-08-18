package com.zam.sidik_padang.home.sklb.dataternak;

import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 5/14/17.
 */

public class DataTernakViewHolder extends RecyclerView.ViewHolder {
    public View itemView, buttonDelete, header, pita;
    public TextView textViewNama, textViewJenis, textViewBangsa, textViewKelamin, textViewUmur, textViewPetugas, textViewPemilik;
    public TableRow rowPetugas, rowPemilik;

    public DataTernakViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        header = itemView.findViewById(R.id.item_data_ternak_dibawahnya_ViewHeader);
        textViewNama = itemView.findViewById(R.id.item_data_ternak_dibawahnya_TextViewNama);
        textViewJenis = itemView.findViewById(R.id.item_data_ternak_dibawahnya_TextViewJenis);
        textViewBangsa = itemView.findViewById(R.id.item_data_ternak_dibawahnya_TextViewBangsa);
        textViewKelamin = itemView.findViewById(R.id.item_data_ternak_dibawahnya_TextViewKelamin);
        textViewUmur = itemView.findViewById(R.id.item_data_ternak_dibawahnya_TextViewUmur);
        textViewPetugas = itemView.findViewById(R.id.item_data_ternak_dibawahnya_TextViewPetugas);
        textViewPemilik = itemView.findViewById(R.id.item_data_ternak_dibawahnya_TextViewPemilik);
        rowPetugas = itemView.findViewById(R.id.rowPetugas);
        rowPemilik = itemView.findViewById(R.id.rowPemilik);
        buttonDelete = itemView.findViewById(R.id.item_data_ternak_dibawahnya_ButttonDelete);
        pita = itemView.findViewById(R.id.pita);
    }
}
