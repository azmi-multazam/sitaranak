package com.zam.sidik_padang.home.ppob.history.tagihan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;

import com.zam.sidik_padang.R;


/**
 * Created by supriyadi on 8/20/17.
 */

public class HistoryTagihanAdapter extends RecyclerView.Adapter<HistoryTagihanViewHolder> {
    private List<HistoryTagihan> list;
    private OnItemButtonClickListener listener;

    public HistoryTagihanAdapter(List<HistoryTagihan> list, OnItemButtonClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public HistoryTagihanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryTagihanViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_tagihan, parent, false));
    }

    @Override
    public void onBindViewHolder(HistoryTagihanViewHolder holder, int position) {
        final int pos = position;
        final HistoryTagihan tagihan = list.get(pos);
        holder.textViewTitle.setText(tagihan.produk + " - " + tagihan.idpel);
        holder.textViewNama.setText(": " + tagihan.namapel);
        String total = ": ";
        if (!tagihan.total.isEmpty())
            total += "Rp. " + NumberFormat.getInstance().format(Double.parseDouble(tagihan.total));
        holder.textViewTotal.setText(total);
        String buttonText = "Sukses dibayar";
        if (!tagihan.status.toLowerCase().contains("sukses")) {
            holder.button.setEnabled(true);

            if (tagihan.status.toLowerCase().contains("bayar")) {
                buttonText = "Bayar";
            } else buttonText = "Cek ulang";

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tagihan.status.toLowerCase().contains("bayar"))
                        listener.onItemButtonClickBayar(pos);
                    else listener.onItemButtonClickCekUlang(pos);
                }
            });
        } else {
            holder.button.setEnabled(false);

        }

        holder.button.setText(buttonText);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static interface OnItemButtonClickListener {
        void onItemButtonClickCekUlang(int position);

        void onItemButtonClickBayar(int position);
    }

}
