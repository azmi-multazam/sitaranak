package com.zam.sidik_padang.home.ppob.history.deposit;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.zam.sidik_padang.R;

public class HistoryDepositViewHolder extends RecyclerView.ViewHolder {
    View view;
    TextView textTanggal, textTotal, textBank, textStatus, textLimit;

    public HistoryDepositViewHolder(View view) {
        super(view);
        this.view = view;
        textTanggal = (TextView) view.findViewById(R.id.item_history_deposit_TextViewTanggal);
        textBank = (TextView) view.findViewById(R.id.item_history_deposit_TextViewBank);
        textTotal = (TextView) view.findViewById(R.id.item_history_deposit_TextViewTotal);
        textStatus = (TextView) view.findViewById(R.id.item_history_deposit_TextViewStatus);
        textLimit = (TextView) view.findViewById(R.id.item_history_deposit_TextViewLimit);
    }
}
