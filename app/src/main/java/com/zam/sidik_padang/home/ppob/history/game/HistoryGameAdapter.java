package com.zam.sidik_padang.home.ppob.history.game;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryGameAdapter extends RecyclerView.Adapter<HistoryGameViewHolder> {
    private final List<HistoryGame> list;

    public HistoryGameAdapter(List<HistoryGame> list) {
        this.list = list;
    }

    @Override
    public HistoryGameViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
        // TODO: Implement this method
        return new HistoryGameViewHolder(new TextView(p1.getContext()));

    }

    @Override
    public void onBindViewHolder(HistoryGameViewHolder p1, int p2) {
//		p1.tv.setText(list.get(p2).str);
    }

    @Override
    public int getItemCount() {
        // TODO: Implement this method
        return list.size();
    }


}
