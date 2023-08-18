package com.zam.sidik_padang.home.dataternak.detailternak.datarecording;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

/**
 * Created by supriyadi on 9/11/17.
 */

public class DataRecordingItemViewHolder extends RecyclerView.ViewHolder {
    TextView textViewTanggal, textViewBeratBadan, textViewPanjangBadan, textViewLingkarDada,
            textViewDalamDada, textViewTinggiBadan, textViewPbbh;
    View deleteButton;
    View view;

    public DataRecordingItemViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        deleteButton = itemView.findViewById(R.id.item_data_recording_ternak_ButttonDelete);
//		textViewNomor = (TextView) itemView.findViewById(R.id.item_data_recording_ternak_TextViewNomor);
        textViewTanggal = (TextView) itemView.findViewById(R.id.item_data_recording_ternak_TextViewTanggal);
        textViewBeratBadan = (TextView) itemView.findViewById(R.id.item_data_recording_ternak_TextViewBeratBadan);
        textViewPanjangBadan = (TextView) itemView.findViewById(R.id.item_data_recording_ternak_TextViewPAnjangBadan);
        textViewLingkarDada = (TextView) itemView.findViewById(R.id.item_data_recording_ternak_TextViewLingkarDada);
        textViewDalamDada = (TextView) itemView.findViewById(R.id.item_data_recording_ternak_TextViewDalamDada);
        textViewTinggiBadan = (TextView) itemView.findViewById(R.id.item_data_recording_ternak_TextViewTinggiBadan);
        textViewPbbh = (TextView) itemView.findViewById(R.id.item_data_recording_TextViewPbbh);

    }
}
