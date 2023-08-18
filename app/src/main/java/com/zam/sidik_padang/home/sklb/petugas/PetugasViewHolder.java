package com.zam.sidik_padang.home.sklb.petugas;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import com.zam.sidik_padang.R;

public class PetugasViewHolder extends RecyclerView.ViewHolder {

    TextView nama, alamat, umur, hp;
    ImageView edit, delete;

    public PetugasViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        nama = itemView.findViewById(R.id.textViewNama);
        alamat = itemView.findViewById(R.id.textViewAlamat);
        umur = itemView.findViewById(R.id.tvUmur);
        hp = itemView.findViewById(R.id.textViewHp);

        edit = itemView.findViewById(R.id.flow_edit);
        delete = itemView.findViewById(R.id.flow_delete);
    }
}
