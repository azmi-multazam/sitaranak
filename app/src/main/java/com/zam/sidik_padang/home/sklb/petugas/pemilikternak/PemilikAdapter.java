package com.zam.sidik_padang.home.sklb.petugas.pemilikternak;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import com.zam.sidik_padang.R;
import com.zam.sidik_padang.home.sklb.petugas.vm.pemilik.PemilikTernak;
import com.zam.sidik_padang.util.Util;

public class PemilikAdapter extends RecyclerView.Adapter<PemilikViewHolder> {

    private List<PemilikTernak> list;
    private final PemilikCallback callback;

    public PemilikAdapter(List<PemilikTernak> list, PemilikCallback callback) {
        this.list = list;
        this.callback = callback;
    }

    public void setList(List<PemilikTernak> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public PemilikViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PemilikViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pemilik_ternak, parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull PemilikViewHolder holder, int position) {
        final PemilikTernak b = list.get(position);
        if (b != null) {
            holder.nama.setText(b.getNama());
            holder.kode.setText(String.valueOf(b.getPemilikTernak()));
            holder.alamat.setText(b.getAlamat());
            holder.hp.setText(Util.decodeUrl(b.getHp()));

            holder.itemView.setOnClickListener(v -> callback.onPemilikClick(b.getPemilikTernak(), b.getNama()));
            holder.edit.setOnClickListener(v -> callback.onEditClick(new Gson().toJson(b)));
            holder.delete.setOnClickListener(v -> callback.onDeleteClick(b.getId(), b.getNama()));
            /*
            holder.more.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(v.getContext(), holder.more);
                popup.inflate(R.menu.menu_list_more);

                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.more_edit) {
                        callback.onEditClick(new Gson().toJson(b));
                    } else if (item.getItemId() == R.id.more_delete) {
                        callback.onDeleteClick(b.getId());
                    }
                    return false;
                });

                // Force icons to show
                Object menuHelper;
                Class[] argTypes;
                try {
                    Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                    fMenuHelper.setAccessible(true);
                    menuHelper = fMenuHelper.get(popup);
                    argTypes = new Class[] { boolean.class };
                    menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
                } catch (Exception e) {
                    popup.show();
                    return;
                }
                popup.show();
            });
            */
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface PemilikCallback {
        void onPemilikClick(String kode, String nama);

        void onEditClick(String data);

        void onDeleteClick(String id, String nama);
    }

}
