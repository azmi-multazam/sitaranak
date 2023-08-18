package com.zam.sidik_padang.home.ppob.history.deposit;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;

import com.zam.sidik_padang.R;

public class HistoryDepositAdapter extends RecyclerView.Adapter<HistoryDepositViewHolder> {
    private final List<HistoryDeposit> list;

    public HistoryDepositAdapter(List<HistoryDeposit> list) {
        this.list = list;
    }

    @Override
    public HistoryDepositViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
        // TODO: Implement this method
        return new HistoryDepositViewHolder(LayoutInflater.from(p1.getContext()).inflate(R.layout.item_history_deposit, p1, false));
    }

    @Override
    public void onBindViewHolder(HistoryDepositViewHolder h, int p) {
        HistoryDeposit hd = list.get(p);
        h.textBank.setText(hd.bank);
        h.textLimit.setText(hd.limite);
        h.textStatus.setText(hd.status);
        h.textTanggal.setText(hd.tgl);
        h.textTotal.setText("Rp. " + NumberFormat.getInstance().format(hd.total));
    }

    @Override
    public int getItemCount() {
        // TODO: Implement this method
        return list.size();
    }

}
