package com.zam.sidik_padang.home.sklb.petugas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.sklb.petugas.vm.PeternakUmum;

public class PeternakAdapter extends RecyclerView.Adapter<PetugasViewHolder> {

    private List<PeternakUmum> list;
    private final PetugasCallback callback;

    public PeternakAdapter(List<PeternakUmum> list, PetugasCallback callback) {
        this.list = list;
        this.callback = callback;
    }

    public void setList(List<PeternakUmum> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public PetugasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PetugasViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_petugas, parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull PetugasViewHolder holder, int position) {
        final PeternakUmum b = list.get(position);
        if (b != null) {
            holder.nama.setText(b.getNama());
            holder.umur.setText(String.valueOf(b.getUmur()));
            holder.alamat.setText(String.format("%s %s, %s", b.getAlamat(), b.getDesa(), b.getKecamatan()));
            holder.hp.setText(b.getHp());
            holder.edit.setVisibility(View.INVISIBLE);
            holder.delete.setVisibility(View.INVISIBLE);
            /*
            holder.edit.setOnClickListener(v -> {
                callback.onEditClicked(new Gson().toJson(b));
            });
            holder.delete.setOnClickListener(v -> {
                callback.onDeleteClicked(b.getId(), b.getNama());
            });
             */
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
