package com.zam.sidik_padang.home.sklb.petugas.pemilikternak;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import com.zam.sidik_padang.R;

public class PemilikViewHolder extends RecyclerView.ViewHolder {

    TextView nama, alamat, kode, hp;
    ImageView edit, delete;//more;

    public PemilikViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        nama = itemView.findViewById(R.id.nama);
        alamat = itemView.findViewById(R.id.alamat);
        kode = itemView.findViewById(R.id.kode);
        hp = itemView.findViewById(R.id.hp);
        //more = itemView.findViewById(R.id.more);
        edit = itemView.findViewById(R.id.edit);
        delete = itemView.findViewById(R.id.delete);
    }
}
